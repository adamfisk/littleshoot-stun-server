package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.StunIoHandler;
import org.lastbamboo.common.stun.stack.StunProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;
import org.lastbamboo.common.util.mina.MinaServer;
import org.lastbamboo.common.util.mina.MinaTcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a STUN server.
 */
public class TcpStunServer extends AbstractStunServer
    {

    private final Logger m_log = 
        LoggerFactory.getLogger(TcpStunServer.class);
    
    /**
     * Creates a new STUN server.
     * 
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     * @param threadName Additional string for thread naming to make 
     * debugging easier.
     */
    public TcpStunServer(final StunMessageVisitorFactory visitorFactory, 
        final String threadName)
        {
        super(visitorFactory, threadName);
        }
    
    @Override
    protected void bind(final InetSocketAddress bindAddress)
        {
        final ProtocolCodecFactory codecFactory = 
            new StunProtocolCodecFactory();
        
        final IoHandler handler = new StunIoHandler(this.m_visitorFactory);
        final MinaServer server = new MinaTcpServer(codecFactory, this, 
            handler, bindAddress.getPort(), 
            "TCP-STUN-Server-" + this.m_threadName);
        m_log.debug("Running STUN TCP server on: {}", bindAddress);
        server.start();
        }

    }
