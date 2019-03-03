package Util;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable{
    private int id;
    private int customerId;
    private ArrayList<SendableSubOrder> sendableSubOrders;

    public Order(int id, int customerId) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.sendableSubOrders = new ArrayList<SendableSubOrder>();
    }

    public Order(int id, int customerId, ArrayList<SubOrder> subOrders) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.sendableSubOrders = new ArrayList<SendableSubOrder>();
        this.setSubOrders(subOrders);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ArrayList<SendableSubOrder> getSendableSubOrders() {
        return sendableSubOrders;
    }

    public void setSubOrders(ArrayList<SubOrder> subOrders) {
        sendableSubOrders.clear();
        for (SubOrder subOrder : subOrders)
            sendableSubOrders.add(new SendableSubOrder(subOrder.getProduct().getId(), subOrder.getQuantity()));
    }

    public void addSubOrder(SubOrder subOrder) {
        sendableSubOrders.add(new SendableSubOrder(subOrder.getProduct().getId(),subOrder.getQuantity()));
    }

}
