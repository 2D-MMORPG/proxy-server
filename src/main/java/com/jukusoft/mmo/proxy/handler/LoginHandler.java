package com.jukusoft.mmo.proxy.handler;

import com.jukusoft.mmo.proxy.database.Database;
import com.jukusoft.mmo.proxy.database.InjectDatabase;
import com.jukusoft.mmo.proxy.message.LoginRequestMessage;
import com.jukusoft.mmo.proxy.message.LoginResponseMessage;
import com.jukusoft.mmo.proxy.network.Connection;
import com.jukusoft.mmo.proxy.network.message.MessageReceiver;
import com.jukusoft.mmo.proxy.utils.HashUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler implements MessageReceiver<LoginRequestMessage> {

    @InjectDatabase
    protected Database database;

    protected Random random = new Random();

    @Override
    public void onReceive(LoginRequestMessage msg, Connection connection) {
        if (connection.isLoggedIn()) {
            throw new IllegalStateException("user is already logged in.");
        }

        Logger.getAnonymousLogger().log(Level.INFO, "try to login user '" + msg.getUsername() + "'.");

        //query params
        JsonArray params = new JsonArray();
        params.add(msg.getUsername());

        //get row
        JsonObject row = database.getRow("SELECT * FROM `{prefix}users` WHERE `username` = ?; ", params);

        if (row == null) {
            Logger.getAnonymousLogger().log(Level.INFO, "user '" + msg.getUsername() + "' doesnt exists.");
        }

        //get password hash
        String expectedPasswordHash = row.getString("password");

        //get salt
        String salt = row.getString("salt");

        String passwordHash = HashUtils.computeSHA256Hash(msg.getPassword(), salt);

        LoginResponseMessage response = new LoginResponseMessage();

        if (expectedPasswordHash.equals(passwordHash)) {
            //login successful

            //get userID
            int userID = row.getInteger("userID");
            String username = row.getString("username");

            //set connection state to logged in
            connection.login(userID, username);

            //generate session ID
            long sessionID = this.random.nextLong();

            //get ip of user
            String ip = connection.getTcpSocket().remoteAddress().host();

            JsonArray params1 = new JsonArray();
            params1.add(ip);
            params1.add(userID);

            //save ip address, set last_online timestamp and online = 1
            this.database.update("UPDATE `{prefix}users` SET `online` = '1', `last_online` = CURRENT_TIMESTAMP, `ip` = ? WHERE `userID` = ?", params1);

            //TODO: send success message
        } else {
            //login failed

            //TODO: send failed message
        }

        //send message to client
        connection.write(response);
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
