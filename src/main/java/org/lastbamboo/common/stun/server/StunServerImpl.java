package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
import org.lastbamboo.common.stun.stack.message.StunDecoder;
import org.lastbamboo.common.stun.stack.message.StunEncoder;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;

/**
 * Implementation of a STUN server.
 */
public class StunServerImpl implements StunServer
    {

    private static final Log LOG = LogFactory.getLog(StunServerImpl.class);
    
    /**
     * Use the default STUN port.
     */
    private static final int STUN_PORT = 3478;

    private final StunMessageFactory m_messageFactory;

    private final StunMessageVisitorFactory m_visitorFactory;
    
    /**
     * Creates a new STUN server.
     * 
     * @param messageFactory The factory class for creating STUN messages.
     * @param visitorFactory The factory for creating classes for visiting 
     * STUN messages and handling them appropriately as they're read.
     */
    public StunServerImpl(final StunMessageFactory messageFactory,
        final StunMessageVisitorFactory visitorFactory)
        {
        m_messageFactory = messageFactory;
        m_visitorFactory = visitorFactory;
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
        }
    
    public void start()
        {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final IoAcceptor acceptor = new DatagramAcceptor(executor);
        final IoAcceptorConfig config = new DatagramAcceptorConfig();
        config.getFilterChain().addLast("executor", 
            new ExecutorFilter(executor));
        
        final ProtocolEncoder encoder = new StunEncoder();
        final ProtocolDecoder decoder = new StunDecoder(m_messageFactory);
        final ProtocolCodecFilter stunFilter = 
            new ProtocolCodecFilter(encoder, decoder);
        config.getFilterChain().addLast("to-stun", stunFilter);
        
        final IoHandler handler = 
            new StunServerIoHandler(this.m_visitorFactory);
        //acceptor.setHandler(handler);
        final InetSocketAddress address = new InetSocketAddress(STUN_PORT);
        //acceptor.setLocalAddress(address);
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
    }
