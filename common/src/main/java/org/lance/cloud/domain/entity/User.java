package org.lance.cloud.domain.entity;


import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lance
 */
@Data
@TableName("user")
public class User {

    @TableId
    private Long id;

    @TableField(condition = SqlCondition.LIKE)
    private String name;

    /**
     * condition为小于
     */
    @TableField(condition = "%s&lt;#{%s}")
    private Integer age;

    private String email;

    /**
     * 上级id
     */
    @TableField("superior_id")
    private Long superiorId;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private String remark;
}
