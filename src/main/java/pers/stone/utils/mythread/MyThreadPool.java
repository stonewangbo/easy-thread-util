package pers.stone.utils.mythread;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

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
            MyThread thread = new MyThread(taskQueue);
            pool.add(thread);
            thread.start();
        }
    }

    public void shutdown() {
        for (MyThread thread : pool) {
            thread.interrupt();
        }
    }
    
    public <E> TaskReturn<E> submit(Callable<E> callable) throws Exception {
        ThreadTask<E> task = new CallableThreadTask<E>(callable);
        taskQueue.put(task);
        return task.getReturn();
    }
    
    public <E> TaskReturn<E> submit(Runnable runnable) throws Exception {
        ThreadTask<E> task = new CallableThreadTask<E>(runnable);
        taskQueue.put(task);
        return task.getReturn();
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

        ThreadTask<E> task = new ReflectThreadTask<E>(instance, method, args);
        taskQueue.put(task);
        return task.getReturn();
    }   

  
    

}
