package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageFactoryImpl;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributesFactory;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributesFactoryImpl;

/**
 * Launches the STUN server.
 */
public class Launcher
    {

    private static final Log LOG = LogFactory.getLog(Launcher.class);
    
    /**
     * Launches the STUN server.
     * 
     * @param args The command line arguments.
     */
    public static void main(final String[] args)
        {
        LOG.debug("Launching SIP and TURN servers...");
        final Launcher launcher = new Launcher();
        LOG.debug("Created launcher");
        launcher.start();
        LOG.debug("Started launcher");
        }

    private void start()
        {
        final StunAttributesFactory attributesFactory = 
            new StunAttributesFactoryImpl();
        final StunMessageFactory messageFactory = 
            new StunMessageFactoryImpl(attributesFactory);
        final StunServer server = new StunServerImpl(messageFactory);
        server.start();
        }

    }
