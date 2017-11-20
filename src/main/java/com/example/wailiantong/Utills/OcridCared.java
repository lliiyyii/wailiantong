package com.example.wailiantong.Utills;


public class OcridCared {
    String name;

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id_card_number;

    public OcridCared(String id_card_number, String name) {
        this.id_card_number = id_card_number;
        this.name = name;
    }
}
