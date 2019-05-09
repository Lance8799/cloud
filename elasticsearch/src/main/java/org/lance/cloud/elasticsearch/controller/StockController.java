package org.lance.cloud.elasticsearch.controller;

import org.elasticsearch.index.query.QueryBuilders;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.elasticsearch.domain.Stock;
import org.lance.cloud.elasticsearch.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository repository;

    @GetMapping("/search")
    public HttpResult search(String name){
        PageRequest pageRequest = PageRequest.of(1, 10);

        Page<Stock> stocks = repository.search(QueryBuilders.matchQuery("name", name), pageRequest);

        return HttpResultBuilder.ok(stocks);
    }
}
