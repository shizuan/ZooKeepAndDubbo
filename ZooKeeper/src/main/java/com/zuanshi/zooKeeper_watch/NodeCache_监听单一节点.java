package com.zuanshi.zooKeeper_watch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class NodeCache_监听单一节点 {
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
        System.out.println("客户端启动了");

        //创建节点数据监听对象
        NodeCache nodeCache = new NodeCache(client, "/app");

        //启动监听  参数为true：可以直接获取监听的节点 参数为false：不可以获取监听的节点
        nodeCache.start(true);

        //获取节点数据
        System.out.println(nodeCache.getCurrentData());

        //添加监听对象
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            //监听后的回调函数
            @Override
            public void nodeChanged() throws Exception {
                //获取节点数据
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("节点路径为："+nodeCache.getCurrentData().getPath());
                System.out.println("节点数据为："+data);
            }
        });

        //读取键盘数据，用于将当前线程卡住
        System.in.read();

        //关闭客户端
        client.close();

    }
}
