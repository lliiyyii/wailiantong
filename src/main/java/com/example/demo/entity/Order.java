package com.example.demo.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

//订单
public class Order {
    private int id;
    private int send_id;
    private String title;
    private String img;
    private String content;
    private int all_cus;//允许接受到的人数
    private int had_cus;//已经接受的人数
    private int price;//价格
    private Date time;
    private int credit;//发布者的信用评级
    private String real_name;//发布者的真名
    private String deadline;

    public Order(int send_id, String title, String img, String content, int all_cus, int price,String deadline) {
        this.send_id = send_id;
        this.title = title;
        this.img = img;
        this.content = content;
        this.all_cus = all_cus;
        this.price = price;
        this.deadline = deadline;
    }

    public Order(String title, String img, String content, int price, Date time) {
        this.title = title;
        this.img = img;
        this.content = content;
        this.price = price;
        this.time = time;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSend_id() {
        return send_id;
    }

    public void setSend_id(int send_id) {
        this.send_id = send_id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAll_cus() {
        return all_cus;
    }

    public void setAll_cus(int all_cus) {
        this.all_cus = all_cus;
    }

    public int getHad_cus() {
        return had_cus;
    }

    public void setHad_cus(int had_cus) {
        this.had_cus = had_cus;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime() {
        SimpleDateFormat dateformat2=new SimpleDateFormat("yyyy-MM-dd");
        String a2=dateformat2.format(time);
        return a2;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
