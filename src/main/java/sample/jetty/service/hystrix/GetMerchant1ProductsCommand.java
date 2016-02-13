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
import sample.jetty.model.merchant1.Merchant1Product;

import java.util.List;

public class GetMerchant1ProductsCommand extends HystrixCommand<List<Merchant1Product>> {

    private final String query;

    public GetMerchant1ProductsCommand(String query) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("merchant1"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)));

        this.query = query;
    }

    @Override
    protected List<Merchant1Product> run() {
        System.out.println("GetMerchant1Products.construct: " + Thread.currentThread().getName());
        return findProducts(query);
    }

    private List<Merchant1Product> findProducts(String query) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        // we ignore the query parameter. It can be added later
        return Client.create(clientConfig)
                .resource("http://localhost:9001/products/")
                        .get(new GenericType<List<Merchant1Product>>(){});
    }

    @Override
    protected List<Merchant1Product> getFallback() {
        return ImmutableList.of(new Merchant1Product(1l, 1l, "FALLBACK"), new Merchant1Product(2l, 2l, "FALLBACK"), new Merchant1Product(3l, 3l, "FALLBACK"), new Merchant1Product(4l, 4l, "FALLBACK"), new Merchant1Product(5l, 5l, "FALLBACK"));
    }

}











