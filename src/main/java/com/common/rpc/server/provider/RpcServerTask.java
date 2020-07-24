package com.common.rpc.server.provider;

import com.common.rpc.common.utils.BeanUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * 类说明:
 *
 * @author zengpeng
 */
@Slf4j
public class RpcServerTask implements Runnable {


    @SneakyThrows
    @Override
    public void run() {

        //开启RPC服务端,并将服务提供方注册到zk
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //部分参数可以改为可配的
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ServerChannelInitializer<SocketChannel>());
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 获取 RPC 服务器的 IP 地址与端口号
            String serviceAddress = BeanUtils.getPropertiesValue("rpc.provider.ip");
            if(serviceAddress == null){
                throw new Exception("rpc center config ip is null");
            }
            String[] addressArray = serviceAddress.split(":");
            int port = Integer.parseInt(addressArray[1]);

            log.info("server started on port {}", port);

            // 启动 RPC 服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            // 关闭 RPC 服务器 服务提供方什么时候关闭？
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
