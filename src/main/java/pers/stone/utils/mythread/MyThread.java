package pers.stone.utils.mythread;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * 类名: MyThread <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 5:30:33 PM
 */
public class MyThread extends Thread {
    
    private BlockingQueue<ThreadTask<?>> taskQueue;

    /**
     * @param taskQueue
     */
    public MyThread(BlockingQueue<ThreadTask<?>> taskQueue) {
        super();
        this.taskQueue = taskQueue;
    }

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