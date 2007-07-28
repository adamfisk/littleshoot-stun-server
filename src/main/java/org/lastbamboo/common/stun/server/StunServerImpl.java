package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoService;
import org.apache.mina.common.IoServiceConfig;
import org.apache.mina.common.IoServiceListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.lastbamboo.common.stun.stack.decoder.StunProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;

/**
 * Implementation of a STUN server.
 */
public class StunServerImpl implements StunServer, IoServiceListener
    {

    private static final Log LOG = LogFactory.getLog(StunServerImpl.class);
    
    /**
     * Use the default STUN port.
     */
    private static final int STUN_PORT = 3478;

    private final StunMessageVisitorFactory m_visitorFactory;

    /**
     * Creates a new STUN server.
     */
    public StunServerImpl()
        {
        this(new StunServerMessageVisitorFactory());
        }
    
    /**
     * Creates a new STUN server.
     * 
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     */
    public StunServerImpl(final StunMessageVisitorFactory visitorFactory)
        {
        m_visitorFactory = visitorFactory;
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
        }
    
    public void start()
        {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final IoAcceptor acceptor = new DatagramAcceptor(executor);
        acceptor.addListener(this);
        final DatagramAcceptorConfig config = new DatagramAcceptorConfig();
        config.getSessionConfig().setReuseAddress(true);
        
        final ProtocolCodecFactory codecFactory = 
            new StunProtocolCodecFactory();
        final ProtocolCodecFilter codecFilter = 
            new ProtocolCodecFilter(codecFactory);
        config.getFilterChain().addLast("stunFilter", codecFilter);
        config.getFilterChain().addLast("executor", 
            new ExecutorFilter(executor));
        final IoHandler handler = 
            new StunServerIoHandler(this.m_visitorFactory);
        
        final InetSocketAddress address = new InetSocketAddress(STUN_PORT);
        try
            {
            acceptor.bind(address, handler, config);
            LOG.debug("Started STUN server!!");
            }
        catch (final IOException e)
            {
            LOG.error("Could not bind server", e);
            }
        }

    public void serviceActivated(final IoService service, 
        final SocketAddress serviceAddress, final IoHandler handler, 
        final IoServiceConfig config)
        {
        // TODO Auto-generated method stub
        
        }

    public void serviceDeactivated(final IoService service, 
        final SocketAddress serviceAddress, final IoHandler handler, 
        final IoServiceConfig config)
        {
        // TODO Auto-generated method stub
        
        }

    public void sessionCreated(IoSession session)
        {
        // TODO Auto-generated method stub
        
        }

    public void sessionDestroyed(IoSession session)
        {
        // TODO Auto-generated method stub
        
        }
    }
