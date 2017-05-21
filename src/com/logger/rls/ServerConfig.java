package com.logger.rls;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午10:38
 * Desc: 被调用方配置
 */
public class ServerConfig {

    private ServerConfig() {}
    private static ServerConfig serverConfig = null;

    private Map<Integer, String> logPathMap = new HashMap<>();
    private int threadNum = 1;
    private int port = 8007;

    public Map<Integer, String> getLogPathMap() {
        return logPathMap;
    }

    public void setLogPathMap(Map<Integer, String> logPathMap) {
        this.logPathMap = logPathMap;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static ServerConfig getServerConfig() {
        if (serverConfig == null) {
            serverConfig = new ServerConfig();
            Properties properties = new Properties();
            InputStream is = LoggerWriter.class.getClassLoader().getResourceAsStream("rls.properties");
            try {
                properties.load(is);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            for (Object key : properties.keySet()) {
                String propertyName = (String) key;
                String propertyValue = properties.getProperty(propertyName);
                if (propertyName.startsWith("bid.")) {
                    int bid = Integer.parseInt(propertyName.substring(4));
                    serverConfig.getLogPathMap().put(bid, propertyValue);
                } else if ("threadNum".equals(propertyName)) {
                    serverConfig.setThreadNum(Integer.parseInt(propertyValue));
                } else if ("port".equals(propertyName)) {
                    serverConfig.setPort(Integer.parseInt(propertyValue));
                }
            }
        }
        return serverConfig;
    }
}
