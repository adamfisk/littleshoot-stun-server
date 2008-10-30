package org.lastbamboo.common.stun.server;

import java.io.IOException;

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
        try
            {
            launcher.launch();
            LOG.debug("Started launcher");
            }
        catch (final IOException e)
            {
            LOG.error("Could not start!!!",e);
            }
        }

    /**
     * Launches the server.
     * @throws IOException If we cannot bind the server port. 
     */
    public void launch() throws IOException
        {
        final StunMessageVisitorFactory messageVisitorFactory = 
            new StunServerMessageVisitorFactory();
        final StunServer server = 
            new UdpStunServer(messageVisitorFactory, "UDP-STUN-Server");
        server.start();
        }

    }
