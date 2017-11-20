## 修改订单内容

- url: /order/edit
- method:post
- ***Content-type:form-data***
- ***body***

参数名 | 类型 | 是否必须 | 描述
---- | ----  | ---- | ----
order_id| Text | 是 | 订单id
title | Text | 否 |
img_url | Text | 否 |
content | Text | 否 |
order_id | Text | 否 |
deadline | Text | 否 |

 - status
0 : 出错
1 ： 没有修改
n(n>1) : 修改了n-1个数据

### 示例

> 传入

```
order_id:1
price:3
all_cus:100
content:iiiiiiiiiiiiiiiiiii
```

> 成功传出

```
{
    "msg": "success  content,price,all_cus,",
    "status": 4,
    "data": null
}
```