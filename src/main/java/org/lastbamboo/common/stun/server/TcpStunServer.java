package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoServiceListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.StunProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;
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
    private final IoHandler m_ioHandler;
    private final MinaTcpServer m_server;
    
    /**
     * Creates a new STUN server.
     * 
     * @param ioHandler The {@link IoHandler} to use for processing messages
     * and {@link IoSession}s. 
     * @param messageVisitorFactory The factory for creating classes for 
     * visiting STUN messages and handling them appropriately as they're read.
     * @param threadName Additional string for thread naming to make 
     * debugging easier.
     */
    public TcpStunServer(final IoHandler ioHandler, 
        final StunMessageVisitorFactory messageVisitorFactory, 
        final String threadName)
        {
        super(messageVisitorFactory, threadName);
        this.m_ioHandler = ioHandler;
        final ProtocolCodecFactory codecFactory = 
            new StunProtocolCodecFactory();
        
        this.m_server = new MinaTcpServer(codecFactory, this, 
            this.m_ioHandler, "TCP-STUN-Server-" + this.m_threadName);
        }

    @Override
    protected void bind(final InetSocketAddress bindAddress)
        {
        m_log.debug("Running STUN TCP server on: {}", bindAddress);
        m_server.start(bindAddress.getPort());
        }

    public void addIoServiceListener(IoServiceListener serviceListener)
        {
        this.m_server.addIoServiceListener(serviceListener);
        }

    }
