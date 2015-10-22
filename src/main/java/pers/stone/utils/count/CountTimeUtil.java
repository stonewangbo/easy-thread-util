package pers.stone.utils.count;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 类名: CountTimeUtil <br/>
 * 用途: <br/>
 * 
 * @author wangbo <br/>
 *         Oct 20, 2015 5:58:09 PM
 */
public class CountTimeUtil {

    static Map<String, LogTime> timeMap = new HashMap<String, LogTime>();

    public static void startCount(String key) {
        LogTime logTime = new LogTime();
        logTime.start = new Date();
        timeMap.put(key, logTime);
    }

    public static void endCount(String key) {
        LogTime logTime = timeMap.get(key);
        logTime.end = new Date();
    }

    public static String timeInfo() {
        StringBuilder res = new StringBuilder();
        for (String key : timeMap.keySet()) {
            res.append("\r\n数据类型:").append(key).append(" 耗时:").append(timeMap.get(key)).append("秒");
        }
        return res.toString();
    }

    static class LogTime {

        Date start;
        Date end;
      
        @Override
        public String toString() {
            return String.valueOf((end.getTime() - start.getTime()) / 1000);
        }
    }
}
