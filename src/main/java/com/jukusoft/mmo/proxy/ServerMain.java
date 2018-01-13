package com.jukusoft.mmo.proxy;

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.mmo.proxy.network.VertxManager;
import com.jukusoft.mmo.proxy.network.TCPServer;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

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

        //create new server
        TCPServer server = new TCPServer(vertxManager.getVertx());

        //activate SSL
        //TODO: remove test password later
        server.initSSL("./config/keystore.jks", "my-pass");

        //start server
        server.startServer(2600, new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket event) {
                //
            }
        });

        Logger.getAnonymousLogger().log(Level.INFO, "proxy server started.");
    }

}
