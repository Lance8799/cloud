package org.lance.cloud.exception;


import org.lance.cloud.exception.enums.ErrorType;

/**
 * 应该自定义异常
 */
public class ApplicationException extends RuntimeException {

    private ErrorType errorType;

    public ApplicationException() {
    }

    public ApplicationException(ErrorType errorType){
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
