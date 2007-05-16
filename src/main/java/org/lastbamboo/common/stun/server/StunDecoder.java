package org.lastbamboo.common.stun.server;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;

public class StunDecoder implements ProtocolDecoder
    {

    private final StunMessageFactory m_stunMessageFactory;

    public StunDecoder(final StunMessageFactory stunMessageFactory)
        {
        m_stunMessageFactory = stunMessageFactory;
        }
    
    public void decode(final IoSession session, final ByteBuffer in,
        final ProtocolDecoderOutput out) throws Exception
        {
        final StunMessage message = this.m_stunMessageFactory.createMessage(in);
        
        out.write(message);
        //final StunMessageModifier message = new StunMessageModifier();
        }

    public void dispose(IoSession session) throws Exception
        {
        // TODO Auto-generated method stub

        }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception
        {
        // TODO Auto-generated method stub

        }

    }
