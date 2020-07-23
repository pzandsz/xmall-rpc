package com.common.rpc.client.consumer;

import com.common.rpc.client.handler.RpcClientHandler;
import com.common.rpc.client.handler.RpcInvocationHandler;
import com.common.rpc.common.domian.RpcRequest;
import com.common.rpc.common.domian.RpcResponse;
import com.common.rpc.common.utils.BeanUtils;
import com.common.rpc.register.ServiceDiscovery;
import com.common.rpc.register.impl.ZooKeeperServiceDiscovery;
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
 * @author huangyong
 * @since 1.0.0
 */
@Slf4j
public class RpcConsumerBean implements ApplicationContextAware, FactoryBean<Object> {

//    private AtomicBoolean startFlag = new AtomicBoolean();
    private String proxy;
    private String interfaceName;
    private Class<?> interfaceClazz;

    private String centerAddress;

    public String getCenterAddress() {
        return centerAddress;
    }

    public void setCenterAddress(String centerAddress) {
        this.centerAddress = centerAddress;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Class<?> getInterfaceClazz() {
        return interfaceClazz;
    }

    public void setInterfaceClazz(Class<?> interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

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
    public Object getObject() throws Exception {
        //1.读取远程中心地址,BeanUtils读取不到配置文件，为什么?
        ZooKeeperServiceDiscovery serviceDiscovery = new ZooKeeperServiceDiscovery(centerAddress);

        interfaceClazz = Class.forName(interfaceName);

        //2.将serviceName -> path 存放到map中
        String discover = null;
        try {
            discover = serviceDiscovery.discover(interfaceClazz.getName());
        } catch (Exception e) {
            log.error("remote address is null,e:{}",e);
            throw e;
        }
        pathMap.put(interfaceClazz.getName(),discover);
        Object consumerBean = create(interfaceClazz);

        //3.将代理对象存入Spring容器中
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
