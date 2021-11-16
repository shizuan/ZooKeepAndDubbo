package com.zuanshi.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DeleteNode {
    public static void main(String[] args) throws Exception {
        /**
         *  RetryPolicy： 失败的重试策略的公共接口
         *  ExponentialBackoffRetry是 公共接口的其中一个实现类
         *      参数1： 初始化sleep的时间，用于计算之后的每次重试的sleep时间
         *      参数2：最大重试次数
         *      参数3（可以省略）：最大sleep时间，如果上述的当前sleep计算出来比这个大，那么sleep用这个时间
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端框架获取连接CuratorFramework.newClient().
        /**
         * 参数1：连接的ip地址和端口号
         * 参数2：会话超时时间，单位毫秒
         * 参数3：连接超时时间，单位毫秒
         * 参数4：失败重试策略
         */
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 1000, retryPolicy);

        //启动客户端
        client.start();

        //删除一个子节点 不能删除有子节点的节点
            //client.delete().forPath("/app");

        //递归删除节点
            //client.delete().deletingChildrenIfNeeded().forPath("/app1");

        //强制保证删除一个节点 只要回话有效那么保证会删除该节点，后台会一直执行删除操作，直到删除成功
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/app1");
        //关闭客户端
        client.close();
    }
}
