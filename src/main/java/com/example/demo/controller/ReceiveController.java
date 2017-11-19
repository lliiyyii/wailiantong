package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseApi;
import com.example.demo.entity.Receiver;
import com.example.demo.entity.Usr;
import com.example.demo.service.ReceiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * 与接受需求有关的
 */
@RestController
@RequestMapping("/rcv")
public class ReceiveController {
    @Autowired
    private ReceiveService service;
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @PostMapping("/new")
    public BaseApi rcvOrder(@RequestBody JSONObject rcvJson){
        try {
            int order_id = Integer.parseInt(rcvJson.get("order_id").toString());
            int rcv_id = Integer.parseInt(rcvJson.get("user_id").toString());
            Receiver receiver = new Receiver(order_id, rcv_id);
            int status = service.addRcv(receiver);
            //返回真名和name
            JSONObject jsonObject = new JSONObject();
            if (status==1){
                List<Usr> list = service.getAllRcv(order_id);
                for(int i=0;i<list.size();i++){
                    jsonObject.put("realName",list.get(i).getReal_name());
                    jsonObject.put("phone",list.get(i).getName());
                }
                return new BaseApi("success",1,jsonObject);
            }else if(status==2){
                return new BaseApi("已预定",2,null);
            }
            else {
                return new BaseApi("预定失败",0,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }


    }

    /**
     * 查看接受需求的人
     * @param rcvjson
     * @return
     */
    @PostMapping("/view")
    public BaseApi viewOrder(@RequestBody JSONObject rcvjson){
        try {
            int oId = Integer.parseInt(rcvjson.get("order_id").toString());
            List<Usr> list = service.getAllRcvUser(oId);
            //返回真名和name
            List<JSONObject> jsonList = new LinkedList<>();

            for (Usr aList : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credit", aList.getCredit());
                jsonObject.put("phone", aList.getName());
                jsonObject.put("name",aList.getReal_name());
                jsonObject.put("rcv_id", aList.getRcv_cus_id());
                jsonList.add(jsonObject);
            }
                return new BaseApi("success",1,jsonList);
        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }    }
}
