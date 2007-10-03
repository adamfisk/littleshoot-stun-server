package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoService;
import org.apache.mina.common.IoServiceConfig;
import org.apache.mina.common.IoServiceListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
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

    //protected final StunMessageVisitorFactory m_visitorFactory;

    private InetSocketAddress m_boundAddress;

    protected final String m_threadName;

    protected final ProtocolCodecFactory m_codecFactory;

    protected final IoHandler m_ioHandler;
    
    public AbstractStunServer(final ProtocolCodecFactory codecFactory, 
        final IoHandler ioHandler, final String threadName)
        {
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
        m_codecFactory = codecFactory;
        m_ioHandler = ioHandler;
        m_threadName = threadName;
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
