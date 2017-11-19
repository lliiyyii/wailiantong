package com.example.demo.dao;

import com.example.demo.entity.Com;
import com.example.demo.entity.Usr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    @Select("select count(*) from user where name=#{name};")
    int exitName(@Param("name") String name);

    @Insert("insert user(name,pwd,type) values(#{usr.name},#{usr.pwd},#{usr.type});")
    void insertUser(@Param("usr") Usr usr);

    @Insert("insert into com (com_id) SELECT max(id) from user where type=1;")
    void insertCom();

    @Insert("insert into stu (stu_id) SELECT max(id) from user where type=0;")
    void insertStu();

    @Select("select id, name,pwd,type from user where name = #{usr.name} and #{usr.pwd};")
    Usr login(@Param("usr") Usr usr);

    @Update("UPDATE user,stu SET real_name=#{usr.real_name},identity_img=#{usr.identity},\n" +
            "certifycation=1 " +
            "WHERE user.id=#{usr.id} AND user.id=stu_id;")
    void certifyStu(@Param("usr") Usr usr);

    @Update("UPDATE user,com SET real_name=#{usr.real_name},\n" +
            "  identity_img=#{usr.identity},business_img=#{usr.business},\n" +
            " certifycation=1 ,head = #{usr.head} \n" +
            "WHERE user.id=#{usr.id} AND user.id=com_id;")
    void certifyCom(@Param("usr") Usr usr);

    @Select("SELECT type FROM user WHERE id=#{id};")
    int type (@Param("id") int id);

    @Select("SELECT user.id,user.name,user.type,user.credit,user.real_name,com.head,\n" +
            "  com.business_img,com.identity_img\n" +
            "FROM user,com\n" +
            "WHERE user.id=#{id} AND user.id=com.com_id;")
    Usr comInfo (@Param("id") int id);

    @Select("SELECT user.id,user.name,user.type,user.credit,user.real_name,\n" +
            "  stu.identity_img\n" +
            "FROM user,stu\n" +
            "WHERE user.id=#{id} AND user.id=stu.stu_id;")
    Usr stuInfo (@Param("id") int id);
}
