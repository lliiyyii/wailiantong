package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseApi;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);


    /**
     * 新建订单
     * @param rcpJson
     * @return
     */
    @PostMapping(value = "/new")
    public BaseApi newOrder(@RequestBody JSONObject rcpJson) {
        try {
            log.info("进入order new 接受到 "+rcpJson);
            String title = rcpJson.get("title").toString();
            String img = rcpJson.get("img_url").toString();
            int price = Integer.parseInt(rcpJson.get("price").toString());
            String content = rcpJson.get("content").toString();
            int send_id = Integer.parseInt(rcpJson.get("send_id").toString());
            int allCus = Integer.parseInt(rcpJson.get("all_cus").toString());
            String deadline = rcpJson.get("deadline").toString();
            Order order = new Order(send_id,title,img, content, allCus,price,deadline);
            int orderId = orderService.addOrder(order);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("order_id",orderId);
            if(orderId==0){
                return new BaseApi("新建订单失败", 0, null);
            }else {
                return new BaseApi("success", 1, jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }

    }

    /**
     * 首页展示所有订单
     * 商家只能看到学生的
     * 学生只能看到商家的
     * @param rcpjson
     * @return
     */
    @PostMapping(value = "/main")
    public BaseApi allOrder(@RequestBody JSONObject rcpjson){
        try {
            log.info("进入order all 接受到 "+rcpjson);
            int userId = Integer.parseInt(rcpjson.get("user_id").toString());
            int type= 5;
            int next=Integer.parseInt(rcpjson.get("next").toString());;

            try {
                type=Integer.parseInt(rcpjson.get("type").toString());
            }catch (Exception e){}
            List<Order> list = null;
            int nextSql= next*10;
            switch (type){
                case 1:list=orderService.getCreditHign(userId,nextSql); break;
                case 2:list= orderService.getCreditLow(userId,nextSql);break;
                case 3:list=orderService.getPriceHign(userId,nextSql);break;
                case 4:list=orderService.getPriceLow(userId,nextSql);break;
                case 5:list = orderService.getAllOrder(userId,nextSql);break;
                case 6:list = orderService.getTimeFar(userId,nextSql);break;
                default:list = orderService.getAllOrder(userId,nextSql);break;

            }
//            List<Order> list = orderService.getAllOrder(userId,next*10);
            int allNum = orderService.getAllOrderNum(userId);
            JSONObject sendJson = new JSONObject();
            List<JSONObject> jsonList = new LinkedList<>();
            for(int i=0;i<list.size();i++) {
                JSONObject jsonObject = makeList(list,i);
                jsonList.add(jsonObject);
            }
            log.info("jL是 "+jsonList);
                sendJson.put("list",jsonList);
            if(allNum>(next+1)*10) {
                next = next+1;
                sendJson.put("next", String.valueOf(next));
            }
            else sendJson.put("next","");
            return new BaseApi("success",1,sendJson);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    @PostMapping(value = "search")
    public BaseApi searchOrder(@RequestBody JSONObject rcpjson){
        try{
            log.info("进入order search 接受到 "+rcpjson);
            int userId = Integer.parseInt(rcpjson.get("user_id").toString());
            String keywords = rcpjson.get("keywords").toString();
            int next=0;
            try {
                next=Integer.parseInt(rcpjson.get("next").toString());
            }catch (Exception e){}

            List<Order> list = orderService.getSearchOrder(keywords,userId,next*10);
            int allNum = orderService.getAllOrderNum(userId);
            log.info("查询数据库的内容是 "+list.toString());
            JSONObject sendJson = new JSONObject();
            List<JSONObject> jsonList = new LinkedList<>();
            for(int i=0;i<list.size();i++) {
               JSONObject jsonObject = makeList(list,i);
                jsonList.add(jsonObject);
            }
            log.info("jL是 "+jsonList);
            sendJson.put("list",jsonList);
            if(allNum>(next+1)*10) {
                next = next+1;
                sendJson.put("next", String.valueOf(next));
            }
            else sendJson.put("next","");
            return new BaseApi("success",1,sendJson);
        }catch (Exception e) {
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    /**
     * 订单详情
     * 自己可以看到有几人接受，别人看不到
     * @param rcpjson
     * @return
     */
    @PostMapping(value = "detail")
    public BaseApi detailOrder(@RequestBody JSONObject rcpjson){
        try{
            int userIdd = Integer.parseInt(rcpjson.get("user_id").toString());
            int orderId = Integer.parseInt(rcpjson.get("order_id").toString());
            log.info(orderId+"最初的id是 "+userIdd);
            int isMy = orderService.isMy(userIdd,orderId);
            log.info("isMy 的值是 "+isMy);
            Order order;
            if(isMy==0){
                log.info("不是我的订单");
                log.info(orderId+"现在的id是 "+userIdd);
                order = orderService.getDetailOrder(userIdd,orderId);
                log.info("查询结果是 "+order);
            }else order = orderService.getMyDetailOrder(userIdd,orderId);
            JSONObject jsonObject = makeJson(order);
            if (isMy!=0)
            {
                jsonObject.put("had_cus",order.getHad_cus());
                return new BaseApi("myOrder",2,jsonObject);
            }
            else {
                return new BaseApi("othersOrder",1,jsonObject);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    /**
     * 修改订单内容
     * @param
     * @return
     */
    @PostMapping(value = "/edit")
    public BaseApi editOrder(@RequestParam(value = "title",required = false) String title,
                             @RequestParam(value = "img_url",required = false) String img,
                             @RequestParam(value = "price",required = false) String price,
                             @RequestParam(value = "content",required = false) String content,
                             @RequestParam(value = "order_id") int order_id,
                             @RequestParam(value = "all_cus",required = false) String all_cus,
                             @RequestParam(value = "deadline",required = false) String deadline){
        try{
            String msg  = "success  ";
            int status = 1;
            if(title!=null)
            {
                status += orderService.editItem(title,order_id,"title");
                msg=msg+"title,";
            }
            if (img!=null)
            {
                status += orderService.editItem(img,order_id,"img");
                msg=msg+"img,";
            }
           if (content!=null){
               status +=orderService.editItem(content,order_id,"content");
               msg=msg+"content,";
            }if(deadline!=null){
                status +=orderService.editItem(deadline,order_id,"deadline");
                msg=msg+"deadline,";
            }if(price!=null){
                status += orderService.editIntItem(Integer.parseInt(price),order_id,"price");
                msg=msg+"price,";
            }if (all_cus!=null){
                status +=orderService.editIntItem(Integer.parseInt(all_cus),order_id,"all_cus");
                msg=msg+"all_cus,";
            }
            return new BaseApi(msg,status,null);
        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }
    }

    /**
     * 用户全部发送订单
     * @param rcpjson
     * @return
     */
    @PostMapping(value = "/mySend")
    public BaseApi mySend(@RequestBody JSONObject rcpjson){
        int id = Integer.parseInt(rcpjson.get("user_id").toString());

        int next=0;
        try {
            next=Integer.parseInt(rcpjson.get("next").toString());
        }catch (Exception e){}

        List<Order> list = orderService.getMySend(id,next);
        int allNum = orderService.getAllMySend(id);
        log.info("查询数据库的内容是 "+list.toString());
        JSONObject sendJson = new JSONObject();
        List<JSONObject> jsonList = new LinkedList<>();
        for(int i=0;i<list.size();i++) {
            JSONObject jsonObject = makeList(list,i);
            jsonList.add(jsonObject);
        }
        log.info("jL是 "+jsonList);
        sendJson.put("list",jsonList);
        if(allNum>(next+1)*10) {
            next = next+1;
            sendJson.put("next", String.valueOf(next));
        }
        else sendJson.put("next","");
        return new BaseApi("success",1,sendJson);
    }

//    /**
//     * 与用户相关的所有订单
//     * @param rcpjson
//     * @return
//     */
//    @PostMapping(value = "/myAll")
//    public BaseApi mySend(@RequestBody JSONObject rcpjson){
//
//    }
//
//    /**
//     * 用户收到的所有订单
//     * @param rcpjson
//     * @return
//     */
//    @PostMapping(value = "/myRcp")
//    public BaseApi mySend(@RequestBody JSONObject rcpjson){
//
//    }

    private JSONObject makeList(List<Order> list,int i){
            JSONObject jsonObject = new JSONObject();
            String content = "";
            jsonObject.put("order_id", list.get(i).getId());
            jsonObject.put("img_url", list.get(i).getImg());
            jsonObject.put("title", list.get(i).getTitle());
            content = list.get(i).getContent();
            log.info("content的长度是 " + content.length());
            jsonObject.put("content", 15 > content.length() ? content : content.substring(0, 14)+"…");
            jsonObject.put("time", list.get(i).getTime());
            jsonObject.put("price", list.get(i).getPrice());
            jsonObject.put("credit", list.get(i).getCredit());
            log.info("jO是 " + jsonObject);
            return jsonObject;

    }
    private JSONObject makeJson(Order order) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id",order.getId());
        jsonObject.put("img_url", order.getImg());
        jsonObject.put("title", order.getTitle());
        jsonObject.put("content", order.getContent());
        jsonObject.put("time", order.getTime());
        jsonObject.put("price", order.getPrice());
        jsonObject.put("credit", order.getCredit());
        jsonObject.put("deadline",order.getDeadline());
        log.info("jO是 " + jsonObject);
        return jsonObject;
    }


}
