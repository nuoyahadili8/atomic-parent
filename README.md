## 平台简介
一套基于Oozie调度引擎开发的大数据任务可视化编辑、运行与监控的调度管理平台，同时扩展了Oozie Action组件(支持更多丰富的action操作，如：jdbc、impala、elasticsearch、数据清洗、压缩、解压等等)。平台具备完善的权限管理和数据权限。


## 技术栈
1. SpringBoot框架
2. Shiro安全控制
3. Thymeleaf模板
4. Bootstrap 3.3.6前端框架
5. Dubbo分布式
6. Quartz定时服务
7. Hadoop（Hdfs、Yarn、Oozie、Hive、Spark、MapReduce、Sqoop ... ）
8. Kerberos大数据安全认证


## 内置功能
1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 策略通道：在线（添加、修改、删除)调度策略的执行计划。
13. 代码生成：前后端代码的生成（java、html、xml、sql)支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
18. 集群注册：根据具体的Hadoop集群环境配置相应参数。比如：Hdfs、HA、Yarn、Oozie等必要参数。
19. 租户注册：对指定集群环境配置租户信息。
20. 项目管理：针对大数据平台实施具体项目工程，进行作业信息隔离的凭证。
21. 项目包：开发具体作业所使用的普通包、模板包等，完成对oozie workflow的可视化操作。
22. 作业维护：具体运行的作业任务，支持对任务的定时、依赖等策略的设置。对应于oozie的coordinator。
23. 作业日志：对任务的可视化监控和日志查看。

## 体验
> admin/admin123  
> 

演示地址：

文档地址：

## 演示图
![](doc/img/login.jpg)
![](doc/img/main.jpg)
![](doc/img/user.jpg)
![](doc/img/menu.jpg)
![](doc/img/group.jpg)
![](doc/img/dept.jpg)
![](doc/img/dict.jpg)
![](doc/img/task.jpg)
![](doc/img/tenant.jpg)
![](doc/img/platform.jpg)
![](doc/img/package.jpg)
![](doc/img/design.jpg)
![](doc/img/action.jpg)
![](doc/img/waizer.jpg)
![](doc/img/project.jpg)
![](doc/img/role.jpg)
![](doc/img/log-table.jpg)
![](doc/img/view-log.jpg)

## Atomic交流群
QQ：683706330

## 开源不易
| 微信 | 支付宝 |
| ---- | ---- |
|![weixin](doc/img/weixin.jpg) | ![](doc/img/zhifubao.jpg)|


