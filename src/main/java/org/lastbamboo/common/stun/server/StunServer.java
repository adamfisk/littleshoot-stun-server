package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoServiceListener;

/**
 * Interface for starting a STUN server.
 */
public interface StunServer
    {

    /**
     * Starts the server on the default STUN port.
     */
    void start();
    
    /**
     * Starts the server, binding to the specified address.  If the argument
     * is <code>null</code>, this will choose an available port to bind to.
     * 
     * @param bindAddress The address to bind to.
     */
    void start(InetSocketAddress bindAddress);

    /**
     * Gets the address the server is bound to.
     * 
     * @return The address the server is bound to.
     */
    InetSocketAddress getBoundAddress();

    void addIoServiceListener(IoServiceListener serviceListener);

    }
