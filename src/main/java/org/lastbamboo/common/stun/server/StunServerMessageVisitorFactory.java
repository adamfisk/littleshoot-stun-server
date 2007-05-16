package org.lastbamboo.common.stun.server;

import org.apache.mina.common.IoSession;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitor;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;

public class StunServerMessageVisitorFactory implements
        StunMessageVisitorFactory
    {

    public StunMessageVisitor createVisitor(final IoSession session)
        {
        return new StunServerMessageVisitor(session);
        }

    }
