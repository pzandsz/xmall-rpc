package com.common.rpc.client.handler;

import com.common.rpc.common.domian.Constants;
import com.common.rpc.common.domian.RpcRequest;
import com.common.rpc.common.domian.RpcResponse;
import com.common.rpc.common.log.Logger;
import com.common.rpc.common.utils.StringUtils;
import com.common.rpc.register.ServiceDiscovery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 类说明: RPC代理处理方法
 *
 * @author zengpeng
 */
@Data
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private String serviceAddress;

    private Class<?> interfaceClass;



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 创建 RPC 请求对象并设置请求属性
        RpcRequest request = new RpcRequest();
        request.setInterfaceName(method.getDeclaringClass().getName());
        request.setMethod(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        // 获取 RPC 服务地址
        if (serviceAddress == null) {
            Logger.ErrLog("service not registry");
            throw new RuntimeException("server address is empty");
        }
        // 从 RPC 服务地址中解析主机名与端口号
        String[] array = StringUtils.split(serviceAddress, Constants.COLON);
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        // 创建 RPC 客户端对象并发送 RPC 请求
        RpcClientHandler client = new RpcClientHandler(host, port);
        long time = System.currentTimeMillis();
        RpcResponse response = client.send(request);
        Logger.InfoLog("time: {" + (System.currentTimeMillis() - time) + "} ms");
        if (response == null) {
            throw new RuntimeException("response is null");
        }
        // 返回 RPC 响应结果
        if (response.hasException()) {
            throw response.getException();
        } else {
            return response.getResult();
        }
    }
}
