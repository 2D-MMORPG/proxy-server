package com.jukusoft.mmo.proxy.network;

import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {

    //instance of vert.x
    protected Vertx vertx = null;

    //vert.x network server
    protected NetServer netServer = null;

    protected boolean sslActivated = false;
    protected String keyStorePath = "";
    protected String jksPassword = "";

    /**
    * default constructor
    */
    public TCPServer(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
    * initialize SSL
     *
     * @param jksPassword jks password
    */
    public void initSSL (String keyStorePath, String jksPassword) {
        this.sslActivated = true;
        this.keyStorePath = keyStorePath;
        this.jksPassword = jksPassword;
    }

    /**
    * start tcp server
     *
     * @param port port
     * @param connectHandler connection handler
    */
    public void startServer (int port, Handler<NetSocket> connectHandler) {
        //create options for TCP network server
        NetServerOptions netServerOptions = new NetServerOptions();

        if (this.sslActivated) {
            //activate SSL
            netServerOptions.setSsl(true);
            netServerOptions.setKeyStoreOptions(
                    new JksOptions().
                            setPath(this.keyStorePath).
                            setPassword(this.jksPassword)
            );
        }

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
