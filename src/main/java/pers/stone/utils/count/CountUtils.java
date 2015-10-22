package pers.stone.utils.count;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 类名: CountUtils <br/>
 * 用途: 程序计数器 <br/>
 * 
 * @author wangbo <br/>
 *         Oct 20, 2015 5:38:22 PM
 */
public class CountUtils {

    static Map<String, Integer> countMap = new HashMap<String, Integer>();

    public static synchronized void count(String key, int count) {
        if (!countMap.containsKey(key)) {
            countMap.put(key, count);
        } else {
            countMap.put(key, countMap.get(key) + count);
        }
    }

    public static String countInfo() {
        StringBuilder res = new StringBuilder();
        for (String key : countMap.keySet()) {
            res.append("\r\n数据类型:").append(key).append(" 数量:").append(countMap.get(key));
        }
        return res.toString();
    }
}
