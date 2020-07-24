package com.common.rpc.common.domian;

import lombok.Data;

/**
 * 类说明: RPC请求对象
 *
 * @author zengpeng
 */
@Data
public class RpcRequest {

    /** 接口名称 */
    private String interfaceName;

    /** 方法名称 */
    private String method;

    /** 方法参数类型列表 */
    private Class<?>[] parameterTypes;

    /** 方法参数数组 */
    private Object[] parameters;

    /** 代理的应用名称 通过proxy-interface获取服务提供方的ip地址 */
    private String proxy;

    /** 请求id */
    private Long requestId;

    
}
