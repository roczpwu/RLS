package com.logger.rls;

import com.logger.common.proto.LogBodyModule;
import com.logger.utils.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午10:41
 * Desc: 写日志类
 */
public class LoggerWriter {
    private LoggerWriter() {
        int threadNum = ServerConfig.getServerConfig().getThreadNum();
        this.logService = Executors.newFixedThreadPool(threadNum);
    }
    private static LoggerWriter instance = null;

    private ExecutorService logService = null;

    public static LoggerWriter getInstance() {
        if (instance == null) {
            instance = new LoggerWriter();
        }
        return instance;
    }

    public void log(byte[] logBytes) {
        logService.execute(new Runnable() {
            @Override
            public void run() {
                log2File(logBytes);
            }
        });
    }

    private void log2File(byte[] logBytes) {
        LogBodyModule.PBLogMessage logBody;
        try {
            logBody = LogBodyModule.PBLogMessage.parseFrom(logBytes);
            int bid = logBody.getBid();
            if (!ServerConfig.getServerConfig()
                    .getLogPathMap().containsKey(bid)) {
                System.out.println("bid: "+bid+" illegal.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            long dateTime = logBody.getDateTime();
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String ctime = formatter.format(new Date(dateTime));
            sb.append("[").append(ctime).append("]").append("\t");
            sb.append(logBody.getLevel()).append("\t");
            sb.append(logBody.getClassName()).append("\t\t");
            sb.append(logBody.getLineNumber()).append(":\t");
            sb.append(logBody.getMessage()).append("\n");
            String fileName = ServerConfig.getServerConfig()
                    .getLogPathMap().get(bid)+"-"+ctime.substring(0, 10)+".log";
            String logStr = sb.toString();
            System.out.println(logStr);
            String configLevel = ServerConfig.getServerConfig().getLogLevelMap().get(bid);
            String targetLevel = logBody.getLevel();
            int configPrior = LogLevel.getPriorty(configLevel);
            int targetPrior = LogLevel.getPriorty(targetLevel);
            if (targetPrior>=configPrior)
                FileUtil.append(logStr, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("illegal log protobuf bytes.");
        }
    }
}
