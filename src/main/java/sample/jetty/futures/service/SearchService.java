package sample.jetty.futures.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.jetty.model.internal.Product;
import sample.jetty.model.internal.SearchResult;
import sample.jetty.model.merchant1.Merchant1Product;
import sample.jetty.model.merchant2.Merchant2Product;
import sample.jetty.service.InventoryService;
import sample.jetty.service.ProductService;
import sample.jetty.service.hystrix.GetInventoryCommand;
import sample.jetty.service.hystrix.GetMerchant1ProductsCommand;
import sample.jetty.service.hystrix.GetMerchant2ProductsCommand;
import sample.jetty.service.hystrix.GetProductCommand;
import sample.jetty.service.hystrix.GetProductIndexCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service("futureService")
public class SearchService {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;


    public List<SearchResult> findProducts(String query) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<List<SearchResult>> internalResultFuture =
                executorService.submit(() -> findInternalProducts(query));
        Future<List<SearchResult>> externalResultFuture =
                executorService.submit(() -> findExternalProducts(query));

        List<SearchResult> result = internalResultFuture.get();
        result.addAll(externalResultFuture.get());

        return result;
    }

    private List<SearchResult> findInternalProducts(String query) throws ExecutionException, InterruptedException {

        Future<List<Long>> productIndexFuture = getProductIndexFuture(query);

        List<Long> productIdList = productIndexFuture.get();

        List<FuturePair> futurePairList = new ArrayList<>();
        for (Long productId : productIdList) {
            Future<Product> productFuture = retrieveProductFromProductSystem(productId);
            Future<Long> quantityFuture = retrieveQuantityFromInventoryService(productId);
            futurePairList.add(new FuturePair(productFuture, quantityFuture));
        }

        List<SearchResult> searchResultList = new ArrayList<>();
        for (FuturePair futurePair : futurePairList) {
            Product product = futurePair.getProductFuture().get();
            Long quantity = futurePair.getQuantityFuture().get();
            SearchResult searchResult = new SearchResult(product, quantity);
            searchResultList.add(searchResult);
        }

        return searchResultList;
    }


    private List<SearchResult> findExternalProducts(String query) throws ExecutionException, InterruptedException {


        Future<List<Merchant1Product>> searchResultM1Future =
                new GetMerchant1ProductsCommand(query).queue();

        Future<List<Merchant2Product>> searchResultM2Future =
                new GetMerchant2ProductsCommand(query).queue();

        List<Merchant1Product> searchResultM1 = searchResultM1Future.get();
        List<Merchant2Product> searchResultM2 = searchResultM2Future.get();

        List<SearchResult> externalSearchResults = new ArrayList<>();
        for (Merchant1Product merchant1Product : searchResultM1) {
            externalSearchResults.add(toSearchResult(merchant1Product));
        }

        for (Merchant2Product merchant2Product : searchResultM2) {
            externalSearchResults.add(toSearchResult(merchant2Product));
        }




        return externalSearchResults;


    }

    private Future<List<Long>> getProductIndexFuture(String query) {
        return new GetProductIndexCommand(query).queue();
    }

    private Future<Product> retrieveProductFromProductSystem(Long productId) {
        return new GetProductCommand(productId, productService).queue();
    }

    private Future<Long> retrieveQuantityFromInventoryService(Long productId) {
        return new GetInventoryCommand(productId, inventoryService).queue();
    }

    private SearchResult toSearchResult(Merchant1Product product) {
        //System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private SearchResult toSearchResult(Merchant2Product product) {
        //System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private class FuturePair {

        private final Future<Product> productFuture;

        private final Future<Long> quantityFuture;

        public FuturePair(Future<Product> productFuture, Future<Long> quantityFuture) {
            this.productFuture = productFuture;
            this.quantityFuture = quantityFuture;
        }

        public Future<Long> getQuantityFuture() {
            return quantityFuture;
        }

        public Future<Product> getProductFuture() {
            return productFuture;
        }
    }
}
