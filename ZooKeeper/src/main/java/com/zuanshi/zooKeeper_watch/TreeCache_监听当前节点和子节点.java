package com.zuanshi.zooKeeper_watch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class TreeCache_监听当前节点和子节点 {
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

        //创建监听节点对象
        TreeCache treeCache = new TreeCache(client,"/app");

        //启动监听
        treeCache.start();

        //获取节点数据
        System.out.println(treeCache.getCurrentData("/app"));

        //创建监听回调方法
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if(event.getType() == TreeCacheEvent.Type.NODE_ADDED){
                    System.out.println(event.getData().getPath() + "节点添加");
                }else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED){
                    System.out.println(event.getData().getPath() + "节点移除");
                }else if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                    System.out.println(event.getData().getPath() + "节点修改");
                }else if(event.getType() == TreeCacheEvent.Type.INITIALIZED){
                    System.out.println("初始化完成");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_SUSPENDED){
                    System.out.println("连接过时");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_RECONNECTED){
                    System.out.println("重新连接");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_LOST){
                    System.out.println("连接过时一段时间");
                }
            }
        });
        System.in.read();

    }
}
