package com.example.bookstore.Object;

public class CartItem {
    public String book_id;
    public String book_name;
    public long cost;
    public String book_image;
    public int total_number;

    public CartItem(String book_id, String book_name, long cost, String book_image, int total_number) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.cost = cost;
        this.book_image = book_image;
        this.total_number = total_number;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
