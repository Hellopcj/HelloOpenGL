package com.baidu.pcj.myapplication.threadpool;

import android.support.annotation.NonNull;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池 支持高并发
 * 支持串行和并行以及单线程
 * 线程池的作用： 有效的管理线程
 * Android中的线程池的概念来源于Java中的Executor，Executor是一个接口，真正的线程池的实现为ThreadPoolExecutor ThreadPoolExecutor提供了一系列参数来配置线程池，通过不同的参数可以创建不同的线程池。
 * //////////////////////////////////////////////////
 * 线程池的优点：
 * <p>
 * 线程池的出现，恰恰就是解决上面类似问题的痛点，而线程池的优点有：
 * <p>
 * （1）复用线程池中的线程，避免因为线程的创建和销毁所带来的性能开销。
 * <p>
 * （2）能够有效的控制线程池的最大并发数，避免大量的线程之间因互相抢占系统资源而导致的阻塞现象。
 * <p>
 * （3）能够对线程进行简单的管理，并提供定时执行以及指定间隔循环执行等功能。
 */

public class OpenGlThreadPool {

    private static OpenGlThreadPool mInstance = null;
    /**
     *
     */
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数  最少2个线程，最多4
     */
    private static int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    private static int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 支持所容纳的最大的串行线程个数
     */
    private static int MAX_SERIA_THREAD_SIZE = 3;
    /**
     * 支持所容纳的最大的并行线程个数
     */
    private static int MAX_PARALLEL_THREAD_SIZE = 3;

    /**
     * 串行线程池
     */
    private ThreadPoolExecutor mSerialThreadPool;
    /**
     * 并行线程池
     */
    private ThreadPoolExecutor mParallelThreadPool;

    /**
     * 单线程池
     */
    private ThreadPoolExecutor mThread;
    /**
     * 任务串行
     */
    public static int SERIAL_EXECUTOR = 0;

    /**
     * 任务并行
     */
    public static int PARALLEL_EXCUTOR = 1;

    /**
     * 串行 数据队列 静态阻塞队列
     */
    private Queue<byte[]> mSerialQueue;
    /**
     * 并行数据队列
     */
    private Queue<byte[]> mParellelQueue;

    /**
     * 工作队列
     */
    private BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingDeque<>(10);

    /**
     * 超时的时间单位
     */
    private final TimeUnit unitTime = TimeUnit.SECONDS; // TimeUnit.SECONDS 秒

    /**
     * 非核心线程闲置时的超时时长  单位s
     */
    private final int keepAliveTime = 10;

    /**
     * 线程工厂  为线程池提供创建新线程的功能
     */
    private ThreadFactory mThreadFactory = new ThreadFactory() {
        // 线程安全的Integer 操作类
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, mCount.getAndIncrement() + "");
        }
    };


    private OpenGlThreadPool() {
        // TODO 串行 线程池
        mSerialThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, keepAliveTime, unitTime, mWorkQueue, mThreadFactory, new DiscardOldestPolicy());
        mSerialThreadPool.allowCoreThreadTimeOut(true);

        // TODO 并行 线程池
      //  mParallelThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,)

        mSerialQueue = new LinkedBlockingDeque<>(MAX_SERIA_THREAD_SIZE);
        mParellelQueue = new LinkedBlockingDeque<>(MAX_PARALLEL_THREAD_SIZE);
    }

    public static OpenGlThreadPool getInstance() {
        if (mInstance == null) {
            synchronized (OpenGlThreadPool.class) {
                mInstance = new OpenGlThreadPool();
            }
        }
        return mInstance;
    }


    private void destroyThreadPool(ThreadPoolExecutor poolExecutor, Queue<byte[]> queue) {
        // 参考 shutdown方法
        if (poolExecutor == null) {
            return;
        } else
            try {
                poolExecutor.shutdown();
                queue.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * A handler for rejected tasks that discards the oldest unhandled
     * request and then retries {@code execute}, unless the executor
     * is shut down, in which case the task is discarded.
     */
    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code DiscardOldestPolicy} for the given executor.
         */
        public DiscardOldestPolicy() {
        }

        /**
         * Obtains and ignores the next task that the executor
         * would otherwise execute, if one is immediately available,
         * and then retries execution of task r, unless the executor
         * is shut down, in which case task r is instead discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (e.isShutdown()) {
                return;
            }
            Runnable reject = e.getQueue().poll();
            if (reject != null && reject instanceof OpenGlRunnable) {
                OpenGlRunnable runnable = (OpenGlRunnable) reject;
                runnable.returnAvailableData();
            }
            e.execute(r);
        }
    }
}
