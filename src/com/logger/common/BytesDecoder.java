package com.logger.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * User: rocwu
 * Date: 2017/5/21
 * Time: 下午7:59
 * Desc: 带消息长度的消息解码器
 */
public class BytesDecoder extends ByteToMessageDecoder {

    private static final int HEAD_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于Encoder中我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
        if (byteBuf.readableBytes() < HEAD_LENGTH) {
            return;
        }
        // 标记一下当前的readIndex的位置
        byteBuf.markReaderIndex();
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        int dataLength = byteBuf.readInt();
        // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < 0) {
            ctx.close();
        }
        //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[dataLength];  //传输正常
        byteBuf.readBytes(bytes);
        list.add(bytes);
    }
}
