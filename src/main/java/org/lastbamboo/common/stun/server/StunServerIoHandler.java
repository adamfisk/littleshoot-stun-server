package org.lastbamboo.common.stun.server;

import org.apache.mina.common.IoHandler;
import org.lastbamboo.common.stun.stack.AbstractStunIoHandler;

/**
 * {@link IoHandler} for STUN servers.
 */
public class StunServerIoHandler extends AbstractStunIoHandler
    {

    /**
     * Creates a new server IO handler.
     */
    public StunServerIoHandler()
        {
        super(new StunServerMessageVisitorFactory());
        }

    }
