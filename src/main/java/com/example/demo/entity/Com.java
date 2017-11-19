package com.example.demo.entity;

public class Com {
    private int id;
    private int com_id;
    private String name;
    private int verify;
    private String phone;
    private String head;//负责人
    private String img;//宣传照
    private String business;//营业执照
    private String identity_img;//身份证照片

    public Com(int com_id, String name, int verify, String phone, String head, String img, String business, String identity_img) {
        this.com_id = com_id;
        this.name = name;
        this.verify = verify;
        this.phone = phone;
        this.head = head;
        this.img = img;
        this.business = business;
        this.identity_img = identity_img;
    }
    public Com(){
        super();
    }

    public Com(String head, String business, String identity_img) {
        this.head = head;
        this.business = business;
        this.identity_img = identity_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getIdentity_img() {
        return identity_img;
    }

    public void setIdentity_img(String identity_img) {
        this.identity_img = identity_img;
    }
}
