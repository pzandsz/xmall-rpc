package com.common.rpc.server.provider;

import com.common.rpc.common.codec.protostuff.RpcDecoder;
import com.common.rpc.common.codec.protostuff.RpcEncoder;
import com.common.rpc.common.domian.RpcRequest;
import com.common.rpc.common.domian.RpcResponse;
import com.common.rpc.server.handler.RpcServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

/**
 * 类说明:
 *
 * @author zengpeng
 */
public class ServerChannelInitializer<T extends SocketChannel> extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 解码 RPC 请求
        pipeline.addLast(new RpcDecoder(RpcRequest.class));
        // 编码 RPC 响应
        pipeline.addLast(new RpcEncoder(RpcResponse.class));
        // 处理 RPC 请求
        pipeline.addLast(new RpcServerHandler());
    }
}
