package com.jukusoft.mmo.proxy.network.message;

import com.jukusoft.mmo.proxy.network.Protocol;

public class Message implements Comparable<Message> {

    //message type
    protected short messageType = 0;

    protected short version = 1;

    //internal priority
    protected volatile int priority = 0;

    protected Protocol protocol = Protocol.TCP;

    //ack ID
    protected int ackID = 0;

    /**
    * public constructor without params, because of pooling
    */
    public Message() {
        //reset message
        this.reset();
    }

    public int getAckID () {
        return this.ackID;
    }

    public void setAckID (int ackID) {
        this.ackID = ackID;
    }

    /**
    * increment priority (each frame)
    */
    public void incrementPriority () {
        this.priority++;
    }

    /**
    * get current priority
    */
    public int priority () {
        return this.priority;
    }

    public void setPriority (int priority) {
        if (priority <= 0) {
            throw new IllegalArgumentException("priority has to be >= 0.");
        }

        if (priority > 20) {
            throw new IllegalArgumentException("max priority is 20, current value: " + priority);
        }

        this.priority = priority;
    }

    public Protocol getProtocol() {
         return this.protocol;
    }

    public void setProtocol (Protocol protocol) {
        if (protocol == null) {
            throw new NullPointerException("protocol cannot be null.");
        }

        this.protocol = protocol;
    }

    public void reset() {
        //reset values
        this.messageType = 0;
        this.version = 0;
        this.priority = 0;

        this.protocol = Protocol.TCP;
    }

    @Override
    public int compareTo(Message o) {
        return ((Integer) this.priority).compareTo(o.priority);
    }

    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Message)) {
            throw new IllegalArgumentException("to compare objects, object has to be of same type");
        }

        return this == o;
    }

    public short getVersion () {
        return this.version;
    }

}
