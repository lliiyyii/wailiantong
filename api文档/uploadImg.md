## 上传图片

- url:/upload/img
- method:post
- Content-type:form-data
- __body__
参数名 | 类型 | 是否必须 | 描述
---- | ---- | ---- |----
img| file| 是 | 图片

- 传出

参数名 | 类型 | 描述
---- | ----  | ----
img_url| String| 图片地址

---

### 示例

- 成功传出
```
{
    "msg": "ok",
    "status": 1,
    "data": {
        "img_url": "C:\\Users\\yi\\AppData\\Local\\Temp\\tomcat-docbase.8385683536260301439.8080\\images\\\\th.jpg"
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