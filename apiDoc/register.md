## 注册接口


- url:/usr/register
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
phone | string | 是 | 用户名/电话号码
pwd | String | 是 | 
code | int | 是 | 验证码 默认8888
type | int |是 | 类型1是商家0是学生

---

### 示例
- 传入
```
{
"phone":"aaa",
"code":9999,
"pwd":"12345",
"type":1
}
```

- 成功传出
```
{
    "msg": "succuss",
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







