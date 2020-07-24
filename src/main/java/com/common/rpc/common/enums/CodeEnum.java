package com.common.rpc.common.enums;

import lombok.*;

/**
 * 类说明: 状态枚举类
 *
 * @author zengpeng
 */
@AllArgsConstructor
@NoArgsConstructor
public enum  CodeEnum {
    CONNECTION_TIMEOUT(-10001,"连接超时"),
    RPC_SERVER_ERROR(-10002,"服务提供方异常"),
    RPC_CLIENT_ERROR(-10003,"服务消费方异常");

    @Setter @Getter
    private Integer id;

    @Getter @Setter
    private String name;
}
