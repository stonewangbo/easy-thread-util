package pers.stone.utils.chars;

/**
 * 
 * 类名: CharUtils <br/>
 * 用途: 字符处理工具 <br/>
 * 
 * @author wangbo <br/>
 *         Oct 15, 2015 7:45:02 PM
 */
public class CharUtils {

    static final byte[] replace = "?".getBytes();

    /**
     * 处理mysql无法识别的utf-8字符集
     * 
     * @param
     * @return
     */
    static public String replaceMysqlUnsupport(String str) {
        /**
         * U-00000000 - U-0000007F: 0xxxxxxx U-00000080 - U-000007FF: 110xxxxx
         * 10xxxxxx U-00000800 - U-0000FFFF: 1110xxxx 10xxxxxx 10xxxxxx
         * U-00010000 - U-001FFFFF: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
         * U-00200000 - U-03FFFFFF: 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
         * U-04000000 - U-7FFFFFFF: 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
         * 10xxxxxx 从4字节字符开始，mysql utf-8无法识别，因此使用此工具对其进行处理
         */
        byte[] src = str.getBytes();
        byte[] res = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            // 过滤出4字节以上的字符
            if (v >= 0xf0 && v <= 0xff) {
                res[i] = replace[0];
                // 以下代码判断字符占用的字节数
                int char_length = 4;
                if (v >= 0xf8 && v <= 0xfb) {
                    char_length = 5;
                }
                if (v >= 0xfc) {
                    char_length = 6;
                }
                // 将字符占用的字节用问号替换
                for (int c = 1; c < char_length; c++) {
                    if (++i < src.length) {
                        res[i] = replace[0];
                    }
                }
            } else {
                res[i] = src[i];
            }
        }
        return new String(res);
    }

    static public void main(String[] arg) {
        System.out.println("res:" + CharUtils.replaceMysqlUnsupport("1测试汉字23😯4"));
    }
}
