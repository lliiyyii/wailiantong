package com.example.demo.entity;

public class Usr {
    int id;
    String name;
    String pwd;
    int type;
    int com_id;
    int stu_id;
    int credit;
    String real_name;
    int rcv_cus_id;
    String identity_img;
    String business_img;
    String com_name;
    String head;
    int certify;

    public int getCertify() {
        return certify;
    }

    public void setCertify(int certify) {
        this.certify = certify;
    }

    public Usr(String name, String pwd, int type) {

        this.name = name;
        this.pwd = pwd;
        this.type = type;

    }
    public Usr(int id, String name, String pwd, int type) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.type = type;

    }
    public Usr( String name, String pwd) {

        this.name = name;
        this.pwd = pwd;


    }

    public Usr(int id, String identity,String real_name, String business,String head) {
        this.id = id;
        this.head = head;
        this.identity_img = identity;
        this.business_img = business;
        this.real_name = real_name;
    }

    public Usr(int id, String real_name) {
        this.id = id;

        this.real_name = real_name;
    }

    public Usr(int id, String real_name, String identity) {
        this.id = id;
        this.real_name = real_name;
        this.identity_img = identity;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Usr() {
        super();
    }

    public String getIdentity_img() {
        return identity_img;
    }

    public void setIdentity_img(String identity_img) {
        this.identity_img = identity_img;
    }

    public String getBusiness_img() {
        return business_img;
    }

    public void setBusiness_img(String business_img) {
        this.business_img = business_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public int getRcv_cus_id() {
        return rcv_cus_id;
    }

    public void setRcv_cus_id(int rcv_cus_id) {
        this.rcv_cus_id = rcv_cus_id;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
