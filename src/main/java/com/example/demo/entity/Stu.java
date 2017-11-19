package com.example.demo.entity;

public class Stu {
    private int id;
    private int stu_id;
    private String img;
    private String identity_img;
    private String stu_name;

    public Stu(String img, String identity_img, String stu_name) {
        this.img = img;
        this.identity_img = identity_img;
        this.stu_name = stu_name;
    }

    public Stu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIdentity_img() {
        return identity_img;
    }

    public void setIdentity_img(String identity_img) {
        this.identity_img = identity_img;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }
}
