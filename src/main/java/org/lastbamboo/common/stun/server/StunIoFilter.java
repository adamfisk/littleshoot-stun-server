package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;

/**
 * Filters raw buffer data from the network into STUN messages.
 */
public class StunIoFilter extends IoFilterAdapter
    {
    
    private static final Log LOG = LogFactory.getLog(StunIoFilter.class);
    
    public void messageReceived(final NextFilter nextFilter, 
        final IoSession session, final Object message)
        {
        if (LOG.isDebugEnabled())
            {
            LOG.debug("Received message: "+message);
            }
        
        //super.messageReceived(nextFilter, session, message)
        }
    }
