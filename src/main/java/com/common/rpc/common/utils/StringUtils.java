package com.common.rpc.common.utils;

/**
 * 类说明: String工具类
 *
 * @author zengpeng
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return null == str || "".equals(str.trim());
    }


}
