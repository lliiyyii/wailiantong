## 接受需求


- url:/rcv/new
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
order_id | int | 是 | 
user_id | int | 是 | 

- 传出

参数名 | 类型 | 描述
---- | ----  | ----
realName | String | 用户认证名
phone | String |  用户名/号码

-  ***status***
 > 1 : 接收成功
> 2 ：用户已预定订单
> 0 : 失败

---

### 示例

- 传入

```
{
"order_id":2,
"user_id":1
}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": {
        "realName": "hng",
        "phone": "aaa"
    }
}
```

- 已预订

```
{
    "msg": "已预定",
    "status": 2,
    "data": null
}
```