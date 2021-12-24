package com.example.bookstore.Object;

public class Order {
    private String order_id;
    private String order_number;
    private String address;
    private boolean isPaid;
    public Order(){}
    public Order(String order_id, String order_number, String address, boolean isPaid) {
        this.order_number = order_number;
        this.address = address;
        this.isPaid = isPaid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
