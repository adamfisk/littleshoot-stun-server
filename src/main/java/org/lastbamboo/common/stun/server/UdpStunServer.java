package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.common.ExecutorThreadModel;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoServiceListener;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.lastbamboo.common.stun.stack.StunIoHandler;
import org.lastbamboo.common.stun.stack.StunProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a STUN server.
 */
public class UdpStunServer extends AbstractStunServer
    {

    private static final Logger LOG = 
        LoggerFactory.getLogger(UdpStunServer.class);
    private final DatagramAcceptor m_acceptor = 
        new DatagramAcceptor(Executors.newCachedThreadPool());
    
    /**
     * Creates a new STUN server.
     */
    public UdpStunServer()
        {
        this(new StunServerMessageVisitorFactory(), "");
        }
    
    /**
     * Creates a new STUN server.
     * 
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     */
    public UdpStunServer(final StunMessageVisitorFactory visitorFactory)
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
    public UdpStunServer(final StunMessageVisitorFactory visitorFactory, 
        final String threadName)
        {
        super(visitorFactory, threadName);
        }
    
    @Override
    protected void bind(InetSocketAddress bindAddress)
        {
        m_acceptor.addListener(this);
        final DatagramAcceptorConfig config = new DatagramAcceptorConfig();
        config.getSessionConfig().setReuseAddress(true);
        config.setThreadModel(
            ExecutorThreadModel.getInstance("STUN Server: "+this.m_threadName));
        
        final ProtocolCodecFactory codecFactory = 
            new StunProtocolCodecFactory();
        final ProtocolCodecFilter codecFilter = 
            new ProtocolCodecFilter(codecFactory);
        config.getFilterChain().addLast("stunFilter", codecFilter);
        config.getFilterChain().addLast("executor", 
            new ExecutorFilter(Executors.newCachedThreadPool()));
        final IoHandler handler = new StunIoHandler(this.m_visitorFactory);
        
        try
            {
            m_acceptor.bind(bindAddress, handler, config);
            LOG.debug("Started STUN server!!");
            }
        catch (final IOException e)
            {
            LOG.error("Could not bind server", e);
            }
        }

    public void addIoServiceListener(final IoServiceListener serviceListener)
        {
        this.m_acceptor.addListener(serviceListener);
        }

    }
