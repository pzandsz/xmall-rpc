package com.common.rpc.common.codec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 编码类型枚举类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CodeTypeEnum {

    PROTO_STUFF(1,"protostuff编码");

    private Integer id;
    private String msg;
}
