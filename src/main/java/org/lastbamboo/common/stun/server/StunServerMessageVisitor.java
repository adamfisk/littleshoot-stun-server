package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.id.uuid.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.lastbamboo.common.stun.stack.message.BindingRequest;
import org.lastbamboo.common.stun.stack.message.SuccessfulBindingResponse;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttribute;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributeType;
import org.lastbamboo.common.stun.stack.message.attributes.turn.DataAttribute;
import org.lastbamboo.common.stun.stack.message.attributes.turn.RemoteAddressAttribute;
import org.lastbamboo.common.stun.stack.message.turn.AllocateRequest;
import org.lastbamboo.common.stun.stack.message.turn.DataIndication;
import org.lastbamboo.common.stun.stack.message.turn.SendIndication;
import org.lastbamboo.common.stun.stack.message.turn.SuccessfulAllocateResponse;

/**
 * Class that visits read messages on a STUN server.
 */
public class StunServerMessageVisitor implements StunMessageVisitor
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

    public void visitBindingRequest(final BindingRequest binding)
        {
        final InetSocketAddress address = 
            (InetSocketAddress) m_session.getRemoteAddress();
        
        final UUID transactionId = binding.getTransactionId();
        final StunMessage response = 
            new SuccessfulBindingResponse(transactionId.getRawBytes(), address);
        
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

    public void visitSuccessfulBindingResponse(final SuccessfulBindingResponse response)
        {
        LOG.error("We should not get binding responses on the server");
        }



    public void visitAllocateRequest(final AllocateRequest request)
        {
        LOG.trace("Processing allocate request...");

        }

    public void visitSendIndication(final SendIndication request)
        {
        if (LOG.isDebugEnabled())
            {
            LOG.debug("Processing send request: "+request);
            }
        
        final Map<Integer, StunAttribute> attributes = request.getAttributes();
        
        final RemoteAddressAttribute remoteAddressAttribute =
            (RemoteAddressAttribute) request.getAttribute(
                StunAttributeType.REMOTE_ADDRESS);
        
        final InetSocketAddress remoteAddress = 
            remoteAddressAttribute.getInetSocketAddress();
        //final RemoteAddressAttribute remoteAddressAttribute =
          //  attributes.get(new Integer(StunAttributeType.REMOTE_ADDRESS));
        
        //final InetSocketAddress destinationAddress = 
          //  request.getDestinationAddress();
        
        final DataAttribute dataAttribute = 
            (DataAttribute) request.getAttribute(StunAttributeType.DATA);
        
        
        }

    public void visitDataIndication(DataIndication data)
        {
        // TODO Auto-generated method stub
        
        }

    public void visitSuccessfulAllocateResponse(SuccessfulAllocateResponse response)
        {
        // TODO Auto-generated method stub
        
        }

    }
