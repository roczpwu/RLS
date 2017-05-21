package com.logger.rlc;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午3:41
 * Desc: 日志调用方配置
 */
public class ClientConfig {
    private boolean remoteEnabled;
    private String serverAddress;
    private String serverPort;

    public boolean isRemoteEnabled() {
        return remoteEnabled;
    }

    public void setRemoteEnabled(boolean remoteEnabled) {
        this.remoteEnabled = remoteEnabled;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}
