/*
 * 宜人贷
 * TaskReturn.java
 * Copyright (c) 2012 普信恒业科技发展(北京)有限公司
 * All rights reserved.
 * --------------------------
 * Aug 9, 2016 Created
 */
package pers.stone.utils.mythread;

/**
 * 
 * 类名: TaskReturn <br/>
 * 用途: <br/>
 * 
 * @author wangbo <br/>
 *         Aug 9, 2016 5:05:18 PM
 */
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
