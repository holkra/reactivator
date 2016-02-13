package sample.jetty.streams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
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

import java.util.stream.Stream;

@Service
public class SearchService3 {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    public Observable<SearchResult> findExternalProductsWithHystrix(String query) {

        Stream<SearchResult> searchResultM1Stream =
               new GetMerchant1ProductsCommand(query).execute().parallelStream().map(this::toSearchResult);

        Stream<SearchResult> searchResultM2Stream =
                new GetMerchant2ProductsCommand(query).execute().parallelStream().map(this::toSearchResult);

//        Observable<SearchResult> mergedSearchResultObservable =
//            searchResultM1Observable.mergeWith(searchResultM2Observable);
//
//
//        System.out.println(mergedSearchResultObservable);

//        return mergedSearchResultObservable;
        return null;
    }

    public Observable<SearchResult> findInternalProducts(String query) {

        System.out.println("findInternalProducts: " + Thread.currentThread().getName());

        Observable<Long> productIndexObservable =
                new GetProductIndexCommand(query).observe().flatMap(Observable::from);

        return productIndexObservable.flatMap(productId -> {

            System.out.println("productIndexObservable.flatMap: " + Thread.currentThread().getName());

            Observable<Product> productObservable =
                    retrieveProductFromProductSystem(productId);

            Observable<Long> quantityObservable =
                    retrieveQuantityFromInventoryService(productId);

            return Observable.zip(productObservable,
                    quantityObservable, SearchResult::new);
        });
    }

    private SearchResult toSearchResult(Merchant1Product product) {
        System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private SearchResult toSearchResult(Merchant2Product product) {
        System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private Observable<Product> retrieveProductFromProductSystem(Long productId) {
        System.out.println("retrieveProductFromProductSystem: " + Thread.currentThread().getName());
        return new GetProductCommand(productId, productService).observe();
    }

    private Observable<Long> retrieveQuantityFromInventoryService(Long productId) {
        System.out.println("retrieveQuantityFromInventoryService: " + Thread.currentThread().getName());
        return new GetInventoryCommand(productId, inventoryService).observe();
    }
}
