package com.example.demo.service.serviceImpl;

import com.example.demo.dao.ReceiveDao;
import com.example.demo.entity.Receiver;
import com.example.demo.entity.Usr;
import com.example.demo.service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService{
    @Autowired
    ReceiveDao dao;
    @Override
    public int addRcv(Receiver receiver) {
        try {
            int num = 1;
            num = dao.isNew(receiver);
            if(num==0) {
                dao.newRcv(receiver);
                dao.newRcvS(receiver);
                return 1;
            }
            else return 2;
        }catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Usr> getAllRcv(int id) {
        return dao.allRcpUsr(id);
    }

    @Override
    public List<Usr> getAllRcvUser(int oId) {
        return dao.allUsr(oId);
    }
}
