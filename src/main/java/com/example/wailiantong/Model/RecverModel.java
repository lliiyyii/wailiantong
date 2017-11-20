package com.example.wailiantong.Model;

/**
 * Created by 蔚克 on 2017/11/20.
 */

public class RecverModel {
    private int credit;
    private int rcv_cus_id;
    private String name;
    private String phone;

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getRcv_cus_id() {
        return rcv_cus_id;
    }

    public void setRcv_cus_id(int rcv_cus_id) {
        this.rcv_cus_id = rcv_cus_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RecverModel(int credit, int rcv_cus_id, String name, String phone) {
        this.credit = credit;
        this.rcv_cus_id = rcv_cus_id;
        this.name = name;
        this.phone = phone;

    }


}
