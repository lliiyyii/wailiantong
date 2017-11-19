package com.example.demo.service;

import com.example.demo.entity.Msg;

import java.util.List;

public interface MsgService {
    int newMsgMy(Msg msg);
    int newMsg(Msg msg);
    List<Msg> viewMgMsg(Msg msg);
    List<Msg> viewMsg(Msg msg);
}
