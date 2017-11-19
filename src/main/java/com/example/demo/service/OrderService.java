package com.example.demo.service;

import com.example.demo.entity.Order;

import java.util.List;

public interface OrderService {
    int addOrder(Order order);
    //获取全部表单
    List<Order> getAllOrder(int id,int next);
    int getAllOrderNum(int id);
    List<Order> getCreditHign(int id,int next);
    List<Order> getCreditLow(int id,int next);
    List<Order> getPriceHign(int id,int next);
    List<Order> getPriceLow(int id,int next);
    List<Order> getTimeFar(int id,int next);
    List<Order> getSearchOrder(String keywords,int id,int next);
    Order getDetailOrder(int uId,int oId);
    Order getMyDetailOrder(int uId,int oId);
    int isMy(int uId,int oId);
    int editItem(String column,int id,String colName);
    int editIntItem(int column,int id,String colName);
    List<Order> getMySend(int id,int next);
    int getAllMySend(int id);
}
