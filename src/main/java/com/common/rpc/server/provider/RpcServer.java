package com.common.rpc.server.provider;

import com.common.rpc.common.utils.BeanUtils;
import com.common.rpc.common.utils.StringUtils;
import com.common.rpc.register.impl.ZooKeeperServiceRegistry;
import com.common.rpc.server.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明:
 *
 * @author zengpeng
 */
@Slf4j
@Component
public class RpcServer implements ApplicationContextAware , InitializingBean {

    /**
     * key:
     * 服务名 => 服务对象
     */
    public static Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 扫描带有 RpcService 注解的类并初始化 handlerMap 对象
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.clazz().getName();
                String name = rpcService.name();
                if(!StringUtils.isEmpty(name)){
                    log.error("registry name is null");
                    handlerMap.put(serviceName,serviceBean);
                }
                //将注有RpcService的实现类存放到map中
                handlerMap.put(name,serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将信息注册到zk
        String zookeeperAddress = BeanUtils.getPropertiesValue("rpc.center.ip");
        if(zookeeperAddress == null){
            throw new Exception("rpc center config ip is null");
        }
        String providerAddress = BeanUtils.getPropertiesValue("rpc.provider.ip");
        if(providerAddress == null){
            throw new Exception("rpc provider config ip is null");
        }
        ZooKeeperServiceRegistry serviceRegistry = new ZooKeeperServiceRegistry(zookeeperAddress);
        // 注册 RPC 服务地址
        if (serviceRegistry != null) {
            for (String interfaceName : handlerMap.keySet()) {
                serviceRegistry.register(interfaceName, providerAddress);
                System.out.println("register service: { " + interfaceName + " } => { " + providerAddress + " }");
                log.info("register service: {} => {}", interfaceName, providerAddress);
            }
        }
        RpcServerTask rpcServerTask = new RpcServerTask();
        Thread thread = new Thread(rpcServerTask);
        thread.setName("rpc-server");
        thread.start();
        System.out.println("testtsetaetaetaetaate");



    }
}
