package com.example.demo.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

//留言实体
public class Msg {
    private int id;
    private int order_id;
    private int send_id;
    private int rcp_id;
    private String content;
    private Date time;
    private String real_name;

    public Msg(int order_id, int send_id, int rcp_id, String content) {
        this.order_id = order_id;
        this.send_id = send_id;
        this.rcp_id = rcp_id;
        this.content = content;
    }

    public Msg(int order_id, int send_id, String content) {
        this.order_id = order_id;
        this.send_id = send_id;
        this.content = content;
    }

    public Msg(int order_id, int send_id) {
        this.order_id = order_id;
        this.send_id = send_id;
    }

    public Msg(int order_id, int send_id, int rcp_id) {
        this.order_id = order_id;
        this.send_id = send_id;
        this.rcp_id = rcp_id;
    }

    public Msg(String content) {
        this.content = content;
    }
    public Msg() {
        super();
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

    public int getSend_id() {
        return send_id;
    }

    public void setSend_id(int send_id) {
        this.send_id = send_id;
    }

    public int getRcp_id() {
        return rcp_id;
    }

    public void setRcp_id(int rcp_id) {
        this.rcp_id = rcp_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        SimpleDateFormat dateformat2=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String a2=dateformat2.format(time);
        return a2;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}

