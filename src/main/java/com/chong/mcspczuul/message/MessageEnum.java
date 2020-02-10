package com.chong.mcspczuul.message;

public enum MessageEnum {
    ERROR_NO_AUTH(1010, "No auth."),
    ERROR_JWT_EXPIRE(1011,"jwt token expire."),
    ERROR_JWT_SIGNATURE(1012,"jwt token signature error."),
    ERROR_JWT_PARSE(1013,"jwt token parse error."),
    ERROR_SYSTEM(1000,"Service is unused.");

    private int code;
    private String message;

    private MessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code){
        for(MessageEnum messageEnum:MessageEnum.values()){
            if(messageEnum.getCode()==code){
                return messageEnum.getMessage();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
