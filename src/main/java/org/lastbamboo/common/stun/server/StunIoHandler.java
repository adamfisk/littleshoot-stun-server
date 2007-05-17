package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;
import org.lastbamboo.common.stun.stack.message.VisitableStunMessage;

/**
 * Validates a STUN message and processes it.
 */
public class StunIoHandler extends IoHandlerAdapter
    {
    
    private static final Log LOG = LogFactory.getLog(StunIoHandler.class);
    private final StunMessageVisitorFactory m_visitorFactory;
    
    /**
     * Creates a new STUN IO handler class.
     * 
     * @param visitorFactory The factory for creating visitors for the 
     * specific STUN deployment.  Some factories might create visitors for the
     * client side while others create visitors for the server side, 
     * for example.
     */
    public StunIoHandler(final StunMessageVisitorFactory visitorFactory)
        {
        m_visitorFactory = visitorFactory;
        }

    public void messageReceived(final IoSession session, final Object message)
        {
        if (LOG.isDebugEnabled())
            {
            LOG.debug("Received message: "+message);
            }
        
        final VisitableStunMessage stunMessage = (VisitableStunMessage) message;
        
        final StunMessageVisitor visitor = 
            this.m_visitorFactory.createVisitor(session);
        
        stunMessage.accept(visitor);
        }
    }
