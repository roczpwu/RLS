package com.logger.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * User: rocwu
 * Date: 2017/5/21
 * Time: 下午7:56
 * Desc: 带消息长度的消息编码器
 */
public class BytesEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] bytes, ByteBuf byteBuf) throws Exception {
        if (bytes == null)
            bytes = new byte[0];
        int dataLength = bytes.length;  //读取消息的长度
        byteBuf.writeInt(dataLength);   //先将消息长度写入，也就是消息头
        byteBuf.writeBytes(bytes);      //消息体中包含要发送的数据
    }
}
