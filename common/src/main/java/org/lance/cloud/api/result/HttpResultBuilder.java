package org.lance.cloud.api.result;

import org.lance.cloud.exception.enums.ErrorType;

/**
 * Http返回结果的构建类，用于方便使用静态方法直接构建
 */
public class HttpResultBuilder {

    private HttpResultBuilder(){}

    public static <T> HttpResult<T> ok(T data, String message){
        return new HttpResult<>(HttpResult.OK, message, data);
    }

    public static <T> HttpResult<T> ok(T data){
        return HttpResultBuilder.ok(data, "");
    }

    public static <T> HttpResult<T> fail(Integer code, String message){
        return new HttpResult<>(code, message, null);
    }

    public static <T> HttpResult<T> fail(ErrorType type){
        return HttpResultBuilder.fail(type.getCode(), type.getDescription());
    }
}
