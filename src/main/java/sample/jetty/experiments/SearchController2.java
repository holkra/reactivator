package sample.jetty.experiments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;
import sample.jetty.model.internal.SearchResult;
import sample.jetty.rxjava.service.SearchService;

import java.util.List;

/**
 * NOTE: Controller is buggy as it tries to use an async result with a synchronous endpoint which results in random results.
 */
//@Controller
public class SearchController2 {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    @ResponseBody
    public List<SearchResult> findProducts(String query) {
        return getSearchResults(query);
    }

    public List<SearchResult> getSearchResults(String query) {
        Observable<SearchResult> searchResultObservable =
                searchService.findInternalProducts(query)
                    .mergeWith(searchService.findExternalProducts(query));

        TestObserver testObserver = new TestObserver();
        searchResultObservable.subscribe(testObserver);

        return testObserver.getSearchResultList();
    }
}
