package com.example.demo.service;

import com.example.demo.entity.Com;
import com.example.demo.entity.Usr;

import java.util.List;
import java.util.Map;

public interface UserService {
    int register(Usr usr);
    Usr login(Usr usr);
    void cerifyStu(Usr usr);
    void cerifyCom(Usr usr);
    Usr info(int id);
}
