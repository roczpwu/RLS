package com.logger.rlc;

import com.logger.common.proto.LogBodyModule;
import com.logger.rlc.tcpclient.LoggerTransferClient;
import com.logger.rls.LoggerWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午2:34
 * Desc: 日志调用方
 */
public final class Logger {

    private Logger() {
        clientConfig = getConfig();
        if (!clientConfig.isRemoteEnabled()) {
            loggerWriter = LoggerWriter.getInstance();
        } else {
            loggerClient = LoggerTransferClient.getInstance(
                    clientConfig.getServerAddress(),
                    Integer.parseInt(clientConfig.getServerPort()));
        }
    }
    private static Logger logger = null;

    private LoggerWriter loggerWriter = null;
    private LoggerTransferClient loggerClient = null;

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private ClientConfig clientConfig = null;

    private ClientConfig getConfig() {
        if (clientConfig != null)
            return clientConfig;
        clientConfig = new ClientConfig();
        Properties properties = new Properties();
        InputStream is = Logger.class.getClassLoader().getResourceAsStream("rlc.properties");
        try {
            properties.load(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (properties.containsKey("remote_enabled"))
            clientConfig.setRemoteEnabled(Boolean.parseBoolean(properties.getProperty("remote_enabled")));
        if (clientConfig.isRemoteEnabled()) {
            clientConfig.setServerAddress((String) properties.get("server_address"));
            clientConfig.setServerPort((String) properties.get("server_port"));
        }
        return clientConfig;
    }

    public void debug(int bid, String message) {
        this.log(bid, message, "DEBUG");
    }

    public void info(int bid, String message) {
        this.log(bid, message, "INFO");
    }

    public void error(int bid, String message) {
        this.log(bid, message, "ERROR");
    }

    private void log(int bid, String message, String level) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement invokorTrace = trace[3];

        LogBodyModule.PBLogMessage.Builder builder = LogBodyModule.PBLogMessage.newBuilder();
        builder.setBid(bid).setClassName(invokorTrace.getClassName())
                .setMethodName(invokorTrace.getMethodName())
                .setLineNumber(invokorTrace.getLineNumber())
                .setMessage(message)
                .setDateTime(System.currentTimeMillis())
                .setLevel(level);
        LogBodyModule.PBLogMessage logMessage = builder.build();
        byte[] bytes = logMessage.toByteArray();
        if (clientConfig.isRemoteEnabled()) {
            loggerClient.send(bytes);
        } else {
            loggerWriter.log(bytes);
        }
    }
}
