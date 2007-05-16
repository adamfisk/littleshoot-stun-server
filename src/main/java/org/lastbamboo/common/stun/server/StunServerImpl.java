package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramAcceptorConfig;
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
    
    public StunServerImpl(final StunMessageFactory messageFactory)
        {
        m_messageFactory = messageFactory;
        createUdpServer();
        }

    private void createUdpServer()
        {
        final ExecutorService executor = Executors.newCachedThreadPool();
        //final int processors = Runtime.getRuntime().availableProcessors();
        //private IoAcceptor acceptor = new DatagramAcceptor();
        final IoAcceptor acceptor = new DatagramAcceptor(executor);
    //        new SocketAcceptor(processors, executor);
        final IoAcceptorConfig config = new DatagramAcceptorConfig();
        //final IoAcceptorConfig config = new SocketAcceptorConfig();
        config.getFilterChain().addLast("executor", 
            new ExecutorFilter(executor));
        
        //final TextLineCodecFactory textCodecFactory = 
          //  new TextLineCodecFactory(Charset.forName("US-ASCII"));
        
        
        final ProtocolEncoder encoder = new StunEncoder();
        final ProtocolDecoder decoder = new StunDecoder(m_messageFactory);
        final ProtocolCodecFilter stunFilter = 
            new ProtocolCodecFilter(encoder, decoder);
        config.getFilterChain().addLast("to-string", stunFilter);
        //config.getFilterChain().addLast("to-stun", new StunIoFilter());
        
        final InetSocketAddress address = new InetSocketAddress(STUN_PORT);

        final StunMessageVisitorFactory factory = 
            new StunServerMessageVisitorFactory();
        try
            {
            acceptor.bind(address, new StunIoHandler(factory), config);
            LOG.debug("Started STUN server!!");
            }
        catch (final IOException e)
            {
            LOG.error("Could not bind server", e);
            }
        }
    }
