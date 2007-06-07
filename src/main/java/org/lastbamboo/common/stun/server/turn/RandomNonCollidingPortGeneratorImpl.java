package org.lastbamboo.common.stun.server.turn;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates random ports in an acceptible range (not restricted or generally
 * assigned) that don't collide.
 */
public final class RandomNonCollidingPortGeneratorImpl implements
    RandomNonCollidingPortGenerator
    {

    /**
     * Logger for this class.
     */
    private static final Log LOG = 
        LogFactory.getLog(RandomNonCollidingPortGeneratorImpl.class);
    
    /**
     * Used to create random ports.
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    
    /**
     * Set of <code>Integer</code>s for ports we've allocated.
     */
    private final Set<Integer> m_allocatedPorts = 
        Collections.synchronizedSet(new HashSet<Integer>());
    
    /**
     * TURN must allocate ports above 49151.  This is actually from an older 
     * TURN draft, but it seems prudent to avoid colliding with assigned ports 
     * below this range.
     */
    private static final int MINIMUM_PORT = 49151;
    
    /**
     * The number of available ports above the minimum port.
     */
    private static final int AVAILABLE_PORTS = 65534 - MINIMUM_PORT;
    
    public int createRandomPort()
        {
        LOG.trace("Creating random port");
        while (true)
            {
            final Integer randomPort = 
                new Integer(MINIMUM_PORT + RANDOM.nextInt(AVAILABLE_PORTS));
          
            if (!this.m_allocatedPorts.contains(randomPort))
                {
                this.m_allocatedPorts.add(randomPort);
                return randomPort.intValue();
                }
            }
        }

    public void removePort(final int port)
        {
        this.m_allocatedPorts.remove(new Integer(port));
        }

    }
