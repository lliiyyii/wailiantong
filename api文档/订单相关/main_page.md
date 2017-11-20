## 首页api

>首页的加载，六个筛选

- url:/order/main
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
user_id| int| 是 | 用户id
next | string/int | 否 |  第一次请求为0，后面请求的next必须为返回的next值
type | int | 是 | 默认为5。1：信用从高到低；2：信用从低到高；3：价格从高到低；4：价格从低到高；5：时间从近到远；6：结束时间从近到近

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
"type":3

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

- 失败传出
```
{
    "msg": "error",
    "status": 0,
    "data": null
}
```