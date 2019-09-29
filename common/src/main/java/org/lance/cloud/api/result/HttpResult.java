package org.lance.cloud.api.result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求返回结果
 *
 * @author Lance
 * @param <T>
 *     泛型主要为了swagger可以正常解析返回结果的具体内容，在controller中需要明确类型
 */
@Data
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

    public boolean success(){
        return OK == this.code;
    }

}
