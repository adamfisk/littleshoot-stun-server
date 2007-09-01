package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ExecutorThreadModel;
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
import org.lastbamboo.common.stun.stack.StunIoHandler;
import org.lastbamboo.common.stun.stack.StunProtocolCodecFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;
import org.lastbamboo.common.util.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a STUN server.
 */
public abstract class AbstractStunServer implements StunServer, IoServiceListener
    {

    private static final Logger LOG = 
        LoggerFactory.getLogger(UdpStunServer.class);
    
    /**
     * Use the default STUN port.
     */
    private static final int STUN_PORT = 3478;

    protected final StunMessageVisitorFactory m_visitorFactory;

    private InetSocketAddress m_boundAddress;

    protected final String m_threadName;

    /**
     * Creates a new STUN server.
     */
    public AbstractStunServer()
        {
        this(new StunServerMessageVisitorFactory(), "");
        }
    
    /**
     * Creates a new STUN server.
     * 
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     */
    public AbstractStunServer(final StunMessageVisitorFactory visitorFactory)
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
    public AbstractStunServer(final StunMessageVisitorFactory visitorFactory, 
        final String threadName)
        {
        m_visitorFactory = visitorFactory;
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
        this.m_threadName = threadName;
        }
    
    public void start()
        {
        try
            {
            start(new InetSocketAddress(NetworkUtils.getLocalHost(),STUN_PORT));
            }
        catch (final UnknownHostException e)
            {
            LOG.error("Could not get the local address", e);
            }
        }
    
    public void start(final InetSocketAddress bindAddress)
        {
        final InetSocketAddress bindAddressToUse = 
            createBindAddress(bindAddress);

        bind(bindAddressToUse);
        final ExecutorService executor = Executors.newCachedThreadPool();
        final DatagramAcceptor acceptor = new DatagramAcceptor(executor);
        acceptor.addListener(this);
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
            new ExecutorFilter(executor));
        final IoHandler handler = new StunIoHandler(this.m_visitorFactory);
        
        try
            {
            acceptor.bind(bindAddressToUse, handler, config);
            LOG.debug("Started STUN server!!");
            }
        catch (final IOException e)
            {
            LOG.error("Could not bind server", e);
            }
        }
    
    protected abstract void bind(final InetSocketAddress bindAddress);

    private static InetSocketAddress createBindAddress(
        final InetSocketAddress bindAddress)
        {
        if (bindAddress == null)
            {
            try
                {
                return new InetSocketAddress(NetworkUtils.getLocalHost(), 0);
                }
            catch (UnknownHostException e)
                {
                LOG.warn("Could not get local host address", e);
                return null;
                }
            }
        else
            {
            return bindAddress;
            }
        }

    public InetSocketAddress getBoundAddress()
        {
        return this.m_boundAddress;
        }

    public void serviceActivated(final IoService service, 
        final SocketAddress serviceAddress, final IoHandler handler, 
        final IoServiceConfig config)
        {
        // Note this is called immediately after the call to bind when
        // starting the server, so the bound address will always be set
        // if start has been called, at least with MINA 1.1.1.
        LOG.debug("Setting bound address to: {}", serviceAddress);
        this.m_boundAddress = (InetSocketAddress) serviceAddress;
        }

    public void serviceDeactivated(final IoService service, 
        final SocketAddress serviceAddress, final IoHandler handler, 
        final IoServiceConfig config)
        {
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
