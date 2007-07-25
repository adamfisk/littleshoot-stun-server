package org.lastbamboo.common.stun.server;

import java.net.InetSocketAddress;

import org.apache.commons.id.uuid.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.lastbamboo.common.stun.stack.message.BindingRequest;
import org.lastbamboo.common.stun.stack.message.NullStunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;
import org.lastbamboo.common.stun.stack.message.SuccessfulBindingResponse;
import org.lastbamboo.common.stun.stack.message.turn.AllocateRequest;
import org.lastbamboo.common.stun.stack.message.turn.ConnectRequest;
import org.lastbamboo.common.stun.stack.message.turn.ConnectionStatusIndication;
import org.lastbamboo.common.stun.stack.message.turn.DataIndication;
import org.lastbamboo.common.stun.stack.message.turn.SendIndication;
import org.lastbamboo.common.stun.stack.message.turn.SuccessfulAllocateResponse;

/**
 * Class that visits read messages on a STUN server.
 */
public class StunServerMessageVisitor implements StunMessageVisitor<Object>
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
        final InetSocketAddress address = 
            (InetSocketAddress) m_session.getRemoteAddress();
        
        final UUID transactionId = binding.getTransactionId();
        final StunMessage response = 
            new SuccessfulBindingResponse(transactionId.getRawBytes(), address);
        
        this.m_session.write(response);
        return null;
        }

    public Object visitAllocateRequest(final AllocateRequest request)
        {
        LOG.debug("Received unhandled message on server: "+request);
        return null;
        }

    public Object visitSendIndication(final SendIndication request)
        {
        LOG.debug("Received unhandled message on server: "+request);
        return null;
        }

    public Object visitDataIndication(final DataIndication data)
        {
        LOG.debug("Received unhandled message on server: "+data);
        return null;
        }
    
    public Object visitConnectRequest(final ConnectRequest request)
        {
        LOG.debug("Received unhandled message on server: "+request);
        return null;
        }

    public Object visitSuccessfulAllocateResponse(
        final SuccessfulAllocateResponse response)
        {
        LOG.error("Unexpected message received on server: "+response);
        return null;
        }

    public Object visitConnectionStatusIndication(
        final ConnectionStatusIndication indication)
        {
        LOG.error("Unexpected message received on server: "+indication);
        return null;
        }
    
    public Object visitSuccessfulBindingResponse(
        final SuccessfulBindingResponse response)
        {
        LOG.error("We should not get binding responses on the server");
        return null;
        }

    public Object visitNullMessage(final NullStunMessage message)
        {
        LOG.error("Received null message on the server");
        return null;
        }

    }
