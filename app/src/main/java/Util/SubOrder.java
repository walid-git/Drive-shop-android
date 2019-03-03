package Util;

import java.io.Serializable;

public class SubOrder implements Serializable {

    private Product product;
    private int quantity;

    public SubOrder(Product product, int quantity) {
        this.setProduct(product);
        this.setQuantity(quantity);
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        return ((SubOrder)obj).product.getId()==this.product.getId();
    }
}
