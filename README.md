# 后台部分


## 已实现功能列表

### 订单相关

 文件名 | 实现功能
 ----  | ---
 main_page |首页的所有功能，其中学生只能看到商家的订单，商家只能看到学生的订单
  main_page |按时间从近到远排序所有订单
  main_page |按结束时间从近到远排序所有订单
  main_page |按信用从高到低排序所有订单
  main_page |按信用从低到高排序所有订单
  main_page |按价格从高到低排序所有订单
  main_page |按价格从低到高排序所有订单
  main_page |点击首页刷新出订单列表
 newOrder | 新建订单 
 search_order | 首页的订单搜索
detail | 订单的具体内容的展示
edit | 修改订单内容，价格等
 mysend | 用户发布订单列表
 myAllOrder | 用户相关的所有订单
 myReceive | 用户接收的所有订单

### 用户相关

 文件名 | 描述
----  | ---
login | 学生商家登录
register |  学生商家注册
cerify | 学生认证
cerify | 商家认证
myInfo | 查看个人信息

### 接收订单相关

 文件名 | 描述
 ----  | ---
 newRcv | 接受需求
 viewRcv | 查看接受订单者
 
 ### 其他
 
  文件名 | 实现功能
 ----  | ---
 upload  | 将图片上传到服务器


### 留言相关

 文件名 | 描述
 ----  | ---
 newMsg | 新建留言
 viewMsg | 查看留言


## 前后端交互部分
- api文档在主目录api文档下
> 主要包括主要的api文档，用户相关的文档，订单相关，接受订单，留言相关的文档
> 后台实现的具体功能也可详见api文档


## 后台开发部分

- 主要技术
> 使用java开发，spring boot 结合mybatis框架搭建
> 前后端使用json传数据，使用fastjson辅助解析json数据

- spring boot+mybatis
 根据spring boot 和 mybatis设计架构和项目需要，将后台分为启动入口：Application,控制器：Controller,服务层：服务层接口service+服务接口实现类serviceImpl,数据库操作层:dao,实体层:entity,工具层：util等多层分层搭建后台架构。
> application是整个后台启动的入口，由于需要上传图片到服务器，废弃spring boot自带的tomcat，使用外置tomcat容器放置后台程序，其中还包含对接口编码的限制

> Cotroller控制器，接收前端请求并交给service处理

> service服务层，负责具体的服务处理

> dao数据操作层，负责与mysql的处理

> entity实体层，包含所需要的实体类型


