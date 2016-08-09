package pers.stone.utils.mythread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * 类名: ReflectThreadTask <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 5:09:15 PM
 */
public class ReflectThreadTask<E> implements ThreadTask<E> {
    Object instance;
    Method method;
    Object[] args;
    TaskReturn<E> res;
   
    /**
     * @param instance
     * @param method
     * @param args
     * @param res
     */
    public ReflectThreadTask(Object instance, Method method, Object[] args) {
        super();
        this.instance = instance;
        this.method = method;
        this.args = args;    
        res = new TaskReturn<E>();
    }


    @Override
    public void fire() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        res.res = (E) method.invoke(instance, args);
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
