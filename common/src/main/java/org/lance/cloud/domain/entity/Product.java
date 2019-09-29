package org.lance.cloud.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lance.cloud.domain.entity.enums.ProductStatus;
import org.lance.cloud.exception.ApplicationException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 产品
 */
@Data
@Entity
@ApiModel("产品")
@Accessors(chain = true)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)   // 在配置文件设置可以不需要
public class Product implements Serializable {

//    @GenericGenerator(name = "uuid-gen", strategy = "uuid")
//    @GeneratedValue(generator = "uuid-gen")
    @Id
    @ApiModelProperty("产品编号")
    private String id;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty(hidden = true)
    @Enumerated(EnumType.ORDINAL)
    private ProductStatus status;

    @ApiModelProperty("数量")
    private Integer amount;

    @ApiModelProperty("创建时间")
    @Temporal(TemporalType.TIMESTAMP)   // 格式化日期
    private Date createAt;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Temporal(TemporalType.TIMESTAMP)   // 格式化日期
    private Date updateAt;

    /**
     * 持久化之前自动填充实体属性
     */
    @PrePersist
    public void onCreate() {
        createAt = updateAt = new Date();
    }

    /**
     * 每次更新实体时更新实体的属性
     */
    @PreUpdate
    public void onUpdate() {
        this.updateAt = new Date();
    }

    public Product decrease(Integer amount){
        if (amount == null || amount <= 0) {
            return this;
        }
        int result = getAmount() - amount;
        if (result < 0) {
            throw new ApplicationException("产品数量少于零");
        }
        this.amount = result;
        return this;
    }

    public static Product generate() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Product().setId(UUID.randomUUID().toString());
    }

}
