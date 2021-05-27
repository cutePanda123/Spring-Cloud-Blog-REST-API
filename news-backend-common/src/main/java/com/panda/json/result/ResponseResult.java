package com.panda.json.result;

import java.util.Map;

public class ResponseResult {
    // response status
    private Integer status;

    // response message
    private String msg;

    // response success/failed flag
    private Boolean success;

    // response date
    private Object data;

    /**
     * If it returns successfully, if there is data, just drop the data data directly to the OK method
     * @param data
     * @return
     */
    public static ResponseResult ok(Object data) {
        return new ResponseResult(data);
    }
    /**
     * If it returns successfully, if there is no data, call the ok method directly, and data does not need to be passed in (in fact, it is null)
     * @return
     */
    public static ResponseResult ok() {
        return new ResponseResult(ResponseStatusEnum.SUCCESS);
    }
    public ResponseResult(Object data) {
        this.status = ResponseStatusEnum.SUCCESS.status();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }

    /**
     * To return an error, just call the error method directly. Of course, you can also customize the error in ResponseStatusEnum and then return.
     * @return
     */
    public static ResponseResult error() {
        return new ResponseResult(ResponseStatusEnum.FAILED);
    }

    /**
     * Error return, the map contains multiple error messages, which can be used for form validation to return all errors uniformly
     * @param map
     * @return
     */
    public static ResponseResult errorMap(Map map) {
        return new ResponseResult(ResponseStatusEnum.FAILED, map);
    }

    /**
     * Error return, return the error message directly
     * @param msg
     * @return
     */
    public static ResponseResult errorMsg(String msg) {
        return new ResponseResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * Error return, token exception, some common ones can be defined here
     * @return
     */
    public static ResponseResult errorTicket() {
        return new ResponseResult(ResponseStatusEnum.TICKET_INVALID);
    }

    /**
     * To customize the error range, you need to pass in a custom enumeration, which can be passed in after customizing in [ResponseStatusEnum.java]
     * @param responseStatus
     * @return
     */
    public static ResponseResult errorCustom(ResponseStatusEnum responseStatus) {
        return new ResponseResult(responseStatus);
    }
    public static ResponseResult exception(ResponseStatusEnum responseStatus) {
        return new ResponseResult(responseStatus);
    }

    public ResponseResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }
    public ResponseResult(ResponseStatusEnum responseStatus, Object data) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }
    public ResponseResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    public ResponseResult() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
