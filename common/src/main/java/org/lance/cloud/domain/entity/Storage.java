package org.lance.cloud.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Fescar中的库存
 */
@Data
@ToString
@TableName("t_storage")
public class Storage implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String commodityCode;
    private String name;
    private Integer count;
}
