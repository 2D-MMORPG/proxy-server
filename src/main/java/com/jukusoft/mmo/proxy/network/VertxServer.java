package com.jukusoft.mmo.proxy.network;

import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VertxServer {

    //vert.x options
    protected VertxOptions vertxOptions = null;

    //instance of vert.x
    protected Vertx vertx = null;

    //vert.x network server
    protected NetServer netServer = null;

    //vert.x cluster manager
    protected ClusterManager clusterManager = null;

    protected boolean initialized = false;

    /**
    * default constructor
    */
    public VertxServer (HazelcastInstance hazelcastInstance) {
        //create new vert.x cluster manager
        this.clusterManager = new HazelcastClusterManager(hazelcastInstance);

        //create new vertx.io options
        this.vertxOptions = new VertxOptions();

        //use clustered mode of vert.x
        this.vertxOptions.setClustered(true);

        //set cluster manager
        this.vertxOptions.setClusterManager(this.clusterManager);

        //set high availability flag
        this.vertxOptions.setHAEnabled(true);

        //set number of threads to use in thread pools
        //this.vertxOptions.setEventLoopPoolSize(this.eventLoopPoolSize);
        //this.vertxOptions.setWorkerPoolSize(this.workerPoolSize);

        //create clustered vertx. instance
        Vertx.clusteredVertx(this.vertxOptions, res -> {
            if (res.succeeded()) {
                initialized = true;
                this.vertx = res.result();
            } else {
                // failed!
                System.exit(1);
            }
        });

        //wait while clustered vertx is initialized
        while (this.initialized == false) {
            Thread.yield();
        }
    }

    public void startServer (int port, Handler<NetSocket> connectHandler) {
        //create options for TCP network server
        NetServerOptions netServerOptions = new NetServerOptions();

        //set port
        netServerOptions.setPort(port);

        //create new instance of TCP network server
        this.netServer = this.vertx.createNetServer(netServerOptions);

        //set connect handler
        this.netServer.connectHandler(connectHandler);

        //start network server
        this.netServer.listen(res -> {
            if (res.succeeded()) {
                Logger.getAnonymousLogger().log(Level.INFO, "server is running now on port " + res.result().actualPort());
            } else {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Couldnt start network server: ", res.cause());
                System.exit(1);
            }
        });
    }

    public void startWebAPI (int port) {
        //TODO: add code here
    }

    public void shutdown () {
        this.vertx.close();
    }

}
