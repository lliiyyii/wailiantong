package com.example.demo.dao;

import com.example.demo.entity.Receiver;
import com.example.demo.entity.Usr;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveDao {
    @Select("select count(*) from rcv where order_id=#{rcv.order_id} and rcv_cus_id=#{rcv.rcv_id}")
    int isNew(@Param("rcv") Receiver receiver);

    @Insert("insert into rcv(order_id,rcv_cus_id,time) values (#{rcv.order_id},#{rcv.rcv_id},now()); ")
    void newRcv(@Param("rcv")Receiver receiver);

    @Update("update `order` set had_cus=had_cus+1 where id = #{rcv.order_id}")
    void newRcvS(@Param("rcv") Receiver receiver);

    @Select("select user.name,user.real_name FROM rcv,`order` a,user WHERE rcv.order_id = a.id " +
            "AND a.send_id = user.id AND rcv.order_id=#{id} ORDER BY rcv.time DESC ; ")
    List<Usr> allRcpUsr(@Param("id") int id);

    @Select("select rcv_cus_id,name,real_name,credit FROM user,rcv\n" +
            "WHERE order_id=#{oId}\n" +
            "  AND user.id=rcv.rcv_cus_id\n" +
            "ORDER BY rcv.time ;")
    List<Usr> allUsr(@Param("oId") int oId);
}
