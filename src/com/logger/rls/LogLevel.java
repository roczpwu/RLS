package com.logger.rls;

/**
 * User: rocwu
 * Date: 2017/5/22
 * Time: 下午12:27
 * Desc: 日志级别
 */
public class LogLevel {
    public static final String DEBUG    = "DEBUG";
    public static final String INFO     = "INFO";
    public static final String ERROR    = "ERROR";

    public static int getPriorty(String level) {
        if (DEBUG.equalsIgnoreCase(level)) {
            return 1;
        } else if (INFO.equalsIgnoreCase(level)) {
            return 2;
        } else if (ERROR.equalsIgnoreCase(level)) {
            return 3;
        } else {
            return 0;
        }
    }
}
