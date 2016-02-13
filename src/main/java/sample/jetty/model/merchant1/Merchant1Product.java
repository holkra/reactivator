package sample.jetty.model.merchant1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Merchant1Product {

    private String origin;

    private Long identifier;

    private Long quantity;

    public Merchant1Product() {
    }

    public Merchant1Product(Long identifier, Long quantity, String origin) {
        this.identifier = identifier;
        this.quantity = quantity;
        this.origin = origin;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
