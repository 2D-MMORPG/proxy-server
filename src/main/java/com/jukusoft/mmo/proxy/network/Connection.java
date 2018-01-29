package com.jukusoft.mmo.proxy.network;

import com.jukusoft.mmo.proxy.network.gateway.TCPGateway;
import com.jukusoft.mmo.proxy.network.message.Message;
import io.vertx.core.net.NetSocket;

/**
* data for one connection
*/
public class Connection {

    //account information
    protected int userID = 0;
    protected String username = "";

    //flag, if user is logged in
    protected boolean loggedIn = false;

    //flag, if user has choose a character
    protected boolean characterChoosen = false;

    protected int connID = 0;
    protected NetSocket tcpSocket = null;
    protected TCPGateway gateway = null;

    public Connection (int connID, NetSocket socket, TCPGateway gateway) {
        this.connID = connID;
        this.tcpSocket = socket;
        this.gateway = gateway;
    }

    public void onClose () {
        //
    }

    public boolean isLoggedIn () {
        return this.loggedIn;
    }

    public void login (int userID, String username) {
        this.userID = userID;
        this.username = username;
    }

    public boolean hasChooseCharacter () {
        return this.characterChoosen;
    }

    public NetSocket getTcpSocket() {
        return this.tcpSocket;
    }

    public <T extends Message> void write (T msg) {
        this.gateway.send(msg, this);
    }

}
