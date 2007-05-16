package org.lastbamboo.common.stun.server;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttribute;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributeType;

/**
 * Encodes bytes into STUN messages.
 */
public class StunEncoder implements ProtocolEncoder
    {

    private static final Log LOG = LogFactory.getLog(StunEncoder.class);
    
    private static final int MAGIC_COOKIE = 0x2112A442;
    
    public void dispose(final IoSession session) throws Exception
        {
        // TODO Auto-generated method stub

        }

    public void encode(final IoSession session, final Object message,
        final ProtocolEncoderOutput out) throws Exception
        {
        final StunMessage stunMessage = (StunMessage) message;
        
        final int length = stunMessage.getTotalLength();
        final ByteBuffer buf = ByteBuffer.allocate(length);
        
        buf.putShort((short) (stunMessage.getType() & 0xffff));
        buf.putShort((short) (stunMessage.getBodyLength() & 0xffff));
        
        
        buf.putInt(MAGIC_COOKIE);
        buf.put(stunMessage.getTransactionId());
        
        final Map<StunAttributeType, StunAttribute> attributes = 
            stunMessage.getAttributes();
        
        if (!buf.hasRemaining())
            {
            LOG.error("Buffer should have space!!");
            }
        
        putAttributes(attributes, buf);
        
        if (buf.hasRemaining())
            {
            LOG.error("Should have completely filled the buffer");
            }

        buf.flip();
        out.write(buf);
        }

    private void putAttributes(
        final Map<StunAttributeType, StunAttribute> attributesMap, 
        final ByteBuffer buf)
        {
        final StunAttributeVisitor visitor = new StunAttributeWriter(buf);
        final Collection<StunAttribute> attributes = attributesMap.values();
        for (final StunAttribute attribute : attributes)
            {
            attribute.accept(visitor);
            }
        }

    }
