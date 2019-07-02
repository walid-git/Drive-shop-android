package Shared;

import Observer.Observable;
import Observer.Observer;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable, Observable {
    private long id;
    private long customerId;
    private ArrayList<SendableSubOrder> sendableSubOrders;

    public enum State{NEW,IN_PROGRESS,PENDING,READY}

    private State orderState;

    ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Order(long id, long customerId, State orderState) {
        this.id = id;
        this.customerId = customerId;
        this.orderState = orderState;
        this.sendableSubOrders = new ArrayList<SendableSubOrder>();
    }

    public Order(long id, long customerId) {
        this.setId(id);
        this.setCustomerId(customerId);
        orderState = State.NEW;
        this.sendableSubOrders = new ArrayList<SendableSubOrder>();
    }

    public Order(long id, long customerId, ArrayList<SubOrder> subOrders) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.sendableSubOrders = new ArrayList<SendableSubOrder>();
        this.setSubOrders(subOrders);
        orderState = State.NEW;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setSendableSubOrders(ArrayList<SendableSubOrder> sendableSubOrders) {
        this.sendableSubOrders = sendableSubOrders;
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
        sendableSubOrders.add(new SendableSubOrder(subOrder.getProduct().getId(), subOrder.getQuantity()));
    }

    public State getOrderState() {
        return orderState;
    }

    public void setOrderState(State orderState) {
        this.orderState = orderState;
    }
}
