package com.example.wailiantong.Model;

/**
 * Created by weike on 2017/6/12.
 */

public class MessageModel {

    private String title;
    private String timestamp;
    private String content;

    public MessageModel(String title, String time, String content) {
        this.title = title;
        this.timestamp = time;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
