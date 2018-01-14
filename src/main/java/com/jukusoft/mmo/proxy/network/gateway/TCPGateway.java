package com.jukusoft.mmo.proxy.network.gateway;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.jukusoft.mmo.proxy.network.Connection;
import com.jukusoft.mmo.proxy.network.message.Message;
import com.jukusoft.mmo.proxy.network.codec.MessageCodec;
import com.jukusoft.mmo.proxy.network.message.MessageReceiver;
import com.jukusoft.mmo.proxy.utils.ByteUtils;
import io.vertx.core.buffer.Buffer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPGateway {

    //https://github.com/carrotsearch/hppc/tree/master/hppc-examples/src/test/java/com/carrotsearch/hppc/examples

    //map with all codecs
    protected IntObjectMap<MessageCodec> codecMap = new IntObjectHashMap<>();

    //map with all handlers
    protected IntObjectMap<MessageReceiver> receiverMap = new IntObjectHashMap<>();

    /**
    * default constructor
    */
    public TCPGateway() {
        //
    }

    /**
    * route message to specific handler
    */
    public void route (Buffer buffer, Connection conn) {
        Logger.getAnonymousLogger().log(Level.INFO, "READ: " + buffer);

        //deocde message
        Message msg = this.decodeBufferToMessage(buffer);

        //get message type
        int msgType = msg.getClass().getSimpleName().hashCode();

        //check, if message type is registered
        if (!this.receiverMap.containsKey(msgType)) {
            throw new IllegalStateException("no message handler registered for message type: " + msg.getClass().getSimpleName());
        }

        MessageReceiver receiver = this.receiverMap.get(msgType);

        //check, if login is required to handle this message
        if (receiver.isLoginRequired() && !conn.isLoggedIn()) {
            Logger.getAnonymousLogger().log(Level.INFO, "Login is required to handle message '" + msg.getClass().getSimpleName() + "', but user isnt logged in.");
        }

        //check, if choosen character is required
        if (receiver.isChoosenCharacterRequired() && !conn.hasChooseCharacter()) {
            Logger.getAnonymousLogger().log(Level.INFO, "Choosen character is required to handle message '" + msg.getClass().getSimpleName() + "', but user hasnt choosen a character yet.");
        }

        receiver.onReceive(msg);
    }

    protected <T extends Message> T decodeBufferToMessage (Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = 0;

        //log message for debugging
        Logger.getAnonymousLogger().log(Level.INFO, "received: " + ByteUtils.bytesToHex(buffer.getBytes()));

        // Length of JSON
        int length = buffer.getInt(_pos);

        //get message type
        int msgType = buffer.getInt(_pos + 4);

        //get message version
        short version = buffer.getShort(_pos + 8);

        //get ackID
        int ackID = buffer.getInt(_pos + 10);

        //get codec
        MessageCodec<T> codec = this.codecMap.get(msgType);

        if (codec == null) {
            throw new IllegalStateException("no codec is specified for received message type: " + msgType);
        }

        return codec.decodeFromWire(_pos + 14, buffer, version);
    }

    public <T extends Message> void addHandler (MessageReceiver<T> receiver, Class<? extends Message> msgType) {
        this.receiverMap.put(msgType.getSimpleName().hashCode(), receiver);
    }

    public <T extends Message> void addCodec (MessageCodec<T> codec, Class<? extends Message> msgType) {
        this.codecMap.put(msgType.getSimpleName().hashCode(), codec);
    }

}
