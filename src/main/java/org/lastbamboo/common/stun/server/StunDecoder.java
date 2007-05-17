package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;

/**
 * Decodes raw byte data from the network into STUN messages. 
 */
public class StunDecoder implements ProtocolDecoder
    {

    private static final Log LOG = LogFactory.getLog(StunDecoder.class);
    
    private final StunMessageFactory m_stunMessageFactory;

    /**
     * Creates a new STUN decoder.
     * 
     * @param stunMessageFactory The factory for creating STUN messages.
     */
    public StunDecoder(final StunMessageFactory stunMessageFactory)
        {
        m_stunMessageFactory = stunMessageFactory;
        }
    
    public void decode(final IoSession session, final ByteBuffer in,
        final ProtocolDecoderOutput out) throws Exception
        {
        final StunMessage message = this.m_stunMessageFactory.createMessage(in);
        out.write(message);
        }

    public void dispose(final IoSession session) throws Exception
        {
        LOG.debug("Got dispose...");
        }

    public void finishDecode(final IoSession session, 
        final ProtocolDecoderOutput out) throws Exception
        {
        LOG.debug("Got finished decode event.");
        }

    }
