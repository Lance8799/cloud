package org.lance.cloud.seata.account;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Lance
 */
@Data
@Entity
@Table(name = "account_tbl")
@DynamicUpdate
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    private String name;

    private Integer money;

}
