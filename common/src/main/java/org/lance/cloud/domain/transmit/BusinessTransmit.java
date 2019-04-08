package org.lance.cloud.domain.transmit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Fescar 测试业务类
 */
@Data
public class BusinessTransmit implements Serializable {

    @ApiModelProperty(value = "用户Id", example = "1")
    private String userId;

    @ApiModelProperty(value = "商品编码", example = "C201901140001")
    private String commodityCode;

    @ApiModelProperty(value = "订单数量", example = "1")
    private Integer count;

    @ApiModelProperty(value = "订单金额", example = "10")
    private BigDecimal amount;
}
