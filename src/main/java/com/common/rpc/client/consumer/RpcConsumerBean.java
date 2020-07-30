package com.common.rpc.client.consumer;

import com.common.rpc.client.handler.RpcInvocationHandler;
import com.common.rpc.register.impl.ZooKeeperServiceDiscovery;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC 代理（用于创建 RPC 服务代理）
 *
 * @author zengpeng
 * @since 1.0.0
 */
@Data
@Slf4j
public class RpcConsumerBean implements ApplicationContextAware, FactoryBean<Object> {

    private String proxy;
    private String interfaceName;
    private Class<?> interfaceClazz;

    private String centerAddress;



    public ConcurrentHashMap<String,String> pathMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler();
        rpcInvocationHandler.setInterfaceClass(interfaceClass);
        rpcInvocationHandler.setServiceAddress(pathMap.get(interfaceClass.getName()));

        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                rpcInvocationHandler
        );
    }

    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


    }

    @Override
    public Object getObject() throws ClassNotFoundException {
        //1.服务发现，远程实现
        ZooKeeperServiceDiscovery serviceDiscovery = new ZooKeeperServiceDiscovery(centerAddress);

        //2.获得Class对象
        try {
            interfaceClazz = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            log.error("can not get Class from the name :{} , e:{}",interfaceName,e);
            throw new ClassNotFoundException();
        }

        //3.将获得的服务提供方的ip地址存入内存
        String discover = null;
        try {
            discover = serviceDiscovery.discover(interfaceClazz.getName());
        } catch (Exception e) {
            log.error("remote address is null,e:{}",e);
            throw e;
        }
        pathMap.put(interfaceClazz.getName(),discover);
        Object consumerBean = create(interfaceClazz);

        //4.将代理对象存入Spring容器中
        return consumerBean;
    }

    @Override
    public Class<?> getObjectType() {

        return interfaceClazz;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
}
