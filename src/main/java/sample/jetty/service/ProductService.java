package sample.jetty.service;

import org.springframework.stereotype.Service;
import sample.jetty.model.internal.Product;
import sample.jetty.model.internal.Origin;

@Service
public class ProductService {

    public Product getProduct(Long productId) {
        return new Product(productId, Origin.INTERNAL.name());
    }
}
