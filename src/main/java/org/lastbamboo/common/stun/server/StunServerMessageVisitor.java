package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.lastbamboo.common.stun.stack.message.BindingRequest;
import org.lastbamboo.common.stun.stack.message.BindingResponse;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;

/**
 * Class that visits read messages on a STUN server.
 */
public class StunServerMessageVisitor implements StunMessageVisitor
    {

    private static final Log LOG = 
        LogFactory.getLog(StunServerMessageVisitor.class);
    
    private final IoSession m_session;

    public StunServerMessageVisitor(final IoSession session)
        {
        m_session = session;
        }

    public void visitBindingRequest(final BindingRequest binding)
        {
        final InetSocketAddress address = 
            (InetSocketAddress) m_session.getRemoteAddress();
        
        final byte[] transactionId = binding.getTransactionId();
        final StunMessage response = 
            new BindingResponse(transactionId, address);
        
        final WriteFuture future = this.m_session.write(response);
        
        future.join();
        
        if (future.isWritten())
            {
            if (LOG.isDebugEnabled())
                {
                LOG.debug("Wrote message");
                }
            }
        else
            {
            if (LOG.isDebugEnabled())
                {
                LOG.debug("Did not write message");
                }
            }
        }

    public void visitBindingResponse(final BindingResponse response)
        {
        LOG.error("We should not get binding responses on the server");
        }

    }
