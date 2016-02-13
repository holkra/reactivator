package sample.jetty.service.hystrix;

import com.google.common.collect.ImmutableList;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import rx.Observable;
import sample.jetty.model.merchant2.Merchant2Product;

import java.util.List;

public class GetMerchant2ProductsCommand extends HystrixCommand<List<Merchant2Product>> {

    private final String query;

    public GetMerchant2ProductsCommand(String query) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("merchant1"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)));

        this.query = query;
    }

    @Override
    protected List<Merchant2Product> run() {
        System.out.println("GetMerchant2Products.construct: " + Thread.currentThread().getName());
        return findProducts(query);
    }

    private List<Merchant2Product> findProducts(String query) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        // we ignore the query parameter. It can be added later
        return Client.create(clientConfig)
                .resource("http://localhost:9002/products/")
                .get(new GenericType<List<Merchant2Product>>() {});
    }

    @Override
    protected List<Merchant2Product> getFallback() {
        return ImmutableList.of(new Merchant2Product(1l, 1l, "FALLBACK"), new Merchant2Product(2l, 2l, "FALLBACK"), new Merchant2Product(3l, 3l, "FALLBACK"), new Merchant2Product(4l, 4l, "FALLBACK"), new Merchant2Product(5l, 5l, "FALLBACK"));
    }

    private void notUserMethod() {

        Observable.just("Book A", "Book B", "Book C");


        Observable<Merchant2Product> a = Observable.from(findProducts(query));


        Observable.create(o -> {
            for (Merchant2Product product : findProducts(query)) {
                o.onNext(product);
            }
            o.onCompleted();
        });


    }

}










