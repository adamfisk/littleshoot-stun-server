package org.lastbamboo.common.stun.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import junit.framework.TestCase;
import net.java.stun4j.NetAccessPointDescriptor;
import net.java.stun4j.StunAddress;
import net.java.stun4j.StunException;
import net.java.stun4j.client.MappedAddressListener;
import net.java.stun4j.client.NetworkConfigurationDiscoveryProcess;
import net.java.stun4j.client.StunDiscoveryReport;
import net.java.stun4j.stack.StunStack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.transport.socket.nio.DatagramConnector;
import org.lastbamboo.common.stun.stack.message.StunMessage;
import org.lastbamboo.common.stun.stack.message.StunMessageFactory;
import org.lastbamboo.common.stun.stack.message.StunMessageFactoryImpl;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributesFactory;
import org.lastbamboo.common.stun.stack.message.attributes.StunAttributesFactoryImpl;
import org.lastbamboo.common.util.NetworkUtils;

/**
 * Tests the STUN server.
 */
public class StunServerTest extends TestCase implements MappedAddressListener
    {

    private static final Log LOG = LogFactory.getLog(StunServerTest.class);
    
    public void testStunServer() throws Exception
        {
        final StunAttributesFactory attributesFactory = 
            new StunAttributesFactoryImpl();
        
        final StunMessageFactory messageFactory = 
            new StunMessageFactoryImpl(attributesFactory);
        
        final StunServer server = new StunServerImpl(messageFactory);
        
        LOG.debug("Server started...running clients...");
        
        hitServer();
        /*
        final IoConnector connector = new DatagramConnector();
        final InetSocketAddress stunServer = 
            new InetSocketAddress(NetworkUtils.getLocalHost(), 3478);
        
        final IoHandler handler = new StunClientIoHandler();
        final ConnectFuture future = connector.connect(stunServer, handler);
        
        future.join();
        final IoSession session = future.getSession();
        
        final StunMessage message = messageFactory.createBindingRequest();
        
        final ByteBuffer buffer = ByteBuffer.wrap("Test this out\r\n".getBytes());
        session.write(buffer);
        */
        
        //session.getCloseFuture().join();
        
        //acceptor.unbind( address );
        
        //Thread.sleep(12000);
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
        
        final StunAddress localAddress = 
            new StunAddress(NetworkUtils.getLocalHost(), port);
        final NetAccessPointDescriptor desc =
            new NetAccessPointDescriptor(localAddress);
        
        final StunStack stack = new StunStack();
        
        final StunAddress serverAddress = 
            new StunAddress("127.0.0.1", 3478);
        final NetworkConfigurationDiscoveryProcess netConfigDiscovery =
            new NetworkConfigurationDiscoveryProcess(desc, serverAddress, stack);
        netConfigDiscovery.start();
        
        final StunDiscoveryReport report = 
            netConfigDiscovery.determineAddress(this);
        LOG.trace(report.getNatType());
        
        final StunAddress stunAddress = report.getPublicAddress();
        
        if (stunAddress != null)
            {
            // The address can be null if we're behind a UDP blocking 
            // firewall and therefore have no public address.
            final InetSocketAddress publicAddress = 
                report.getPublicAddress().getSocketAddress();
            //LOG.debug("Found public address in STUN report: " + 
              //  this.m_publicAddress);
            //final Preferences prefs = Preferences.userRoot();
            //prefs.put(PrefKeys.PUBLIC_URL, this.m_publicAddress.getHostName());
            }
        }

    public void mappedAddress(InetSocketAddress mappedAddress)
        {
        LOG.debug("Got address: "+mappedAddress);
        }
    
    }
