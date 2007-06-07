package org.lastbamboo.common.stun.server.turn;

/**
 * Interface for the class that generates TURN ports within a specified
 * range, making sure the same port is not used for multiple servers on this
 * host.
 */
public interface RandomNonCollidingPortGenerator
    {

    /**
     * Creates a random port that does not collide with any existing port.
     * @return The new, randomly generated port.
     */
    int createRandomPort();

    /**
     * Removes the specified port from the allocated set.
     * @param port The port to remove.
     */
    void removePort(final int port);
    
    }
