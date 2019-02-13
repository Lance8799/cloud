package org.lance.cloud.exception.enums;

/**
 * 应用异常错误类型
 */
public enum ErrorType {

    ILLEGAL_ARGUMENT(2000, "非法参数", false),

    ILLEGAL_REQUEST(3000, "非法请求", false),

    SYSTEM(9999, "系统异常", false);

    private Integer code;

    private String description;

    private boolean retry;

    ErrorType(Integer code, String description, boolean retry) {
        this.code = code;
        this.description = description;
        this.retry = retry;
    }

    public static ErrorType getByCode(String code){
        for (ErrorType type : ErrorType.values()){
            if (type.getCode().equals(code))
                return type;
        }
        return SYSTEM;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}
