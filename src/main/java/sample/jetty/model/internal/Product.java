package sample.jetty.model.internal;

public class Product {

    private Long identifier;

    private String origin;

    public Product(Long identifier, String origin) {
        this.identifier = identifier;
        this.origin = origin;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (identifier != null ? !identifier.equals(product.identifier) : product.identifier != null) return false;
        return origin == product.origin;

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (origin != null ? origin.hashCode() : 0);
        return result;
    }
}
