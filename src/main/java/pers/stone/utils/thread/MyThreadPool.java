package pers.stone.utils.thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * 类名: MyThreadPool <br/>
 * 用途: 线程池实现 <br/>
 * 
 * @author wangbo <br/>
 *         Jul 29, 2016 6:08:43 PM
 */
public class MyThreadPool {

    private List<MyThread> pool;

    private Map<String, Method> modelMap;

    private BlockingQueue<ThreadTask<?>> taskQueue;

    public MyThreadPool(int poolSize, int queueSize) {
        pool = new ArrayList<MyThread>(poolSize);
        modelMap = new HashMap<String, Method>();
        taskQueue = new ArrayBlockingQueue<ThreadTask<?>>(queueSize);
        for (int i = 0; i < poolSize; i++) {
            MyThread thread = new MyThread();
            pool.add(thread);
            thread.start();
        }
    }

    public void shutdown() {
        for (MyThread thread : pool) {
            thread.interrupt();
        }
    }

    public <E> TaskReturn<E> submit(Object instance, String methName, Object... args) throws Exception {
        Class<? extends Object> className = instance.getClass();
        String methodkey = className.getName() + "_" + methName;
        Method method = modelMap.get(methodkey);
        if (method == null) {
            for (Method item : className.getMethods()) {
                if (item.getName().equalsIgnoreCase(methName)) {
                    method = item;
                    modelMap.put(methodkey, method);
                    break;
                }
            }
        }
        if (method == null) {
            throw new Exception("Can not find method [" + methName + "] in class:" + className);
        }

        ThreadTask<E> task = new ThreadTask<E>(instance, method, args);
        taskQueue.put(task);
        return task.res;
    }

    public class TaskReturn<E> {
        volatile boolean finishFlag = false;

        E res = null;

        public E getTaskResult() throws InterruptedException {
            while (!finishFlag) {
                synchronized (this) {
                    this.wait();
                }
            }
            return res;
        }
    }

    private class ThreadTask<E> {

        Object instance;
        Method method;
        Object[] args;
        TaskReturn<E> res;

        public ThreadTask(Object instance, Method method, Object[] args) {
            super();
            this.instance = instance;
            this.method = method;
            this.args = args;
            res = new TaskReturn<E>();
        }

        @SuppressWarnings("unchecked")
        public void fire() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            res.res = (E) method.invoke(instance, args);
            synchronized (res) {
                res.finishFlag = true;
                res.notify();
            }
        }

    }

    private class MyThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    ThreadTask<?> task = taskQueue.take();
                    task.fire();
                } catch (InterruptedException e) {
                    return;
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
