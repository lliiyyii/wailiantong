package com.example.demo.dao;

import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository
public interface OrderDao {
    @Insert("INSERT INTO `order`(send_id, title, img, content, time, all_cus, price,deadline) " +
            "VALUES (#{m.send_id},#{m.title},#{m.img},#{m.content}," +
            "now(),#{m.all_cus},#{m.price},#{m.deadline});")
    void insertOrder(@Param("m") Order order);

    @Select("SELECT max(id) from `order`")
    int selectOrderId();

    //每次10个获取全部订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by a.time desc limit #{next},10;")
    List<Order> allOrder(@Param("id") int id,@Param("next") int next);

    //订单数量
    @Select("select count(*) from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id});")
    int allOrderNum(@Param("id") int id);

    //信用从高到低获取订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by user.credit desc limit #{next},10;")
    List<Order> creditHignOrder(@Param("id") int id,@Param("next") int next);

    //信用从低到高获取订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by user.credit limit #{next},10;")
    List<Order> creditLowOrder(@Param("id") int id,@Param("next") int next);

    //价格从高到低获取订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by a.price desc limit #{next},10;")
    List<Order> priceHignOrder(@Param("id") int id,@Param("next") int next);

    //价格从低到高获取订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by a.price limit #{next},10;")
    List<Order> priceLowOrder(@Param("id") int id,@Param("next") int next);

    //时间从远到近获取订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=user.id AND now()<a.deadline " +
            "and user.type !=(select type from user where user.id=#{id}) order by a.deadline limit #{next},10;")
    List<Order> timeFarOrder(@Param("id") int id,@Param("next") int next);

    /**
     * 搜索订单
     * @param keywords
     * @param id
     *
     * @return
     */
    @Select("select  a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.title like CONCAT('%',#{keywords},'%') " +
            "and  a.send_id=user.id AND now()<a.deadline and user.type !=" +
            "(select type from user where user.id=#{id}) order by a.time desc  limit #{next},10;")
    List<Order> searchOrder(@Param("keywords") String keywords,@Param("id") int id,@Param("next") int next);

    //查询订单发布者和用户是否相同
   @Select("select id from `order` where send_id=#{uId} AND id=#{oId};")
   int isMyOrder(@Param("uId") int uId,@Param("oId") int oId);

    //相同则返回有hsd_cus
    @Select("select a.id,a.deadline, a.img,a.title,a.price,a.content,a.time,a.had_cus,user.credit,user.real_name from `order` a,user " +
            "where a.send_id=user.id AND user.id=#{uId} AND a.id=#{oId};")
    Order myDetailOrder(@Param("uId") int uId,@Param("oId") int oId);

    //不同则返回没有has_cus
    @Select("select a.id,a.deadline, a.img,a.title,a.price,a.content,a.time,user.credit,user.real_name from `order` a,user " +
            "where user.id=#{uId} AND a.id=#{oId};")
    Order detailOrder(@Param("uId") int uId,@Param("oId") int oId);

    //修改订单
    @Update("update `order` SET title= #{column} WHERE `order`.id=#{id};")
    void updateTitle(@Param("column") String column,@Param("id") int id);
    @Update("update `order` SET img= #{column} WHERE `order`.id=#{id};")
    void updateImg(@Param("column") String column,@Param("id") int id);
    @Update("update `order` SET content= #{column} WHERE `order`.id=#{id};")
    void updateContent(@Param("column") String column,@Param("id") int id);

    @Update("update `order` SET deadline= #{column} WHERE `order`.id=#{id};")
    void updateDead(@Param("column") String column,@Param("id") int id);

    @Update("update `order` SET price= #{column} WHERE `order`.id=#{id};")
    void updatePrice(@Param("column") int column,@Param("id") int id);

    @Update("update `order` SET all_cus= #{column} WHERE `order`.id=#{id};")
    void updateAllCus(@Param("column") int column,@Param("id") int id);
    //筛选出用户发送的订单
    @Select("select a.id, a.img,a.title,a.price,a.content,a.time,user.credit from `order` a,user " +
            "where a.send_id=#{id} and a.send_id=user.id " +
            "and  a.send_id=user.id order by a.time desc limit #{next},10;;")
    List<Order> selectMySend(@Param("id") int id,@Param("next") int next);

    //用户发送订单总数
    @Select("select count(*) from `order` a,user " +
            "where a.send_id=user.id" +
            "and a.send_id=#{id}")
    int allMySend(@Param("id") int id);
}
