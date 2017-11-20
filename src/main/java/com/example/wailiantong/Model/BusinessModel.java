package com.example.wailiantong.Model;

/**
 * Created by 蔚克 on 2017/6/2.
 */

public class BusinessModel {

    private int id;
    private String type;
    private String party_name;
    private String certificate_num;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getCertificate_num() {
        return certificate_num;
    }

    public void setCertificate_num(String certificate_num) {
        this.certificate_num = certificate_num;
    }

    public BusinessModel(int id, String type, String party_name, String certificate_num) {
        this.id = id;
        this.type = type;
        this.party_name = party_name;
        this.certificate_num = certificate_num;

    }

}
