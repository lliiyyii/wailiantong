## 新建留言

- url:/msg/new
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
order_id | int | 是 |
user_id | int | 是 |
rcv_id | int | 否 | 接收者的id，订单是自己发的时必须发，别人的订单不用发
content | String |是 | 


---

### 示例

- 传入
```
{
"order_id":1,
"user_id":2,
"content":"whwhwh"
}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": null
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