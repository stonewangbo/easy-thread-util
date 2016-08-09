
package pers.stone.utils.threadsample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pers.stone.utils.mythread.MyThreadPool;

/**
 * 
 * 类名: ThreadPoolvsThread <br/>
 * 用途: <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 3:26:50 PM
 */
public class ThreadPoolvsThread implements Runnable {

    volatile int finishCount;
    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();
    
    
    int poolSize = Runtime.getRuntime().availableProcessors();

    public void run() {
        // 测试进行一定量的运算
        for (int i = 0; i < 100000; i++) {
            Math.sqrt(i);
            Math.cos(i);
        }
        finishCount++;
        lock.lock();
        try {
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    private void runWithSingleThread(int count) throws InterruptedException {
        long start = System.currentTimeMillis();
        finishCount = 0;
        for (int i = 0; i < count; i++) {
            run();
        }
        System.out.println(
                "runWithSingleThread:" + count + " times, cost:" + (System.currentTimeMillis() - start) + "ms");
    }

    private void runWithThread(int count) throws InterruptedException {
        long start = System.currentTimeMillis();
        finishCount = 0;
        for (int i = 0; i < count; i++) {
            new Thread(this).start();
        }
        waitForFinish(count);
        System.out.println("runWithThread:" + count + " times, cost:" + (System.currentTimeMillis() - start) + "ms");
    }

    private void runWithThreadPool(int count) throws InterruptedException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(poolSize, poolSize, 1000L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(1000));
        threadPool.prestartAllCoreThreads();
        long start = System.currentTimeMillis();
        finishCount = 0;
        for (int i = 0; i < count; i++) {
            threadPool.submit(this);
        }
        waitForFinish(count);
        System.out
                .println("runWithThreadPool:" + count + " times, cost:" + (System.currentTimeMillis() - start) + "ms");
        threadPool.shutdown();
    }

    private void runWithMyThreadPool(int count) throws Exception {
        MyThreadPool threadPool = new MyThreadPool(poolSize, 1000);
        long start = System.currentTimeMillis();
        finishCount = 0;
        for (int i = 0; i < count; i++) {
            threadPool.submit(this);
        }
        waitForFinish(count);
        System.out.println(
                "runWithMyThreadPool:" + count + " times, cost:" + (System.currentTimeMillis() - start) + "ms");
        threadPool.shutdown();
    }

    private void runWithMyThreadPoolReflect(int count) throws Exception {
        MyThreadPool threadPool = new MyThreadPool(poolSize, 1000);
        long start = System.currentTimeMillis();
        finishCount = 0;
        for (int i = 0; i < count; i++) {
            threadPool.submit(this, "run");
        }
        waitForFinish(count);
        System.out.println("runWithMyThreadPoolReflect:" + count + " times, cost:"
                + (System.currentTimeMillis() - start) + "ms");
        threadPool.shutdown();
    }

    private void waitForFinish(int count) throws InterruptedException {
        while (count > finishCount) {
            lock.lock();
            try {
                cond.await();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolvsThread threadPoolvsThread = new ThreadPoolvsThread();
        threadPoolvsThread.runWithSingleThread(100);
        threadPoolvsThread.runWithThread(100);
        threadPoolvsThread.runWithThreadPool(100);
        threadPoolvsThread.runWithMyThreadPool(100);
        threadPoolvsThread.runWithMyThreadPoolReflect(100);
    }

   
   
}
