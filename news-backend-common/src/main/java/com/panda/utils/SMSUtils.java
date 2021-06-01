package com.panda.utils;

import com.azure.communication.sms.SmsClient;
import com.azure.communication.sms.SmsClientBuilder;
import com.azure.communication.sms.models.SmsSendResult;
import com.panda.utils.extend.AzureResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils {
    final static Logger logger = LoggerFactory.getLogger(SMSUtils.class);

    @Autowired
    private AzureResource azureResource;

    private static String connectionString;
    private final String fromPhoneNumber = "+123456789";
    private static SmsClient smsClient;

    public void sendSms(String toPhoneNumber, String code) {
        SMSUtils.connectionString = azureResource.getConnectionString();
        smsClient = new SmsClientBuilder()
                .connectionString(SMSUtils.connectionString)
                .buildClient();

        SmsSendResult sendResult = smsClient.send(
                fromPhoneNumber,
                toPhoneNumber,
                "Your security code is: " + code + ". It expires in 10 minutes. Do not share this code with anyone.");
        SMSUtils.logger.info("Message Id: " + sendResult.getMessageId());
        SMSUtils.logger.info("Recipient Number: " + sendResult.getTo());
        SMSUtils.logger.info("Send Result Successful:" + sendResult.isSuccessful());
    }

    public void sendSmsMock(String toPhoneNumber, String code) {
        SMSUtils.logger.info("mocked send sms method runs");
    }
}
