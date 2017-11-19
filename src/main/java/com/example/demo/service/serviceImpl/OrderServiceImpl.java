package com.example.demo.service.serviceImpl;

import com.example.demo.controller.OrderController;
import com.example.demo.dao.OrderDao;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public int addOrder(Order order) {
        try {
            orderDao.insertOrder(order);
            return orderDao.selectOrderId();
        }catch (Exception e) {

            return 0;
        }
    }

    @Override
    public List<Order> getAllOrder(int id,int next) {
        return orderDao.allOrder(id,next);
    }

    @Override
    public int getAllOrderNum(int id) {
        return orderDao.allOrderNum(id);
    }

    @Override
    public List<Order> getCreditHign(int id, int next) {
        return orderDao.creditHignOrder(id,next);
    }

    @Override
    public List<Order> getCreditLow(int id, int next) {
        return orderDao.creditLowOrder(id,next);
    }

    @Override
    public List<Order> getPriceHign(int id, int next) {
        return orderDao.priceHignOrder(id,next);
    }

    @Override
    public List<Order> getPriceLow(int id, int next) {
        return orderDao.priceLowOrder(id, next);
    }

    @Override
    public List<Order> getTimeFar(int id, int next) {
        return orderDao.timeFarOrder(id, next);
    }

    @Override
    public List<Order> getSearchOrder(String keywords, int id, int next) {
//        keywords = "'%"+keywords+"%'";
        return orderDao.searchOrder(keywords,id,next);
    }

    @Override
    public Order getDetailOrder(int uId, int oId) {
            return orderDao.detailOrder(uId, oId);
    }

    @Override
    public Order getMyDetailOrder(int uId, int oId) {
        return orderDao.myDetailOrder(uId, oId);
    }

    @Override
    public int isMy(int uId, int oId) {
        try {
            log.info("返回值是 "+orderDao.isMyOrder(uId, oId));
            return orderDao.isMyOrder(uId, oId);
        }
        catch (Exception e){
            log.info("进入catch");
            return 0;
        }
    }

    @Override
    public int editItem(String column, int id, String colName) {
        try {
            if (colName.equals("title"))
                orderDao.updateTitle(column, id);
            if (colName.equals("img_url"))
                orderDao.updateImg(column, id);
            if (colName.equals("content"))
                orderDao.updateContent(column, id);
            if (colName.equals("deadline"))
                orderDao.updateDead(column, id);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int editIntItem(int column, int id, String colName) {
        try {
            if (colName.equals("price"))
                orderDao.updatePrice(column,id);
            if(colName.equals("all_cus"))
                orderDao.updateAllCus(column,id);
            return 1;
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public List<Order> getMySend(int id,int next) {
        return orderDao.selectMySend(id,next);
    }

    @Override
    public int getAllMySend(int id) {
        return orderDao.allMySend(id);
    }
}
