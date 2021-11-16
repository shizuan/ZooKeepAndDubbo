package com.zuanshi.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class demo {
    @Test
    public void createNode() throws Exception {
        /**
         *  RetryPolicy： 失败的重试策略的公共接口
         *  ExponentialBackoffRetry是 公共接口的其中一个实现类
         *      参数1： 初始化sleep的时间，用于计算之后的每次重试的sleep时间
         *      参数2：最大重试次数
                参数3（可以省略）：最大sleep时间，如果上述的当前sleep计算出来比这个大，那么sleep用这个时间
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端框架获取连接CuratorFramework.newClient().
        /**
         * 参数1：连接的ip地址和端口号
         * 参数2：会话超时时间，单位毫秒
         * 参数3：连接超时时间，单位毫秒
         * 参数4：失败重试策略
         */
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", 3000, 1000, retryPolicy);

        //启动客户端
        client.start();
        //1. 创建一个空节点(a)（只能创建一层节点）
            //client.create().forPath("/app");
        //2. 创建一个有内容的b节点（只能创建一层节点）
            //client.create().forPath("/app","hello create".getBytes());
        //3. 创建持久节点，同时创建多层节点
            //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/app1/a","app1-a".getBytes());
        //4. 创建带有的序号的持久节点
            //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/app2","app2-a".getBytes());
        //5. 创建临时节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）
            //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/app3","app3".getBytes());
            //Thread.sleep(5000);
        //6. 创建临时带序号节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/app4","app4".getBytes());
            Thread.sleep(5000);
        //关闭客户端
        client.close();
    }
}
