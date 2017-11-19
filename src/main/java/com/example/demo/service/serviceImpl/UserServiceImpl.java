package com.example.demo.service.serviceImpl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.Com;
import com.example.demo.entity.Usr;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public int register(Usr usr) {
        String name = usr.getName();
        if (userDao.exitName(name) == 0) {
            log.info("开始插入用户");
            userDao.insertUser(usr);
            int type = usr.getType();
            if (type == 1) {
                log.info("插入商家");
                userDao.insertCom();
            } else {
                log.info("插入学生");
                userDao.insertStu();
            }
            return 1;
        } else {
            log.info("用户名已存在");
            return 0;
        }


    }

    @Override
    public Usr login(Usr usr) {
        return userDao.login(usr);

    }

    @Override
    public void cerifyStu(Usr usr) {
        userDao.certifyStu(usr);
    }

    @Override
    public void cerifyCom(Usr usr) {
        userDao.certifyCom(usr);
    }

    @Override
    public Usr info(int id) {
        int type = 2;
        type = userDao.type(id);
        if (type == 0) {
            return userDao.stuInfo(id);
        } else if (type == 1) {
            return userDao.comInfo(id);
        } else return null;
    }


}
