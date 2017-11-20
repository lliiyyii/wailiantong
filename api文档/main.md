## 基本配置
- url：http://112.74.169.37:8081
- Content-type:application/json
- charset:UTF-8

## api文件

### 该目录下

链接 | 文件名 | 描述
---- | ----  | ---
#4 | upload  | 上传照片，返回url

### 订单相关

链接 | 文件名 | 描述
---- | ----  | ---
#6  | main_page |首页相关的api，包括所有的筛选
#5  | newOrder | 新建订单 
#7 | search_order | 首页的订单搜索
#8 | detail | 订单的具体内容
#9 | edit | 修改订单
#10 |  mysend | 用户发布订单列表

### 用户相关

链接 | 文件名 | 描述
---- | ----  | ---
#2 | login | 登录
#1 | register |  注册
#15 | cerify | 学生商家认证
#16 | myInfo | 查看个人信息

### 接收订单相关

链接 | 文件名 | 描述
---- | ----  | ---
#11  | newRcv | 接受需求
#12 | viewRcv | 查看接受订单者


### 留言相关

链接 | 文件名 | 描述
---- | ----  | ---
#13  | newMsg | 新建留言
#14 | viewMsg | 查看留言


## 返回json
> 后台错误时

```
{
    "msg": "error",
    "status": 0,
    "data": null
}
```