package sample.jetty.rxjava.subscriber;

import org.springframework.web.context.request.async.DeferredResult;
import rx.Subscriber;
import sample.jetty.model.internal.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResultSubscriber extends Subscriber<SearchResult> {

    final private List<SearchResult> searchResultList = new ArrayList<>();

    final private DeferredResult<List<SearchResult>> deferredResult;

    @Override
    public void onNext(SearchResult searchResult) {
        searchResultList.add(searchResult);
    }

    @Override
    public void onCompleted() {
        deferredResult.setResult(searchResultList);
    }

    @Override
    public void onError(Throwable e) {
        deferredResult.setErrorResult(e);
    }

    public SearchResultSubscriber(DeferredResult<List<SearchResult>> deferredResult) {
        this.deferredResult = deferredResult;
    }
}
