package Shared;

import java.io.Serializable;

public class SendableSubOrder implements Serializable {
    private int productID;
    private int quantity;

    public SendableSubOrder(int productID, int quantity) {
        this.setProductID(productID);
        this.setQuantity(quantity);
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
