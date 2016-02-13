package sample.jetty.model.internal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class SearchResult {

    private final Product product;

    private final Long quantity;

    public SearchResult(Product product, Long quantity) {
        //System.out.println("Search:new:  " + Thread.currentThread().getName());
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        return !(quantity != null ? !quantity.equals(that.quantity) : that.quantity != null);

    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
