package com.zuanshi.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;
/*
*   继承PropertyPlaceholderConfigurer类，重写processProperties方法
*   spring的<context:property-placeholder location="classpath:db.properties"/>标签  就是执行了该方法，并注入
*   我们重写该方法，将我们的类注入到spring容器中
*   那么spring容器就会对我们的类进行管理，会使用我们增强的类去加载配置
*       最后实现将配置文件使用zookeeper的方式进行配置
*   zookeeper的监听中，需要使用到当前的spring的核心容器对象，怎么获取？
*       实现spring的ApplicationContextAware接口，重写setApplicationContext方法，获取对象
*   获取到后，怎么让spring容器进行重新加载？
*       使用AbstractApplicationContext这个父类，这个父类被注解的核心容器创建方式等继承
*       使用该类的refresh() 方法，让sprng的核心容器进行重新加载
* */

public class PropertyUtil extends PropertyPlaceholderConfigurer implements ApplicationContextAware {

    private AbstractApplicationContext applicationContext;
    private static Boolean flag = true;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        //获取zookeeper配置中心的配置放入props中
        zkload(props);
        //调用zookeeper的watch机制
        if (flag) {
            zkwatch();
            flag = false;
        }
        super.processProperties(beanFactoryToProcess, props);
    }

    //监听zookeeper的节点，实施回调方法
    private void zkwatch() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 2);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 1000, retryPolicy);
        client.start();
        TreeCache treeCache = new TreeCache(client,"/config");
        try {
            treeCache.start();
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                    //当数据修改时回调该方法
                    if(treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                        //对spring核心容器进行重新加载
                        applicationContext.refresh();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取zookeeper中的配置信息 存入props
    private void zkload(Properties props) {
        //创建zookeeper客户端获取信息，存入properties
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 2);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 1000, retryPolicy);
        client.start();
        try {
            String url = new String(client.getData().forPath("/config/jdbc.url"));
            String driverClassName = new String(client.getData().forPath("/config/jdbc.driverClassName"));
            String username = new String(client.getData().forPath("/config/jdbc.username"));
            String password = new String(client.getData().forPath("/config/jdbc.password"));
            props.setProperty("jdbc.url",url);
            props.setProperty("jdbc.driverClassName",driverClassName);
            props.setProperty("jdbc.username",username);
            props.setProperty("jdbc.password",password);
        } catch (Exception e) {
            e.printStackTrace();
            client.create();
            throw new RuntimeException("获取zookepper数据时出现异常");
        }
        client.close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取当前的spirng核心容器  需要实现ApplicationContextAware接口从写该方法
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }
}
