package org.lance.cloud.seata.order;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Lance
 */
@Data
@Entity
@Table(name = "order_tbl")
@DynamicUpdate
@DynamicInsert
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private Integer money;
}
