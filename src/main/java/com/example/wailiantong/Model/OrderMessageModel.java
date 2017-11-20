package com.example.wailiantong.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by weike on 2017/11/14.
 */

public class OrderMessageModel {
    private String time,content;
    @SerializedName("name")
    private String people;

    public OrderMessageModel(String time, String people, String content) {
        this.time = time;
        this.people = people;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
