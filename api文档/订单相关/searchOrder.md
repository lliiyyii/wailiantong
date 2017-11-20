## 搜索订单
> 首页搜索订单，根据标题搜索

- url:/order/search
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
user_id | int | 是 | 用户名id
keywords | String | 是 | 搜索关键字
next | int/String | 否 | 默认0，后需返回收到的值

- 传出

参数名 | 类型 | 描述
---- | ----  | ----
next| int  | 下一次请求标识，没有数据时返回"next"=""
list | 数组 | 里面每一个对象里的参数如下
order_id | int  | 订单id
img_url | string  | 图片u'r'l
title | String | 订单标题
content | int  | 订单内容，少于等于20字
price | int  | 订单价格
credit | String | 发布者信用

---

### 示例

- 传入

```
{
"user_id":2,
"keywords":"a"

}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": {
        "next": "",
        "list": [
            {
                "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG",
                "price": 400,
                "time": 1510320532000,
                "title": "aaa",
                "credit": 80,
                "order_id": 5,
                "content": "内容"
            },
            {
                "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG",
                "price": 300,
                "time": 1510320518000,
                "title": "aaa",
                "credit": 80,
                "order_id": 4,
                "content": "内容"
            },
            {
                "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG",
                "price": 20,
                "time": 1510320349000,
                "title": "aaa",
                "credit": 80,
                "order_id": 3,
                "content": "内容"
            }
        ]
    }
}
```