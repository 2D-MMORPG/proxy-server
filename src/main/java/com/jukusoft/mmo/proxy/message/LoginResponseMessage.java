package com.jukusoft.mmo.proxy.message;

import com.jukusoft.mmo.proxy.network.message.Message;

public class LoginResponseMessage extends Message {

    protected boolean loggedIn = false;
    protected String errorMessage = "";
    protected long sessionID = 0;
    protected String username = "";

    public boolean isLoggedIn () {
        return this.loggedIn;
    }

}
