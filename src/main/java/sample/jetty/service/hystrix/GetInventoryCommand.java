package sample.jetty.service.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import sample.jetty.service.InventoryService;

/**
 * Created by holger on 9/14/15.
 */
public class GetInventoryCommand extends HystrixCommand<Long> {

    private final Long productId;

    private final InventoryService inventoryService;

    public GetInventoryCommand(Long productId, InventoryService inventoryService) {
        super(HystrixCommandGroupKey.Factory.asKey("get-inventory"));
        this.productId = productId;
        this.inventoryService = inventoryService;
    }

    @Override
    protected Long run() throws Exception {
        System.out.println("GetInventory.run: " + Thread.currentThread().getName());
        return inventoryService.getNumberOnStock(productId);
    }
}
