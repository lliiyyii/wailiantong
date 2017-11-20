package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseApi;
import com.example.demo.entity.Com;
import com.example.demo.entity.Usr;
import com.example.demo.service.UserService;
import com.example.demo.util.ObjectHelper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping("/usr")
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/login")
    public BaseApi login(@RequestBody JSONObject rcpJson) {
        try {
            log.info("进入登录 信息是 " + rcpJson.toString());
            String usrName = rcpJson.get("phone").toString();

            String pwd = rcpJson.get("pwd").toString();

            Usr usr = new Usr(usrName, pwd);
            log.info("查询前用户" + usr.getId() + "登录用户" + usr.getName() + " 密码是 " + usr.getPwd() + "类型是" + usr.getType());
            usr = userService.login(usr);
            if (usr == null) {
                log.info("不存在用户 ");
                return new BaseApi("UserNotExists", 0, null);
            }
            log.info("用户" + usr.getId() + "登录用户" + usr.getName() + " 密码是 " + usr.getPwd() + "类型是" + usr.getType());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", usr.getId());
            jsonObject.put("phone", usr.getName());
            jsonObject.put("type", usr.getType());
            return new BaseApi("success", 1, jsonObject);

        } catch (Exception e) {
            log.info("出错了");
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }

    }

    @PostMapping(value = "register")
    public BaseApi register(@RequestBody JSONObject rcpJson) {
        try {
            log.info("进入 register " + rcpJson.toString());
            String usrName = rcpJson.get("phone").toString();
            int code = Integer.parseInt(rcpJson.get("code").toString());
            String pwd = rcpJson.get("pwd").toString();
            int type = Integer.parseInt(rcpJson.get("type").toString());
            if (code == 8888) {
                Usr usr = new Usr(usrName, pwd, type);
                int status = userService.register(usr);

                if (status == 1) {
                    log.info("成功");
                    return new BaseApi("success", 1, null);
                } else {
                    log.info("用户存在");
                    return new BaseApi("userExists", 0, null);
                }
            } else {
                log.info("验证码错误");
                return new BaseApi("codeError", 0, null);
            }
        } catch (Exception e) {
            log.info("出错了");
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    /**
     * 认证函数
     *
     * @param rcpJson 如果是商家 user_id,商家名，法人，营业执照，身份证照片
     *                学生 user_id,学生名，身份证照片
     * @return
     */
    @PostMapping("/certify/stu")
    public BaseApi certifyStu(@RequestBody JSONObject rcpJson) {
        try {
            log.info("进入学生认证 " + rcpJson);
            int id = Integer.parseInt(rcpJson.get("user_id").toString());
            String realName = rcpJson.get("realname").toString();
            String img = rcpJson.get("identity").toString();
            Usr usr = new Usr(id, realName, img);
            userService.cerifyStu(usr);
            return new BaseApi("success", 1, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    @PostMapping("/certify/com")
    public BaseApi certifyCom(@RequestBody JSONObject rcpJson) {
        try {
            log.info("进入学生认证 " + rcpJson);
            int id = Integer.parseInt(rcpJson.get("user_id").toString());
            String realName = rcpJson.get("realname").toString();
            String img = rcpJson.get("identity").toString();
            String business = rcpJson.get("business").toString();
            String head = rcpJson.get("head").toString();
            Usr usr = new Usr(id, img, realName, business, head);

            userService.cerifyCom(usr);
            return new BaseApi("success", 1, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    /**
     * 请求个人信息
     * @param
     * @return
     */
    @PostMapping(value = "/info",consumes = "application/json")
    public BaseApi info(@RequestBody JSONObject rcvJson ) {
        try {
            int id = Integer.parseInt(rcvJson.get("user_id").toString());
            Usr usr = userService.info(id);
            JSONObject jsonObject =makeJson(usr);


            return  new BaseApi("success",1, jsonObject);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    @PostMapping(value = "/info/update",consumes = "application/json")
    public BaseApi infoUp(@RequestBody JSONObject rcvJson ) {
        try {


            return  new BaseApi("success",1, null);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error", 0, null);
        }
    }

    private JSONObject makeJson(Usr usr){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",usr.getName());
        jsonObject.put("type",usr.getType());
        jsonObject.put("credit",usr.getCredit());
        jsonObject.put("real_name",usr.getReal_name());
        jsonObject.put("certify",usr.getCertify());
        if (usr.getIdentity_img()!=null)
        jsonObject.put("identity",usr.getIdentity_img());
        if (usr.getHead()!=null){
            jsonObject.put("head",usr.getHead());
        }
        if(usr.getBusiness_img()!=null){
            jsonObject.put("business",usr.getBusiness_img());
        }
        return jsonObject;
    }




}
