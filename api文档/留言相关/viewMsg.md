## 查看留言

- url:/msg/view
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
order_id | int | 是 |
user_id | int | 是 |
rcv_id | int | 否 | 接收者的id，订单是自己发的时必须发，别人的订单不用发


---

### 示例

- 传入
```
{
"order_id":1,
"user_id":2,
"rcv_id":1
}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": [
        {
            "name": "88",
            "time": "2017-11-13 20:29",
            "content": "whwhwh"
        },
        {
            "name": "hnhj",
            "time": "2017-11-13 20:29",
            "content": "whwhwh"
        },
        {
            "name": "88",
            "time": "2017-11-13 20:28",
            "content": "whwhwh"
        },
        {
            "name": "hnhj",
            "time": "2017-11-13 20:28",
            "content": "whwhwh"
        }
    ]
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