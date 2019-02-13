package org.lance.cloud.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.lance.cloud.domain.entity.enums.ProductStatus;
import org.lance.cloud.exception.ApplicationException;

import javax.persistence.*;
import java.util.Date;

/**
 * 产品
 */
@Entity
@ApiModel("产品")
//@JsonInclude(JsonInclude.Include.NON_EMPTY)   // 在配置文件设置可以不需要
public class Product {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Product decrease(Integer amount){
        if (amount == null || amount <= 0)
            return this;
        int result = getAmount() - amount;
        if (result < 0)
            throw new ApplicationException("产品数量少于零");
        this.amount = result;
        return this;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", amount=" + amount +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
