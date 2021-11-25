package com.example.bookstore.Object;

import java.util.Date;

public class Book {
    private String author;
    private String book_id;
    private String book_name;
    private String book_image;
    private Double cost;
    private Double total_number;
    private Date publish_day;

    public Book(){}
    public Book(String author, String book_id,String book_name, String book_image, Double cost, Double total_number, Date publish_day){
        this.author = author;
        this.book_id = book_id;
        this.book_name = book_name;
        this.book_image = book_image;
        this.cost = cost;
        this.total_number = total_number;
        this.publish_day = publish_day;
    }
    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getBook_id(){
        return book_id;
    }
    public void setBook_id(String book_id){
        this.book_id = book_id;
    }
    public String getBook_name(){
        return book_name;
    }
    public void setBook_name(String book_name){
        this.book_name = book_name;
    }
    public String getBook_image(){
        return book_image;
    }
    public void setBook_image(String book_image){
        this.book_image = book_image;
    }
    public Number getCost(){
        return cost;
    }
    public void setCost(Double cost){
        this.cost = cost;
    }
    public Number getTotal_number(){
        return total_number;
    }
    public void setTotal_number(Double total_number){
        this.total_number = total_number;
    }
    public Date getPublish_day(){
        return publish_day;
    }
    public void setPublish_day(Date publish_day){
        this.publish_day = publish_day;
    }
}
