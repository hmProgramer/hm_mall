1 搭建项目框架



    包含hm-registry（注册中心）
    包含hm-gateway（网关路由）
    包含hm-common（公共组件）
    包含hm-item（商品品类微服务item框架）

2 创建通用异常处理；
  
    1)在hm-common包里创建CommonHandleException类 加上@ControllerAdivice注解(作用：定义对各种异常的拦截处理)
    2）如何定义各种异常？通过自定义异常HmException类 ，和枚举类ExceptionEnums来定义异常的状态码和错误信息（作用：可以灵活的定义多种异常对应的状态码，统一了错误信息，避免了由于个体差异导致的返回的错误信息不一致等情况）
    3）通过定义ExceptionResult返回错误结果集 （作用：通过异常对象返回了更丰富的返回结果，包含状态码，错误信息，时间戳）
    

3github使用指南
     
     1)合作开发时先通过 项目--》git---》repository---》pull（拉一下）
     2）在点击项目右上角的commit图标（会自动更新出变动的文件）
     3）点击commit 中的commit and push 
     
     
4insert与insertSelectiv之间的区别 
        比如User里面有三个字段:id name age password
        User u=new user();
        //u.setName("bill");
        //mapper.insertSelective(u);
        // 
        //insertSelective执行对应的sql语句的时候，只插入对应的name字段
        //sql语句如下：
        //insert into tb_user (name) value ("bill")
        //insert则是每个字段都要添加一遍
        //insert into tb_user (id,name,age,password) value(null,"bill",null,null);
