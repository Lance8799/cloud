package org.lance.cloud.elasticsearch.repository;

import org.lance.cloud.elasticsearch.domain.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface StockRepository extends ElasticsearchRepository<Stock, String> {
}
