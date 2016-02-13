package sample.jetty.service.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import sample.jetty.model.internal.Product;
import sample.jetty.service.ProductService;

/**
 * Created by holger on 9/14/15.
 */
public class GetProductCommand extends HystrixCommand<Product> {

    private final Long productId;

    private final ProductService productService;

    public GetProductCommand(Long productId, ProductService productService) {
        super(HystrixCommandGroupKey.Factory.asKey("get-product"));
        this.productId = productId;
        this.productService = productService;
    }


    @Override
    protected Product run() throws Exception {
        System.out.println("GetProduct.run: " + Thread.currentThread().getName());
        return productService.getProduct(productId);
    }
}
