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
package Extasys.Network.UDP.Client.Connectors;

import Extasys.Network.UDP.Client.Connectors.Packets.*;
import Extasys.Network.UDP.Client.ExtasysUDPClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Nikos Siatras
 */
public class MulticastConnector extends UDPConnector
{
    /**
     * Constructs a new MulticastConnector.
     * @param myClient is the connectors main Extasys UDP Client.
     * @param name is the name of the connector.
     * @param readBufferSize is the maximum number of bytes the socket can read at a time.
     * @param readTimeOut is the maximum time in milliseconds in wich a datagram packet can be received. Set to 0 for no time-out.
     * @param serverIP is the server's ip address the connector will use to send data.
     * @param serverPort is the server's udp port.
     */
    public MulticastConnector(ExtasysUDPClient myClient, String name, int readBufferSize, int readTimeOut, InetAddress serverIP, int serverPort)
    {
        super(myClient,name,readBufferSize,readTimeOut,serverIP,serverPort);
    }

    /**
     * Start the multicast connector.
     */
    @Override
    public void Start() throws SocketException, Exception
    {
        if (!fActive)
        {
            try
            {
                fActive = true;
                try
                {
                    fSocket = new MulticastSocket(fServerPort);
                    ((MulticastSocket)fSocket).joinGroup(fServerIP); 
                }
                catch (SocketException ex)
                {
                    fActive = false;
                    throw ex;
                }

                fSocket.setReceiveBufferSize(fReadBufferSize);
                fSocket.setSendBufferSize(fReadBufferSize);
                
                fLastIncomingPacket = null;
                fLastOutgoingPacket = null;

                if (fReadTimeOut > 0)
                {
                    fSocket.setSoTimeout(fReadTimeOut);
                }

                try
                {
                    fReadDataThread = new ReadIncomingData(this);
                    fReadDataThread.start();
                }
                catch (Exception ex)
                {
                    throw ex;
                }
            }
            catch (SocketException ex)
            {
                Stop();
                throw ex;
            }
        }
    }
}