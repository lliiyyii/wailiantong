# 后台部分

## 前后端交互部分
- api文档在主目录api文档下
> 主要包括主要的api文档，用户相关的文档，订单相关，接受订单，留言相关的文档
> 后台实现的具体功能也可详见api文档

- 实现功能


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


