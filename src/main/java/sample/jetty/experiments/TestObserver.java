package sample.jetty.experiments;

import rx.Observer;
import sample.jetty.model.internal.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holger on 9/13/15.
 */
public class TestObserver implements Observer<SearchResult> {

    final List<SearchResult> searchResultList = new ArrayList<>();

    @Override
    public void onCompleted() {
        System.out.println("I have finished");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(SearchResult searchResult) {
        System.out.println("added item: "+ searchResult);
        searchResultList.add(searchResult);
    }

    public List<SearchResult> getSearchResultList() {
        System.out.println(searchResultList);
        return searchResultList;
    }
}
