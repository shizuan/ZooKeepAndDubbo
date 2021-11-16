package com.zuanshi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ProviderApplication {
    public static void main(String[] args) throws IOException {
        //加载了配置文件后，就相当于把服务注册到了zookeeper
        //所以启动服务提供者，有三种方式
        /*
        * 使用spring的核心容器对象
        *
        * 使用servlet的监听器
        *
        *       <context-param>
        *           <param-name>contextConfigLocation</param-name>
        *           <param-value>classpath:spring-dubbo.xml</param-value>
        *       </context-param>
        *       <listener>
        *           <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        *       </listener>
        * 
        * 使用springmvc的核心控制器DispatcherServlet   将spring-dubbo.xml配置文件加载
        *
        *
        * */
        new ClassPathXmlApplicationContext("classpath:spring-dubbo.xml");
        System.in.read();
    }
}
