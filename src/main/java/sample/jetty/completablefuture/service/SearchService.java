package sample.jetty.completablefuture.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.jetty.model.internal.SearchResult;
import sample.jetty.service.InventoryService;
import sample.jetty.service.ProductService;
import sample.jetty.service.hystrix.GetProductCommand;
import sample.jetty.service.hystrix.GetProductIndexCommand;

import java.util.List;

@Service("completableFutureService")
public class SearchService {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    private List<SearchResult> findInternatProducts(String query) {
        List<Long> productIndexList = new GetProductIndexCommand(query).execute();

        productIndexList.stream()
                .map(productId -> new GetProductCommand(productId, productService).execute());

        return null;
    }





}
