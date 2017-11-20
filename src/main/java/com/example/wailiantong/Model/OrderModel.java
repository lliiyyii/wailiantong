package com.example.wailiantong.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by weike on 2017/11/13.
 */

public class OrderModel {
    @SerializedName("order_id")
    private int id;
    private String title;
    @SerializedName("img_url")
    private String img;
    private String content;
    private String price;
    @SerializedName("credit")
    private String grade;
    private String person;
    private String time;
    private String deadline;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public OrderModel(int id, String title, String img, String content, String price, String grade, String person, String time, String deadline) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.content = content;
        this.price = price;
        this.grade = grade;
        this.person = person;
        this.time = time;
        this.deadline = deadline;
    }

    public OrderModel(int id, String title, String img, String content, String price) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.content = content;
        this.price = price;
    }

    public OrderModel(int id, String title, String img, String content, String price, String grade, String person, String time) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.content = content;
        this.price = price;
        this.grade = grade;
        this.person = person;
        this.time = time;
    }

    public OrderModel(String title, String content, String price, String grade) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }

    public String getGrade() {
        return grade;
    }

    public String getPerson() {
        return person;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
