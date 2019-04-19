package org.lance.cloud.seata.storage;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Lance
 */
@Data
@Entity
@Table(name = "storage_tbl")
@DynamicUpdate
public class Storage {

    @Id
    private Integer id;

    private String commodityCode;

    private String name;

    private Integer count;
}
