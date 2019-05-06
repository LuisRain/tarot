package com.hy.util;

import java.util.concurrent.*;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.util
 * @Email 1023012015@qq.com
 * @date 2019/3/27 17:21
 */
public class ThreadPoolFactory {
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 私有化工具类构造方法
     * @params: []
     * @return:
     * @Date: 2019/3/27 17:27
     * @Modified By:
    */
    private ThreadPoolFactory() {  }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 生产一个自定义线程数的线程池
     * @params: [poolSize]
     * @return: java.util.concurrent.ExecutorService
     * @Date: 2019/3/27 17:26
     * @Modified By:
     */
    public static ExecutorService getThreadPool(int poolSize){
        return new ThreadPoolExecutor(poolSize, 2*poolSize, 10,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 生产一个线程池
     * @params: []
     * @return: java.util.concurrent.ExecutorService
     * @Date: 2019/3/27 17:24
     * @Modified By:
    */
    public static ExecutorService getThreadPool(){
        return getThreadPool(20);
    }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO生产一个初始线程比较大的线程池
     * @params: []
     * @return: java.util.concurrent.ExecutorService
     * @Date: 2019/3/27 17:25
     * @Modified By:
    */
    public static ExecutorService getMaxThreadPool(){
        return getThreadPool(50);
    }
}
