package com.zuanshi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class CreateJDBCPath {
    @Test
    public void CreateJDBCPath() throws Exception {
        //创建zookeeper的失败重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //获取zookeeper客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",3000,3000,retryPolicy);
        //启动客户端
        client.start();
        //创建jdbc节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/config/jdbc.url","jdbc:mysql:///day20_jdbc?characterEncoding=utf8".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/config/jdbc.driverClassName","com.mysql.jdbc.Driver".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/config/jdbc.username","root".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/config/jdbc.password","root".getBytes());
        //关闭客户端
        client.close();
    }
}
