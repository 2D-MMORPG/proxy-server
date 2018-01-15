package com.jukusoft.mmo.proxy;

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.mmo.proxy.database.Database;
import com.jukusoft.mmo.proxy.database.MySQLDatabase;
import com.jukusoft.mmo.proxy.handler.LoginHandler;
import com.jukusoft.mmo.proxy.message.LoginRequestCodec;
import com.jukusoft.mmo.proxy.message.LoginRequestMessage;
import com.jukusoft.mmo.proxy.network.SocketManager;
import com.jukusoft.mmo.proxy.network.VertxManager;
import com.jukusoft.mmo.proxy.network.TCPServer;
import com.jukusoft.mmo.proxy.network.gateway.TCPGateway;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    protected static boolean dbConnected = false;

    /**
    * main method
    */
    public static void main (String[] args) {
        //create an new hazelcast instance
        Config config = new Config();

        //disable hazelcast logging
        config.setProperty("hazelcast.logging.type", "none");

        CacheSimpleConfig cacheConfig = new CacheSimpleConfig();
        config.getCacheConfigs().put("session-cache", cacheConfig);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

        //create and initialize new vertx instance
        VertxManager vertxManager = new VertxManager();
        vertxManager.init(hazelcastInstance);

        //open database connection
        Database database = new MySQLDatabase(vertxManager.getVertx());

        //connect to database asynchronous
        try {
            database.connect("./config/mysql.cfg", res1 -> {
                if (!res1.succeeded()) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Couldnt connect to database: " + res1.cause());
                    System.exit(1);
                }

                Logger.getAnonymousLogger().log(Level.INFO, "Connected to database successfully.");

                dbConnected = true;
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //wait while mysql client is connecting to mysql database
        while (!dbConnected) {
            Thread.currentThread().yield();
        }

        //create new server
        TCPServer server = new TCPServer(vertxManager.getVertx());

        //activate SSL
        //TODO: remove test password later
        server.initSSL("./config/keystore.jks", "my-pass");

        //create new gateway
        TCPGateway gateway = new TCPGateway(database, hazelcastInstance);

        //add codec
        gateway.addCodec(new LoginRequestCodec(), LoginRequestMessage.class);

        //add message handler
        gateway.addHandler(new LoginHandler(), LoginRequestMessage.class);

        //create new socket manager
        SocketManager socketManager = new SocketManager(gateway);

        //start server
        server.startServer(2600, socketManager);

        Logger.getAnonymousLogger().log(Level.INFO, "proxy server started.");
    }

}
