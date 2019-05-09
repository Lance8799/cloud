package org.lance.cloud.elasticsearch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "stock", type = "doc")
public class Stock {

    @Id
    private String code;

    private String name;

    private Integer market;
}
