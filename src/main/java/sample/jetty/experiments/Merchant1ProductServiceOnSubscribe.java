package sample.jetty.experiments;

import rx.Observable;
import rx.Subscriber;
import sample.jetty.model.merchant1.Merchant1Product;

/**
 * Created by holger on 9/13/15.
 */
public class Merchant1ProductServiceOnSubscribe implements Observable.OnSubscribe<Merchant1Product> {

    @Override
    public void call(Subscriber<? super Merchant1Product> subscriber) {
        subscriber.onNext(new Merchant1Product(1l, 1l, "local"));
        subscriber.onNext(new Merchant1Product(2l, 2l, "local"));
        subscriber.onNext(new Merchant1Product(3l, 3l, "local"));
        subscriber.onError(new Exception("Et hätt noch immer jot jejange!!!"));
        subscriber.onNext(new Merchant1Product(5l, 5l, "local"));
    }
}
