package com.jukusoft.mmo.proxy.network;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.jukusoft.mmo.proxy.network.gateway.TCPGateway;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketManager implements Handler<NetSocket> {

    //gateway
    protected TCPGateway gateway = null;

    //map with all active connections
    protected IntObjectMap<Connection> connMap = new IntObjectHashMap<>();

    //last ID for ID generator
    protected AtomicInteger lastID = new AtomicInteger(0);

    /**
    * default constructor
    */
    public SocketManager(TCPGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void handle(NetSocket socket) {
        //new client has connected to server
        Logger.getAnonymousLogger().log(Level.INFO, "new client connection.");

        //TODO: check firewall for blocked IPs

        //generate new connection ID
        final int connID = this.generateConnID();

        //create new connection
        Connection connection = new Connection(connID, socket, this.gateway);

        //save connection to map
        this.connMap.put(connID, connection);

        //add message handler
        socket.handler(buffer -> {
            this.gateway.route(buffer, connection);
        });

        //add close handler
        socket.closeHandler(res -> {
            //cleanup connection
            connection.onClose();

            //remove connection from map
            this.connMap.remove(connID);
        });
    }

    protected int generateConnID () {
        return this.lastID.incrementAndGet();
    }

}
