package com.example.demo.service.serviceImpl;

import com.example.demo.dao.MsgDao;
import com.example.demo.entity.Msg;
import com.example.demo.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgServiceImpl implements MsgService{
    @Autowired
    MsgDao msgDao;
    @Override
    public int newMsgMy(Msg msg) {
        try{
            msgDao.newMsgToMy(msg);
            return 1;
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public int newMsg(Msg msg) {
        try{
            msgDao.newMsg(msg);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public List<Msg> viewMgMsg(Msg msg) {
        return msgDao.viewMyMsg(msg);
    }

    @Override
    public List<Msg> viewMsg(Msg msg) {
        return msgDao.viewMsg(msg);
    }
}
