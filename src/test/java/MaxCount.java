import java.util.concurrent.CountDownLatch;

import pers.stone.utils.thread.AsynchronousCallUtil;

/*
 * 宜人贷
 * MaxCount.java
 * Copyright (c) 2012 普信恒业科技发展(北京)有限公司
 * All rights reserved.
 * --------------------------
 * Mar 14, 2016 Created
 */

/**
 * 
 * 类名: MaxCount <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Mar 14, 2016 4:40:03 PM
 */
public class MaxCount {
    
    static public void main(String[] args) throws Exception{
        MaxCount maxCount = new MaxCount();
        long startTime = System.currentTimeMillis();
        maxCount.CountDownLatch();
        System.out.println("my cost:"+(System.currentTimeMillis()-startTime)+"ms");
        startTime = System.currentTimeMillis();
        maxCount.wizard();
        System.out.println("wizard cost:"+(System.currentTimeMillis()-startTime)+"ms");
    }
    
    public void CountDownLatch() throws Exception{
        AsynchronousCallUtil threadpool = new AsynchronousCallUtil();
        long target = 1000000;
        long max = (long)Math.sqrt(target);
        for(long a=max;a>=0;a--){    
            threadpool.submit(this, "countSub", target, a);
        }
        threadpool.get();
    }
   
    public void countSub(long target,long a){
        for(long b=(long)Math.sqrt(target-(a*a))+1;b>=0;b--){
            for(long c=(long)Math.sqrt(target-(a*a)-(b*b))+1;c>=0;c--){                   
                if((a*a)+(b*b)+(c*c)==target){
                    //System.out.println("a:"+a+" b:"+b+" c:"+c);   
                }
            }
        }
    }
    
    public void wizard(){
        int[] lookup = new int[1000 * 1000 + 1];
        for (int i = 0; i <= 1000; ++i) {
          lookup[i * i] = i;
        }

        for (int i = 0; i <= 1000; ++i) {
          for (int j = 0; j <= 1000; ++j) {
            int k_square = 1000 * 1000 - i * i - j * j;
            if (k_square >= 0) {
              int k = lookup[k_square];
              if (k >= 0) {
                  //System.out.println("i = " + i +", j = " + j + ", k = " + k );
              }
            }
          }
        }

    }
}
