package com.logger.rlc;

import com.logger.rlc.Logger;
import com.logger.utils.FileUtil;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午2:56
 * Desc:
 */
public class Test {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.debug(1, "hello");
    }
}
