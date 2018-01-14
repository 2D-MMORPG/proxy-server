package com.jukusoft.mmo.proxy.message;

import com.jukusoft.mmo.proxy.network.codec.MessageCodec;
import io.vertx.core.buffer.Buffer;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginRequestCodec implements MessageCodec<LoginRequestMessage> {

    @Override
    public void encodeToWire(Buffer buffer, LoginRequestMessage msg) {
        //convert username and password to byte arrays
        byte[] usernameBytes = msg.username.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = msg.password.getBytes(StandardCharsets.UTF_8);

        //add username
        buffer.appendShort((short) usernameBytes.length);
        buffer.appendBytes(usernameBytes);

        //add password
        buffer.appendShort((short) passwordBytes.length);
        buffer.appendBytes(passwordBytes);
    }

    @Override
    public LoginRequestMessage decodeFromWire(int pos, Buffer buffer, short version) {
        int usernameLength = buffer.getInt(pos);
        System.err.println("pos: " + pos + ", username length: " + usernameLength);
        pos += 4;
        byte[] usernameBytes = buffer.getBytes(pos, pos + usernameLength);

        pos += usernameLength;

        int passwordLength = buffer.getInt(pos);
        pos += 4;
        byte[] passwordBytes = buffer.getBytes(pos, pos + passwordLength);

        String username = new String(usernameBytes, StandardCharsets.UTF_8);
        String password = new String(passwordBytes, StandardCharsets.UTF_8);

        Logger.getAnonymousLogger().log(Level.INFO, "received login request for user '" + username + "'");

        return new LoginRequestMessage(username, password);
    }

}
