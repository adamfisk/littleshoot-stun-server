package org.lastbamboo.common.stun.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

public class StunClientIoHandler extends IoHandlerAdapter
    {
    
    private static final Log LOG = LogFactory.getLog(StunClientIoHandler.class);
    
    public void sessionOpened( IoSession session )
        {
        LOG.debug("Session opened...");
        // Set reader idle time to 10 seconds.
        // sessionIdle(...) method will be invoked when no data is read
        // for 10 seconds.
        session.setIdleTime( IdleStatus.READER_IDLE, 10 );
        }

    public void sessionClosed( IoSession session )
        {
        // Print out total number of bytes read from the remote peer.
        System.err.println( "Total " + session.getReadBytes() + " byte(s)" );
        }

    public void sessionIdle( IoSession session, IdleStatus status )
        {
            // Close the connection if reader is idle.
            if( status == IdleStatus.READER_IDLE )
                session.close();
        }

    public void messageReceived(final IoSession session, Object message )
        {
        ByteBuffer buf = ( ByteBuffer ) message;
        // Print out read buffer content.
        while( buf.hasRemaining() )
        {
            System.out.print( ( char ) buf.get() );
        }
        System.out.flush();
        }
    
    public void exceptionCaught( IoSession session, Throwable cause )
        {
            cause.printStackTrace();
            session.close();
        }
    
    }
