package com.panda.utils;

import com.aliyuncs.utils.Base64Helper;
import com.panda.enums.FaceCompareType;
import com.panda.exception.*;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.utils.extend.AliyunResource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

@Component
public class FaceCompareUtils {
    final static Logger logger = LoggerFactory.getLogger(FaceCompareUtils.class);

    @Autowired
    private AliyunResource aliyunResource;

    private static final String gateway = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";

    /*
     * calculate MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            Base64Helper b64Encoder = new Base64Helper();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }

    /*
     * calculate HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new Base64Helper()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /*
     * convert to JavaScript's new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /**
     * send post request to compare two face images
     * @param type
     *          0: use url to compare，image url is not empty
     *          1: use image content to compare，image content is not empty
     * @param face1
     *          type 0，send image url，type 1 send base64
     * @param face2
     *          type 0，send image url，type 1 send base64
     * @return
     */
    // if send a base64 encoded string, a type 1 will be added
    // if send image url, a type is not needed
    public String getCompareFaceResult(int type, String face1, String face2) throws Exception {
        String body = "";
        if (type == FaceCompareType.base64.type) {
            body = "{\"content_1\": \"" + face1 + "\", \"content_2\":\"" + face2 + "\", \"type\":\"" + type + "\"}";
        } else if (type == FaceCompareType.imageUrl.type) {
            body = "{\"image_url_1\": \"" + face1 + "\", \"image_url_2\":\"" + face2 + "\", \"type\":\"" + type + "\"}";
        } else {
            EncapsulatedException.display(ResponseStatusEnum.FACE_VERIFY_TYPE_ERROR);
        }

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(gateway);
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.encrypt body with MD5+BASE64
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.calculate HMAC-SHA1
            String signature = HMACSha1(stringToSign, aliyunResource.getAccessKeySecret());
            // 3.get authorization header
            String authHeader = "Dataplus " + aliyunResource.getAccessKeyID() + ":" + signature;
            // open url connection
            URLConnection conn = realUrl.openConnection();
            // setup general properties
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Content-type", content_type);
            conn.setRequestProperty("Date", date);
            // add auth info
            conn.setRequestProperty("Authorization", authHeader);
            // these two are required by sending a post request
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // get URLConnection's output stream
            out = new PrintWriter(conn.getOutputStream());
            // send request body
            out.print(body);
            out.flush();
            statusCode = ((HttpURLConnection) conn).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }

    /**
     *
     * @param type
     * @param face1
     * @param face2
     * @param targetConfidence
     *          confidence threshold
     * @return
     */
    public boolean isFaceMatching(int type, String face1, String face2, double targetConfidence) {

        String response = null;
        try {
            response = getCompareFaceResult(type, face1, face2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> map = JsonUtils.jsonToPojo(response, Map.class);
        Object confidenceStr = map.get("confidence");
        Double responseConfidence = (Double)confidenceStr;

        logger.info("face compare result：{}", responseConfidence);

//        System.out.println(response.toString());
//        System.out.println(map.toString());

        return responseConfidence > targetConfidence;
    }

    public String getBase64StringFromImageUrl(String imgUrl){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            URL url = new URL(imgUrl);
            byte[] by = new byte[1024];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeBase64String(data.toByteArray());
    }

    public static void main(String[] args) {
        String face3 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF5MvvGAfnLXAAIHiv37wNk363.jpg";
        String face4 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF5Mv3yAH74mAACOiTd9pO4462.jpg";

        boolean result = new FaceCompareUtils().isFaceMatching(FaceCompareType.imageUrl.type, face3, face4, 60);

        logger.info("face compare result：{}", result);
    }

}
