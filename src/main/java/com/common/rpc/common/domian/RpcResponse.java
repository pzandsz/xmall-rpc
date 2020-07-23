package com.common.rpc.common.domian;

import lombok.Builder;
import lombok.Data;

/**
 * 类说明:RPC请求响应
 *
 * @author zengpeng
 */
@Data
public class RpcResponse {

    /** 返回结果 */
    private Object result;

    /** 异常信息 */
    private Exception exception;

    public boolean hasException(){
        if(exception == null){
            return false;
        }
        return true;
    }
}
