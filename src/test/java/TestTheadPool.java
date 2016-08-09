import pers.stone.utils.mythread.MyThreadPool;
import pers.stone.utils.mythread.TaskReturn;


/**
 * 
 * 类名: TestTheadPool <br/>
 * 用途:  <br/>
 * 
 * @author wangbo <br/>
 *         Jul 29, 2016 6:58:01 PM
 */
public class TestTheadPool {
    public String doTask(String taskName) throws InterruptedException{
        System.out.println("task:"+taskName+" start");
        for(int i =0; i<5;i++){
            Thread.sleep(1000);
            System.out.println("task:"+taskName+" step:"+i);
        }
        
        return "task:"+taskName+" finish";   
    }

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        MyThreadPool myThreadPool = new MyThreadPool(4,4);
        TestTheadPool test = new TestTheadPool();      
        TaskReturn<String> res1 = myThreadPool.submit(test, "doTask", "task1");
        TaskReturn<String> res2 = myThreadPool.submit(test, "doTask", "task2");
        
        System.out.println("res1:"+res1.getTaskResult());
        System.out.println("res2:"+res2.getTaskResult());
        
        myThreadPool.shutdown();
    }   

}
