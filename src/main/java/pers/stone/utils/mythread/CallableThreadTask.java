
package pers.stone.utils.mythread;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * 
 * 类名: CallableThreadTask <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 5:13:12 PM
 */
public class CallableThreadTask<E> implements ThreadTask<E> {  
    Callable<E> callable;    
    Runnable runnable;    
    TaskReturn<E> res;
    
    
 
    /**
     * @param instance
     * @param callable
     */
    public CallableThreadTask(Callable<E> callable) {
        super();       
        this.callable = callable;
        res = new TaskReturn<E>();
    }
    

    public CallableThreadTask(Runnable runnable) {
        super();       
        this.runnable = runnable;
        res = new TaskReturn<E>();
    }

    @Override
    public void fire() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,Exception {    
        if(callable!=null){
            res.res = callable.call();
        }else if(runnable !=null){
            runnable.run();
        }
       
        synchronized (res) {
            res.finishFlag = true;
            res.notify();
        }
    }
    
    @Override
    public TaskReturn<E> getReturn() {
        return res;
    }
}
