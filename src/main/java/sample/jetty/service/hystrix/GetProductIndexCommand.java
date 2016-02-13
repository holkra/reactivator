package sample.jetty.service.hystrix;

import com.google.common.collect.ImmutableList;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.List;

/**
 * Created by holger on 9/14/15.
 */
public class GetProductIndexCommand extends HystrixCommand<List<Long>> {

    private final String query;

    public GetProductIndexCommand(String query) {
        super(HystrixCommandGroupKey.Factory.asKey("productIndex"));
        this.query = query;
    }

    @Override
    protected List<Long> run() throws Exception {
        System.out.println("GetProductIndex.run: " + Thread.currentThread().getName());
        return ImmutableList.of(1l, 2l, 3l, 4l, 5l);
    }
}
