# xmall-rpc
1.RPC的center,consumer,provider配置修改成配置文件配置的形式

2.配置的连接时间，等待时间，返回的状态信息统一一下

3.RPC的异常处理机制

4.provider在配置生产者的时候，目前无法支撑一个service下有多个实现的
场景，待改进

5.对于项目中的常量统一管理，对于字符串的处理通过统一的工具类实现，将
项目规范化

6.实现注册中心的可配置化以及支持本地注册

7.尝试实现对象编码技术的可配置化

8.尝试实现支持SpringBoot的自动配置
@SpringBootApplication,@EnableAutoConfiguration  
...
