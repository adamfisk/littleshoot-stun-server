package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import junit.framework.TestCase;
import net.java.stun4j.StunAddress;
import net.java.stun4j.StunException;
import net.java.stun4j.client.MappedAddressListener;
import net.java.stun4j.client.SimpleAddressDetector;
import net.java.stun4j.stack.StunStack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests the STUN server.
 */
public class StunServerTest extends TestCase implements MappedAddressListener
    {

    private static final Log LOG = LogFactory.getLog(StunServerTest.class);
    
    public void testStunServer() throws Exception
        {
        final StunLauncher launcher = new StunLauncher();
        launcher.launch();
        
        LOG.debug("Server started...running clients...");
        
        hitServer();
        }
 
    /**
     * Uses STUN to determine information about the NAT.
     * 
     * @throws IOException If there's an error sending or receiving data from
     * the network.
     * @throws StunException If the STUN protocol data does not match the
     * expected syntax.
     */
    private void hitServer() throws Exception
        {
        LOG.trace("Querying STUN server...");

        // We just choose a random port here because we're only doing this to 
        // discover the NAT type and not because we need the mapped address.
        final int port = 8111;
        
        final StunStack stack = new StunStack();
        
        // Switch to the central server if you want to test remotely.
        //final String server = "lastbamboo.org";
        final String server = "127.0.0.1";
        final StunAddress serverAddress = new StunAddress(server, 3478);
        
        final SimpleAddressDetector detector = 
            new SimpleAddressDetector(serverAddress, stack);
        
        final StunAddress stunAddress = detector.getMappingFor(port);
        
        if (stunAddress != null)
            {
            // The address can be null if we're behind a UDP blocking 
            // firewall and therefore have no public address.
            final InetSocketAddress publicAddress = stunAddress.getSocketAddress();
            LOG.debug("Got MAPPED ADDRESS"+ publicAddress);
            }
        else
            {
            fail("Did not get address");
            }
        }

    public void mappedAddress(InetSocketAddress mappedAddress)
        {
        LOG.debug("Got address: "+mappedAddress);
        }
    
    }
