## 新建订单

- url:/order/new
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
title | string | 是 | 需求主题
img_url | String | 是 | 宣传照片地址
price | int | 是 | 价格
content | string | 是 | 订单内容
send_id | id | 是 | 发布者id
all_cus | int | 是 | 需要的接单的人数
deadline | string | 是 | 订单截至时间

- 传出

参数名 | 类型 | 描述
---- | ----  | ----
order_id| int | 订单id

---

### 示例

- 传入
```json
{
"title":"aaa",
"content":"内容",
"price":200,
"send_id":1,
"all_cus":5,
"img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.5544114451740993748.8080\\images\\\\a.JPG"
}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": {
        "order_id": 5
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