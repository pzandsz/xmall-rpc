package com.common.rpc.server.handler;

import com.common.rpc.common.domian.RpcRequest;
import com.common.rpc.common.domian.RpcResponse;
import com.common.rpc.common.utils.BeanUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 类说明:
 * 服务端请求处理器
 * @author zengpeng
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        try {
            Object handle = handle(msg);
            rpcResponse.setResult(handle);
        } catch (InvocationTargetException e) {
            log.error("[RpcServerHandler] error:{}",e);
            rpcResponse.setException(e);
        }

        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);

    }


    private Object handle(RpcRequest request) throws InvocationTargetException {
        System.out.println("请求进来: " + request.getInterfaceName());
        //1.获取interfaceName对应的实现类数组,从Spring容器中获取
        Object serviceBean = handlerMap.get(request.getInterfaceName());

        Class<?> serviceClass = handlerMap.get(request.getInterfaceName()).getClass();

        String methodName = request.getMethod();

        Class<?>[] parameterTypes = request.getParameterTypes();

        Object[] parameters = request.getParameters();
        //2.FastClass不使用反射类（Constructor或Method）来调用委托类方法，
        // 而是动态生成一个新的类（继承FastClass）
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }


}
