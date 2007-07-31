package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.commons.id.uuid.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.lastbamboo.common.stun.stack.message.BindingErrorResponse;
import org.lastbamboo.common.stun.stack.message.BindingRequest;
import org.lastbamboo.common.stun.stack.message.NullStunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;
import org.lastbamboo.common.stun.stack.message.BindingSuccessResponse;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorAdapter;
import org.lastbamboo.common.stun.stack.message.turn.AllocateRequest;
import org.lastbamboo.common.stun.stack.message.turn.ConnectRequest;
import org.lastbamboo.common.stun.stack.message.turn.ConnectionStatusIndication;
import org.lastbamboo.common.stun.stack.message.turn.DataIndication;
import org.lastbamboo.common.stun.stack.message.turn.SendIndication;
import org.lastbamboo.common.stun.stack.message.turn.AllocateSuccessResponse;

/**
 * Class that visits read messages on a STUN server.
 */
public class StunServerMessageVisitor extends StunMessageVisitorAdapter<Object>
    {

    private static final Log LOG = 
        LogFactory.getLog(StunServerMessageVisitor.class);
    
    private final IoSession m_session;

    /**
     * Creates a new visitor for visiting STUN messages on the server side.
     * 
     * @param session The MINA IO session.
     */
    public StunServerMessageVisitor(final IoSession session)
        {
        m_session = session;
        }

    public Object visitBindingRequest(final BindingRequest binding)
        {
        LOG.debug("STUN server visiting binding request...");
        final InetSocketAddress address = 
            (InetSocketAddress) m_session.getRemoteAddress();
        
        final UUID transactionId = binding.getTransactionId();
        final StunMessage response = 
            new BindingSuccessResponse(transactionId.getRawBytes(), address);
        
        this.m_session.write(response);
        return null;
        }
    }
