package sample.jetty.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class InventoryService {

    public Long getNumberOnStock(Long productId) {
        return new Random().nextLong();
    }
}
