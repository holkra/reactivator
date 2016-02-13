package sample.jetty.experiments;

import org.springframework.stereotype.Service;
import rx.Observable;
import sample.jetty.model.merchant1.Merchant1Product;

@Service
public class Merchant1ProductService {

    public Observable<Merchant1Product> searchProducts(String searchTerm) {
        //System.out.println("Merchant1ProductService.searchProducts: " + Thread.currentThread().getName());
        return Observable.just(new Merchant1Product(1l, 1l, "local"), new Merchant1Product(2l, 2l, "local"), new Merchant1Product(3l, 3l, "local"), new Merchant1Product(4l, 4l, "local"), new Merchant1Product(5l, 5l, "local"));
        //return Observable.create(new Merchant1ProductServiceOnSubscribe());
    }
}
