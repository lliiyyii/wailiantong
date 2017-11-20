## 查看接受者（发订单的人）

- url:/rcv/view
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
order_id | int | 是 |

- 传出

参数名 | 类型 | 描述
---- | ----  | ----
credit | int  | 接收者信用
phone | String | 
rcv_cus_id | int  | 接收者
name| string | 接收者真名

---

### 示例

- 传入
```
{
"order_id":1
}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": [
        {
            "phone": "aa",
            "name": "88",
            "credit": 80,
            "rcv_cus_id": 1
        },
        {
            "phone": "aaa",
            "name": "hng",
            "credit": 80,
            "rcv_cus_id": 3
        },
        {
            "phone": "15645685698",
            "name": "55",
            "credit": 80,
            "rcv_cus_id": 6
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