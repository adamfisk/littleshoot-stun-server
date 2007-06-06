package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageVisitorFactory;

/**
 * Launches the STUN server.
 */
public class StunServerLauncher
    {

    private static final Log LOG = LogFactory.getLog(StunServerLauncher.class);
    
    /**
     * Launches the STUN server.
     * 
     * @param args The command line arguments.
     */
    public static void main(final String[] args)
        {
        LOG.debug("Launching SIP and TURN servers...");
        final StunServerLauncher launcher = new StunServerLauncher();
        LOG.debug("Created launcher");
        launcher.launch();
        LOG.debug("Started launcher");
        }

    /**
     * Launches the server.
     */
    public void launch()
        {
        final StunMessageVisitorFactory messageVisitorFactory = 
            new StunServerMessageVisitorFactory();
        final StunServer server = new StunServerImpl(messageVisitorFactory);
        server.start();
        }

    }
