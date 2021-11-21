package com.example.bookstore.Object;

public class Book {
    //private String book_id;
    private String book_name;
    private String book_image;
    private Double cost;
    private Double total_number;

    public Book(){}
    public Book(String book_name, String book_image, Double cost, Double total_number){
        //this.book_id = book_id;
        this.book_name = book_name;
        this.book_image = book_image;
        this.cost = cost;
        this.total_number = total_number;
    }
    public String getBook_name(){
        return book_name;
    }
    public void setBook_name(){
        this.book_name = book_name;
    }
    public String getBook_image(){
        return book_image;
    }
    public void setBook_image(){
        this.book_image = book_image;
    }
    public Number getCost(){
        return cost;
    }
    public void setCost(){
        this.cost = cost;
    }
    public Number getTotal_number(){
        return total_number;
    }
    public void setTotal_number(){
        this.total_number = total_number;
    }
}
