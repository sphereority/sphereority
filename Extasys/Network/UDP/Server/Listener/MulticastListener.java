/*Copyright (c) 2008 Nikos Siatras

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/
package Extasys.Network.UDP.Server.Listener;

//import Extasys.Network.UDP.Server.Listener.Packets.*;
import Extasys.Network.UDP.Server.ExtasysUDPServer;
//import java.io.IOException;
//import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Nikos Siatras
 */
public class MulticastListener extends UDPListener
{    
    /**
     * Constructs a new Multicast Listener.
     * @param myUDPServer is the udp listener's main UDP server.
     * @param name is the name of the listener.
     * @param ipAddress is the listener's IP address.
     * @param port is the listener's UDP port.
     * @param readBufferSize is the read buffer size of the listener.
     * @param readDataTimeOut is the maximum time in milliseconds in wich a datagram packet can be received. Set to 0 for no time-out.
     */
    public MulticastListener(ExtasysUDPServer myServer, String name, InetAddress ipAddress, int port, int readBufferSize, int readDataTimeOut)
    {
        super(myServer,name,ipAddress,port,readBufferSize,readDataTimeOut);
    }

    /**
     * Start the multicast connector.
     */
    @Override
    public void Start() throws SocketException
    {
        Stop();
        try
        {
            fSocket = new MulticastSocket(fPort);
            fSocket.setReceiveBufferSize(fReadBufferSize);
            fSocket.setSendBufferSize(fReadBufferSize);
            ((MulticastSocket)fSocket).joinGroup(fIPAddress);

            if (fReadDataTimeOut > 0)
            {
                fSocket.setSoTimeout(fReadDataTimeOut);
            }

            fLastIncomingPacket = null;
            fLastOutgoingPacket = null;

            fActive = true;
            fReadIncomingDataThread = new ReadIncomingDataThread(this);
            fReadIncomingDataThread.start();
        }
        catch (SocketException socketException)
        {
            fActive = false;
            Stop();
            throw socketException;
        }
        catch (Exception ex) {
            fActive = false;
            Stop();
            throw new SocketException("Problem" + ex.getMessage());
        }
    }
}