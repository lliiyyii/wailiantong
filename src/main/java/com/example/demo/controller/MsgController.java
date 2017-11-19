package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseApi;
import com.example.demo.entity.Msg;
import com.example.demo.service.MsgService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    MsgService msgService;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MsgController.class);

    /**
     * 新建留言
     */
    @PostMapping("/new")
    public BaseApi newMsg(@RequestBody  JSONObject rcvJson){
        log.info("进入新建订单");
        try{
            log.info("进入新建订单"+rcvJson);
            int oId = Integer.parseInt(rcvJson.get("order_id").toString());
            int sendId = Integer.parseInt(rcvJson.get("user_id").toString());
            int rcvId=0;//不是自己的订单
            //如果不是自己的订单，则不需要rcvID
            try{
                rcvId = Integer.parseInt(rcvJson.get("rcv_id").toString());
            }catch (Exception e){

            }
            String content = rcvJson.get("content").toString();
            if(rcvId==0){
                Msg msg = new Msg(oId,sendId,content);
                log.info("不是自己的订单");
                msgService.newMsg(msg);
            }
            else {
                Msg msg = new Msg(oId,sendId,rcvId,content);
                log.info("自己的订单");
                msgService.newMsgMy(msg);
            }
            return new BaseApi("success",1,null);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }
    }

    /**
     * 查看留言
     */
    @PostMapping("/view")
    public BaseApi viewMsg(@RequestBody  JSONObject rcvJson){
        log.info("进入查看订单");
        try{
            //不是自己的订单 传入order_id user_id
            //自己的订单 传入order_id user_id rcv_id
            log.info(""+rcvJson);
            int oId = Integer.parseInt(rcvJson.get("order_id").toString());
            int sendId = Integer.parseInt(rcvJson.get("user_id").toString());
            int rcvId=0;//不是自己的订单
            try{
                rcvId = Integer.parseInt(rcvJson.get("rcv_id").toString());
            }catch (Exception e){

            }
            List<Msg> list = null;
            if(rcvId==0){
                Msg msg = new Msg(oId,sendId);
                log.info("不是自己的订单");
                list=msgService.viewMsg(msg);
            }
            else {
                Msg msg = new Msg(oId,sendId,rcvId);
                log.info("自己的订单");
                list=msgService.viewMgMsg(msg);
            }
            List<JSONObject> jsonList = new LinkedList<>();
            for(Msg a:list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",a.getReal_name());
                jsonObject.put("content",a.getContent());
                jsonObject.put("time",a.getTime());
                jsonList.add(jsonObject);
            }
            return new BaseApi("success",1,jsonList);


        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }
    }

}
