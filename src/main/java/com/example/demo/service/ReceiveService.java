package com.example.demo.service;

import com.example.demo.entity.Receiver;
import com.example.demo.entity.Usr;

import java.util.List;

public interface ReceiveService {
    int addRcv(Receiver receiver);
    List<Usr> getAllRcv(int id);//其实是获得发布者的信息，只返回一个
    List<Usr> getAllRcvUser(int oId);
}
