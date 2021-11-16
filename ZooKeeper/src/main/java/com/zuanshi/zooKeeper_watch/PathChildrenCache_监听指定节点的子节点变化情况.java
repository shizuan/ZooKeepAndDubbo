package com.zuanshi.zooKeeper_watch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class PathChildrenCache_监听指定节点的子节点变化情况 {
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

        //变化情况包括 1.新增子节点 2.子节点数据变更 3.子节点删除

        //创建节点数据监听对象 true 能够获取到节点的数据内容  false 无法取到数据内容
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/app", true);

        //启动监听
        /**
         * NORMAL:  普通启动方式, 在启动时缓存子节点数据
         * POST_INITIALIZED_EVENT：在启动时缓存子节点数据，提示初始化
         * BUILD_INITIAL_CACHE: 在启动时什么都不会输出
         *  在官方解释中说是因为这种模式会在start执行执行之前先执行rebuild的方法，而rebuild的方法不会发出任何事件通知。
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        //监听的回调函数
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                //当发生子节点添加后执行该方法：PathChildrenCacheEvent.Type.CHILD_ADDED
                if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    System.out.println("添加了子节点");
                    System.out.println("添加的节点为："+event.getData().getPath());
                    System.out.println("添加的数据为："+new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
                    System.out.println("删除了子节点");
                    System.out.println("删除的节点为："+event.getData().getPath());
                    System.out.println("删除的数据为："+new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
                    System.out.println("修改了子节点");
                    System.out.println("修改的节点为："+event.getData().getPath());
                    System.out.println("修改的数据为："+new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.INITIALIZED){
                    System.out.println("初始化了");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED){
                    System.out.println("连接失效时执行");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED){
                    System.out.println("连接时执行");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_LOST){
                    System.out.println("连接失效后，等一会执行");
                }
            }
        });

        System.in.read();

        client.close();

    }
}
