package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.UserController;
import com.example.demo.entity.Usr;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ObjectHelper {
     public static String[] getNullPropertyNames(Object source) {
final BeanWrapperImpl src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
        Object srcValue = src.getPropertyValue(pd.getName());
        if (srcValue == null)
            emptyNames.add(pd.getName());
    }
    String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
}

    /**
     * @Description <p> 拷贝非空对象属性值 </P>
     * @param source 源对象
     *
     */
    public static Object copyPropertiesIgnoreNull(Object source) {
        Object target = new Object();
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    public JSONObject usrToJson(Usr usr){
        JSONObject jsonObject = new JSONObject();
        if (usr.getName()!=null){
            jsonObject.put("name",usr.getName());
        }
//        jsonObject.put("order_id",order.getId());
//        jsonObject.put("img_url", order.getImg());
//        jsonObject.put("title", order.getTitle());
//        jsonObject.put("content", order.getContent());
//        jsonObject.put("time", order.getTime());
//        jsonObject.put("price", order.getPrice());
//        jsonObject.put("credit", order.getCredit());
//        jsonObject.put("deadline",order.getDeadline());
        return jsonObject;
    }


}
