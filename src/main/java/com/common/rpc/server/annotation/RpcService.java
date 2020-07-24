package com.common.rpc.server.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 表示当前服务为RPC服务
 * name为空时，以Class对象的name为key
 * @author 曾鹏
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {
    @AliasFor(annotation = Component.class)
    String value() default "";


    Class<?> clazz();
    String name() default "";
}
