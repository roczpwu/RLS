package com.logger.common.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.logger.common.proto.LogBodyModule;

import java.util.Arrays;

/**
 * User: rocwu
 * Date: 2017/5/19
 * Time: 下午2:16
 * Desc:
 */
public class ProtoDemo {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        LogBodyModule.PBLogMessage.Builder builder = LogBodyModule.PBLogMessage.newBuilder();
        builder.setBid(1).setClassName("testClass").setMethodName("testMethod").setLineNumber(123).setMessage("testMsg").setDateTime(123456).setLevel("debug");
        LogBodyModule.PBLogMessage logMessage = builder.build();
        byte[] bytes = logMessage.toByteArray();
        System.out.println(Arrays.toString(bytes));

        LogBodyModule.PBLogMessage logMessage1 = LogBodyModule.PBLogMessage.parseFrom(bytes);
        System.out.println("bid: " + logMessage1.getBid());
    }
}
