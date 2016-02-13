package sample.jetty.rxjava.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import sample.jetty.model.internal.SearchResult;
import sample.jetty.rxjava.service.SearchService;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.jetty.rxjava.subscriber.SearchResultSubscriber;

import java.util.Iterator;
import java.util.List;

@Controller("rxController")
public class SearchController {

    @Autowired
    @Qualifier("rxJavaService")
    private SearchService searchService;

    @RequestMapping("/searchWithRx")
    @ResponseBody
    public List<SearchResult> findProducts(String query) {
        Observable<SearchResult> searchResults =
                searchService.findInternalProducts(query)
                        .mergeWith(searchService.findExternalProducts(query));

        Iterator<SearchResult> searchResultIterator
                = searchResults.toBlocking().getIterator();

        return Lists.newArrayList(searchResultIterator);
    }

    @RequestMapping("/searchWithRxDeferred")
    @ResponseBody
    public DeferredResult<List<SearchResult>> findProductsDeferred(String query) {
        Observable<SearchResult> searchResults =
                searchService.findInternalProducts(query)
                        .mergeWith(searchService.findExternalProducts(query));

        DeferredResult<List<SearchResult>> deferredResult = new DeferredResult<>();
        searchResults.subscribe(new SearchResultSubscriber(deferredResult));

        return deferredResult;
    }

    private void printThread(SearchResult r) {
        System.out.println("Am Ende: " + Thread.currentThread().getName() + "Result: " + r);
    }
}
