# Roc Log Service

# 1. 介绍
[Roc Log Service](https://github.com/roczpwu/rls) 是一款轻量级的支持本地或远端异步日志的软件项目，远端调用采用protobuf高性能传输协议。该项目特点是提供的接口简单，使用方便。

# 2. 依赖说明
- Java 1.7+ 
- Netty 4.1
- Protobuf 3.3.0+

# 3. 配置说明

## 3.1 日志调用端
编辑rlc.properties
```
#是否启用远程日志服务
#若选择远程日志服务，需要配置server_address和server_port
remote_enabled=true
#日志服务器的ip
server_address=127.0.0.1
#日志服务器的port
server_port=8007
```

## 3.2 日志记录端
编辑rlc.properties
```
#bid对应的日志文件路径映射和日志等级
#路径最后会跟上 日期.log
bid_path.1=/Users/rocwu/info

bid_level.1=DEBUG

#处理写日志的线程个数，建议不大于CPU核心数
threadNum=2

#服务端口号
port=8007
```

# 4.1 调用方法
获取日志对象调用com.logger.rlc.Logger的getInstance()方法
记录日志调用debug(String), info(String), error(String)方法
```java
Logger logger = Logger.getInstance();
String msg = "log something here."
logger.debug(msg);
logger.info(msg);
logger.error(msg);
```
日志内容格式为
```
[yyyy-mm-dd hh:mm:ss] LogLevel  ClassName   LineNumber: logMessage
```

