## 订单详情

> 点击订单item时的api

- url:/order/detail
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
user_id| int| 是 | 
order_id| int | 是 | 


- 传出

参数名 | 类型 | 描述
---- | ----  | ----
next| int  | 下一次请求标识，没有数据时返回"next"=""
order_id | int  | 订单id
img_url | string  | 图片u'r'l
title | String | 订单标题
content | int  | 订单内容，少于等于20字
price | int  | 订单价格
credit | String | 发布者信用
has_cus | int | 如果是本人发的就会有
deadline | string | 截至时间

- ***自己发的status=2 别人大的status=1***

---

### 示例

- 传入
```
{
"user_id":1,
"order_id":2
}
```

- 自己的成功传出
```
{
    "msg": "myOrder",
    "status": 2,
    "data": {
        "had_cus": 0,
        "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG",
        "price": 20,
        "time": "2017-11-10",
        "title": "aaa",
        "credit": 80,
        "order_id": 3,
        "content": "内容"
    }
}
```

- 别人的成功传出
```
{
    "msg": "othersOrder",
    "status": 1,
    "data": {
        "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG",
        "price": 200,
        "time": "2017-11-10",
        "title": "b",
        "credit": 80,
        "order_id": 2,
        "content": "hhh"
    }
}
```