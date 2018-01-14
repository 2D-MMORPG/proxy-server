package com.jukusoft.mmo.proxy.message;

import com.jukusoft.mmo.proxy.network.message.Message;

public class LoginRequestMessage extends Message {

    protected String username = "";
    protected String password = "";

    public LoginRequestMessage (String username, String password) {
        this.username = username;
        this.password = password;
    }

}
