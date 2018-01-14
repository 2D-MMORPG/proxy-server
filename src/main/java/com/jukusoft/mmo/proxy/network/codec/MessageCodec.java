package com.jukusoft.mmo.proxy.network.codec;

import com.jukusoft.mmo.proxy.network.message.Message;
import io.vertx.core.buffer.Buffer;

public interface MessageCodec<V extends Message> {

    /**
     * Called by Vert.x when marshalling a message to the wire.
     *
     * @param buffer  the message should be written into this buffer
     * @param s  the message that is being sent
     */
    public void encodeToWire(Buffer buffer, V s);

    /**
     * Called by Vert.x when a message is decoded from the wire.
     *
     * @param pos  the position in the buffer where the message should be read from.
     * @param buffer  the buffer to read the message from
     * @return  the read message
     */
    public V decodeFromWire(int pos, Buffer buffer, short version);

}
