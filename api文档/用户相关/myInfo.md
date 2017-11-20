## 查看个人信息

>商家和学生的不同

- url:/usr/info
- method:post
- __body__

参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
user_id| int| 是 | 用户id


- 传出
> 学生用户

参数名 | 类型 | 描述
---- | ----  | ----
phone| string  | 用户电话号码
identity | string | 身份证照片地址
certify | int  | 认证标志
real_name | string  | 真名
type | int | 学生还是商家，学生是0，商家是1
credit | int  | 信用评分


> 商家用户

参数名 | 类型 | 描述
---- | ----  | ----
phone| string  | 用户电话号码
identity | string | 身份证照片地址
certify | int  | 认证标志
real_name | string  | 真名
type | int | 学生还是商家，学生是0，商家是1
credit | int  | 信用评分
business | string | 营业执照图片地址
head | String | 营业执照法人 

***学生也会有business和head只是都为“”***


---

### 学生示例

- 传入

```
{
"user_id":6

}
```

- 成功传出
```
{
    "msg": "success",
    "status": 1,
    "data": {
        "phone": "13756894562",
        "identity": "/tmp/tomcat-docbase.2845662099353871511.8081/images//default.jpg",
        "certify": 0,
        "real_name": "张梦瑶",
        "type": 0,
        "credit": 80
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


### 商家示例

- 传入

```
{
"user_id":1
}
```

- 传出

```
{
    "msg": "success",
    "status": 1,
    "data": {
        "head": "张军",
        "business": "/tmp/tomcat-docbase.2845662099353871511.8081/images//default.jpg",
        "phone": "18753685479",
        "identity": "/tmp/tomcat-docbase.2845662099353871511.8081/images//default.jpg",
        "certify": 0,
        "real_name": "小郡肝串串",
        "type": 1,
        "credit": 80
    }
}
```