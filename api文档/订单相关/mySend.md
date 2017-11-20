## 用户全部发出的订单

>首页的加载，六个筛选

- url:/order/mySend
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
user_id| int| 是 | 用户名/电话号码
next | string/int | 否 |  第一次请求可为空或0，后面请求的next必须为返回的next值

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
"user_id":2

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
                "price": 200,
                "time": "2017-11-10",
                "title": "ddd",
                "credit": 80,
                "order_id": 2,
                "content": "hhh"
            },
            {
                "img_url": "c:\\aTt.jpg",
                "price": 3,
                "time": "2017-11-10",
                "title": "cccc",
                "credit": 80,
                "order_id": 1,
                "content": "iiiiiiiiiiiiiiiiiii"
            }
        ]
    }
}
```

- 失败传出
```
{
    "msg": "error",
    "status": 0,
    "data": null
}
```