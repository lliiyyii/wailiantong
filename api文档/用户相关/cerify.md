## 认证学生和商户

### 认证学生
- url : /certify/stu
- method:post
- 传入参数

参数名 | 类型 | 是否必须 | 描述
----|----| ----| ----
user_id | int | 是 |
realname | String  | 是 | 身份证上的名字
identity | STring |  是 | 身份证地址 | 

---
#### 示例
- 传入
```
{
"realname":"王",
"user_id":4,
"identity":"c\\s\\s\\i.img"
}
```

- 传出 
```
{
    "msg": "success",
    "status": 1,
    "data": null
}
```


### 认证商家
- url : /certify/com
- method:post
- 传入参数

参数名 | 类型 | 是否必须 | 描述
----|----| ----| ----
user_id | int | 是 |
realname | String  | 是 | 身份证上的名字
identity | STring |  是 | 身份证地址 | 
head | String  | 是 | 营业执照的法人？
business| STring |  是 | 营业执照地址 | 

---
#### 示例
- 传入
```
{
"realname":"王",
"user_id":6,
"identity":"c\\s\\s\\i.img",
"business":"c\\s\\s\\ee.img",
"head":"李头"
}
```

- 传出 
```
{
    "msg": "success",
    "status": 1,
    "data": null
}
```