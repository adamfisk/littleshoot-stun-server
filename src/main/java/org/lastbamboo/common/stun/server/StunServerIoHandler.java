package org.lastbamboo.common.stun.server;

import org.apache.mina.common.IoHandler;
import org.lastbamboo.common.stun.stack.AbstractStunIoHandler;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;

/**
 * {@link IoHandler} for STUN servers.
 */
public class StunServerIoHandler extends AbstractStunIoHandler
    {

    /**
     * Creates a new server IO handler.
     * 
     * @param factory The STUN server message visitor for processing read
     * messages. 
     */
    public StunServerIoHandler(final StunMessageVisitorFactory factory)
        {
        super(factory);
        }

    }
