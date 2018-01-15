package com.jukusoft.mmo.proxy.network.message;

import com.jukusoft.mmo.proxy.network.Connection;

public interface MessageReceiver<T> {

    /**
    * receive message
     *
     * @param msg instance of message
    */
    public void onReceive(T msg, Connection connection);

    /**
    * is login required to handle this message
     *
     * @return true, if login is required to handle this message
    */
    public boolean isLoginRequired ();

    /**
    * check, if user has to choose a character to handle this message
     *
     * @return true, if a choosen character is required
    */
    public boolean isChoosenCharacterRequired ();

}
