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

    private static final Logger LOG = 
        LoggerFactory.getLogger(TcpStunServer.class);
    
    /**
     * Creates a new STUN server.
     */
    public TcpStunServer()
        {
        this(new StunServerMessageVisitorFactory(), "");
        }
    
    /**
     * Creates a new STUN server.
     * 
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     */
    public TcpStunServer(final StunMessageVisitorFactory visitorFactory)
        {
        this(visitorFactory, "");
        }
    
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
            handler, bindAddress.getPort(), "TCP-STUN-Server");
        server.start();
        }

    }
