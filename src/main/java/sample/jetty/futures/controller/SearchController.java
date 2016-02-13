package sample.jetty.futures.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.jetty.futures.service.SearchService;
import sample.jetty.model.internal.SearchResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller("futureController")
public class SearchController {

    @Autowired
    @Qualifier("futureService")
    private SearchService searchService;

    @RequestMapping("/searchWithFutures")
    @ResponseBody
    public List<SearchResult> findProducts(String searchTerm) throws ExecutionException, InterruptedException {
        return getSearchResults(searchTerm);
    }

    public List<SearchResult> getSearchResults(String query) throws ExecutionException, InterruptedException {
        return searchService.findProducts(query);
    }
}

