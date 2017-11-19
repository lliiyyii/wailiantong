## 登录接口


- url:/usr/login
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
phone | string | 是 | 用户名/电话号码
pwd | String | 是 | 
code | int | 是 | 验证码 默认8888


---

### 示例
- 传出

参数名 | 类型 | 描述
---- | ----  | ----
user_id | int  | 用户id
phone | String | 
type | int  | 类型：0为学生，1为商家

```
{
"phone":"aaa",
"code":9999,
"pwd":"12345",
}
```

- 成功传出
```
{
    "msg": "succuss",
    "status": 1,
     "data": {
            "user_id": 3,
            "phone": "15645795698",
            "type": 0
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







