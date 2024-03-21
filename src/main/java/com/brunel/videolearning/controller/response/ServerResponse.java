package com.brunel.videolearning.controller.response;

/**
 * Customize the data structure of the object returned by the controller to the frontend
 */
public class ServerResponse {

    //code attribute :
    // If it is 0, it means that the frontend UI does not need to display the content in 'message',
    // and if it is 1, it means that : backend service advice frontend UI to display the content in 'message'.
    private Integer code = 0;

    //message attribute : When returning data, you can also include a message
    // to be displayed on the frontend UI.
    // The decision of whether to display it is determined by the 'code' field.
   private String message;

    //data attribute: Return specific data to the frontend UI, which is a generics type.
    private Object data;

    public ServerResponse() {
        super();
    }

    public ServerResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    //When returning normally, use this 'ok' static method to construct the return object.
    // The properties of the object are passed in as parameters.
    // There are 2 'ok' methods :
    // This ok method has no parameter for 'code', and by default it is 0.
    public static ServerResponse ok(String message, Object data) {
        return new ServerResponse(0, message, data);
    }


    //This ok method has a parameter for 'code'
    public static ServerResponse ok(Integer code,String message, Object data) {
        return new ServerResponse(code, message, data);
    }

    //Fail method: The fail method does not return data, so it only has 2 parameters: 'code' and 'message'.
    public static ServerResponse fail(Integer code, String message) {
        return new ServerResponse(code, message, null);

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
