
package pers.stone.utils.mythread;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * 类名: ThreadTask <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 5:04:46 PM
 */
public interface ThreadTask<E> {
    public void fire() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception ;
    
    public TaskReturn<E> getReturn();
}
