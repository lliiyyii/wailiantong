package com.example.wailiantong.Utills;


public class FaceData {
    int time_used;
    String request_id;
    ResultFaceID result_faceid;
    ResultFaceID result_ref1;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public ResultFaceID getResult_faceid() {
        return result_faceid;
    }

    public void setResult_faceid(ResultFaceID result_faceid) {
        this.result_faceid = result_faceid;
    }

    public ResultFaceID getResult_ref1() {
        return result_ref1;
    }

    public void setResult_ref1(ResultFaceID result_ref1) {
        this.result_ref1 = result_ref1;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }
}
