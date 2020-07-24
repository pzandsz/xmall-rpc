package com.common.rpc.common.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

/**
 * 类说明:
 *
 * @author zengpeng
 */
@Slf4j
public class Logger {
    public static void ErrLog(String msg) {
        log.info(msg);
    }

    public static boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    public static boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public static void ErrLog(String msg,Throwable e){
        log.error("msg",e);
    }

    public static void ErrLog(Exception e) {
        log.error("", e);
    }

    public static void DebugLog(String msg){
        log.debug(msg);
    }

    public static void TraceLog(String msg){
        log.trace(msg);
    }

    public static void DebugLog(String msg,Throwable e){
        log.debug(msg,e);
    }

    public static void InfoLog(String msg,Throwable e) {
        log.info(msg,e);
    }

    public static void InfoLog(String msg) {
        log.info(msg);
    }

    public static void WarnLog(String msg) {
        log.warn(msg);
    }

    public static void WarnLog(String msg,Throwable e) {
        log.warn(msg,e);
    }
}
