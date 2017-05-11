package org.pan.config;

/**
 * Created by panmingzhi on 2017/5/3.
 */
public class ResponseMsgBody {
    private Boolean success;
    private String msg;
    private Object data;

    public static ResponseMsgBody success(String msg, Object data) {
        return getResponseBody(msg, data, true);
    }

    public static ResponseMsgBody fail(String msg, Object data) {
        return getResponseBody(msg, data, false);
    }

    private static ResponseMsgBody getResponseBody(String msg, Object data, boolean success) {
        ResponseMsgBody responseBody = new ResponseMsgBody();
        responseBody.setSuccess(success);
        responseBody.setMsg(msg);
        responseBody.setData(data);
        return responseBody;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
}
