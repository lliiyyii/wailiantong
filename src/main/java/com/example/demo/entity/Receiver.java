package com.example.demo.entity;

import java.util.Date;

public class Receiver {
    private int id;
    private int order_id;
    private int rcv_cus_id;
    private Date time;
    public Receiver(int order_id, int rcv_cus_id) {
        this.order_id = order_id;
        this.rcv_cus_id = rcv_cus_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getRcv_cus_id() {
        return rcv_cus_id;
    }

    public void setRcv_cus_id(int rcv_cus_id) {
        this.rcv_cus_id = rcv_cus_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
