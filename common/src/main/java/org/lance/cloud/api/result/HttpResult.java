package org.lance.cloud.api.result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.lance.cloud.exception.enums.ErrorType;

/**
 * 请求返回结果
 * @param <T>
 *     泛型主要为了swagger可以正常解析返回结果的具体内容，在controller中需要明确类型
 */
@ApiModel("返回结果集")
public class HttpResult<T> {

    public final static int OK = 1;

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("返回信息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    public HttpResult() {
    }

    public HttpResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public HttpResult ok(T data) {
        this.data = data;
        this.code = OK;
        return this;
    }

    public HttpResult fail(ErrorType error){
        this.code = error.getCode();
        this.message = error.getDescription();
        return this;
    }

    public HttpResult fail(Integer code, String message){
        this.code = code;
        this.message = message;
        return this;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success(){
        return OK == this.code;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
