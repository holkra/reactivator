package sample.jetty.experiments;

import org.springframework.stereotype.Service;
import rx.Observable;
import sample.jetty.model.merchant2.Merchant2Product;

@Service
public class Merchant2ProductService {

    public Observable<Merchant2Product> findProducts(String searchTerm) {
        //System.out.println("Merchant2ProductService.findProducts: " + Thread.currentThread().getName());
        return Observable.just(new Merchant2Product(6l, 6l, "EXPERIMENT"), new Merchant2Product(7l, 7l, "EXPERIMENT"), new Merchant2Product(8l, 8l, "EXPERIMENT"), new Merchant2Product(9l, 9l, "EXPERIMENT"), new Merchant2Product(10l, 10l, "EXPERIMENT"));
    }
}
