package com.example.demo.dao;

import com.example.demo.entity.Msg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgDao {
    @Insert("INSERT INTO msg_list(order_id, send_id, rcp_id, time, content) " +
            "VALUES (#{msg.order_id},#{msg.send_id},#{msg.rcp_id},now(),#{msg.content});")
    void newMsgToMy(@Param("msg")Msg msg);
    @Insert("INSERT INTO msg_list(order_id, send_id, rcp_id, time, content) " +
            "select #{msg.order_id},#{msg.send_id},send_id,now(),#{msg.content} " +
            "from `order` where id = #{msg.order_id};")
    void newMsg(@Param("msg")Msg msg);
    @Select(" SELECT user.real_name,content,time\n" +
            "    FROM msg_list,user\n" +
            "    WHERE order_id=#{msg.order_id}\n" +
            "    AND (msg_list.send_id=user.id\n" +
            "            OR msg_list.rcp_id=user.id)\n" +
            "    AND( (send_id=#{msg.send_id} AND rcp_id=#{msg.rcp_id})\n" +
            "    OR (send_id=#{msg.rcp_id} AND rcp_id=#{msg.send_id}))\n" +
            "    ORDER BY msg_list.time  DESC ;")
    List<Msg> viewMyMsg(@Param("msg")Msg msg);


    @Select("SELECT user.real_name,content,time\n" +
            "    FROM msg_list,user\n" +
            "    WHERE order_id=#{msg.order_id}\n" +
            "    AND (msg_list.send_id=user.id\n" +
            "            OR msg_list.rcp_id=user.id)\n" +
            "    AND( (send_id=#{msg.send_id} AND rcp_id=(SELECT send_id FROM `order` WHERE `order`.id=#{msg.order_id}))\n" +
            "    OR (send_id=(SELECT send_id FROM `order` WHERE `order`.id=#{msg.order_id}) AND rcp_id=#{msg.send_id}))\n" +
            "    ORDER BY msg_list.time  DESC ;")
    List<Msg> viewMsg(@Param("msg")Msg msg);


}
