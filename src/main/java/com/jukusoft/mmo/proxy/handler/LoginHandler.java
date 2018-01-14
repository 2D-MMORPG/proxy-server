package com.jukusoft.mmo.proxy.handler;

import com.jukusoft.mmo.proxy.message.LoginRequestMessage;
import com.jukusoft.mmo.proxy.network.message.MessageReceiver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler implements MessageReceiver<LoginRequestMessage> {

    @Override
    public void onReceive(LoginRequestMessage msg) {
        Logger.getAnonymousLogger().log(Level.INFO, "try to login user '" + msg.getUsername() + "'.");
    }

    @Override
    public boolean isLoginRequired() {
        return false;
    }

    @Override
    public boolean isChoosenCharacterRequired() {
        return false;
    }

}
