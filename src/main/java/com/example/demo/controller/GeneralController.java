package com.example.demo.controller;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplication;
import com.example.demo.entity.BaseApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
@RestController
public class GeneralController {
    private static final Logger log = LoggerFactory.getLogger(GeneralController.class);

    @PostMapping(value = "/upload/img")

    public BaseApi uploadImg(HttpServletRequest request,
                             @RequestParam(value = "img",required = true) MultipartFile file) throws Exception{
        log.info("进入了upload ");
        try{
            String filename = file.getOriginalFilename();
            String path = request.getServletContext().getRealPath("/images/");
            log.info("文件名字是 "+filename+" 文件路径是 "+path);
            File filepath = new File(path,filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            //返回文件的路径
            JSONObject jsonObject = new JSONObject();
            path = DemoApplication.host+path.substring(path.indexOf("images")-1,path.length()-1);
//            replaceAll( "\\\\ ",   "/");
            path = path.replaceAll("\\\\\\\\","/");
            log.info("修改后的path是 "+path);
            path = path.replaceAll("\\\\","/");

            log.info("修改后的path是 "+path);
            String imgUrl = path+File.separator+filename;
            imgUrl = imgUrl.replaceAll("\\\\\\\\","/");
            imgUrl = imgUrl.replaceAll("\\\\","/");
            jsonObject.put("img_url",imgUrl);
            return new BaseApi("ok",1,jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return new BaseApi("error",0,null);
        }
    }
}
