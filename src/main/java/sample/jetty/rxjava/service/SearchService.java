package sample.jetty.rxjava.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import sample.jetty.service.InventoryService;
import sample.jetty.service.hystrix.GetInventoryCommand;
import sample.jetty.model.internal.SearchResult;
import sample.jetty.model.merchant1.Merchant1Product;
import sample.jetty.model.merchant2.Merchant2Product;
import sample.jetty.service.hystrix.GetProductCommand;
import sample.jetty.service.hystrix.GetProductIndexCommand;
import sample.jetty.model.internal.Product;
import sample.jetty.service.ProductService;
import sample.jetty.service.hystrix.GetMerchant1ProductsCommand;
import sample.jetty.service.hystrix.GetMerchant2ProductsCommand;

import java.util.List;

@Service("rxJavaService")
public class SearchService {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    public Observable<SearchResult> findExternalProducts(String query) {

       Observable<SearchResult> searchResultM1Observable =
               new GetMerchant1ProductsCommand(query)
                       .observe()
                       .flatMap(Observable::from)
                       .map(product -> toSearchResult(product));

        Observable<List<Merchant2Product>> productM2ListObservable =
                new GetMerchant2ProductsCommand(query).observe();

        Observable<Merchant2Product> productM2Observable =
                productM2ListObservable.flatMap(Observable::from);

        Observable<SearchResult> searchResultM2Observable =
                productM2Observable.map(this::toSearchResult);

        Observable<SearchResult> mergedSearchResultObservable =
            searchResultM1Observable.mergeWith(searchResultM2Observable);

        System.out.println(mergedSearchResultObservable);

        return mergedSearchResultObservable;
    }

    public Observable<SearchResult> findInternalProducts(String query) {

        //System.out.println("findInternalProducts: " + Thread.currentThread().getName());

        Observable<Long> productIndexObservable =
                getProductIndexObservable(query);

        return productIndexObservable
                .flatMap(this::productDetails);

    }

    private Observable<SearchResult> productDetails(Long productId) {

        //System.out.println("productDetails: " + Thread.currentThread().getName());

        Observable<Product> productObservable =
                retrieveProductFromProductSystem(productId);

        Observable<Long> quantityObservable =
                retrieveQuantityFromInventoryService(productId);

        return Observable.zip(productObservable,
                quantityObservable, SearchResult::new);
    }

    private SearchResult toSearchResult(Merchant1Product product) {
        //System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private SearchResult toSearchResult(Merchant2Product product) {
        //System.out.println("toSearchResult: " + Thread.currentThread().getName());
        return new SearchResult(new Product(product.getIdentifier(), product.getOrigin()), product.getQuantity());
    }

    private Observable<Product> retrieveProductFromProductSystem(Long productId) {
        //System.out.println("retrieveProductFromProductSystem: " + Thread.currentThread().getName());
        return new GetProductCommand(productId, productService).observe();
    }

    private Observable<Long> retrieveQuantityFromInventoryService(Long productId) {
        //System.out.println("retrieveQuantityFromInventoryService: " + Thread.currentThread().getName());
        return new GetInventoryCommand(productId, inventoryService).observe();
    }

    private List<Merchant2Product> findProducts(String query) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        // we ignore the query parameter. It can be added later
        return Client.create(clientConfig)
                .resource("http://localhost:9002/products/")
                .get(new GenericType<List<Merchant2Product>>() {});
    }

    private Observable<Long> getProductIndexObservable(String query) {
        return new GetProductIndexCommand(query).observe().flatMap(Observable::from);
    }

}
