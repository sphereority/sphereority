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
package Extasys.Network.TCP.Server.Listener.Packets;

import Extasys.Network.TCP.Server.Listener.TCPClientConnection;
import Extasys.ManualResetEvent;

/**
 *
 * @author Nikos Siatras
 */
public class MessageCollectorTCPClientConnectionPacket implements Runnable
{

    public ManualResetEvent fDone = new ManualResetEvent(false);
    private TCPClientConnection fClient;
    private String fData;
    private MessageCollectorTCPClientConnectionPacket fPreviousPacket;
    public boolean fCancel = false;

    /**
     * Constructs a new (incoming) message collector packet.
     * 
     * Use this class to receive data from a client.
     * This is an incoming message that will remain in the servers thread pool
     * as a job for the thread pool workers.
     * 
     * @param TCPClientConnection is the packets TCPClientConnection.
     * @param data is the string received.
     * @param previousPacket is the previous message collector packet of the TCPClientConnection.
     */
    public MessageCollectorTCPClientConnectionPacket(TCPClientConnection client, String data, MessageCollectorTCPClientConnectionPacket previousPacket)
    {
        fClient = client;
        fData = data;
        fPreviousPacket = previousPacket;

        fClient.getMyExtasysTCPServer().getMyThreadPool().execute(this);
    }

    /**
     * Cancel this message collector packet.
     * 
     * By calling this method this and all the previous message collector packets that 
     * are stored in the thread pool will be canceled.
     * Call this method for the last message collector of the TCPClientConnection
     * when the TCPClientConnection disconnects.
     * 
     */
    public void Cancel()
    {
        fCancel = true;
        fDone.Set();
    }

    @Override
    public void run()
    {
        try
        {
            if (fPreviousPacket == null)
            {
                fClient.getMyMessageCollector().AppendData(fData);
            }
            else
            {
                fPreviousPacket.fDone.WaitOne();

                if (!fCancel && !fPreviousPacket.fCancel)
                {
                    fClient.getMyMessageCollector().AppendData(fData);
                }
                else
                {
                    fCancel = true;
                }

                fPreviousPacket = null;
            }
        }
        catch (Exception ex)
        {
            
        }

        fDone.Set();
    }

    /** 
     * Returns the data of this packet.
     * 
     * @return the data of this packet.
     */
    public String getData()
    {
        return fData;
    }

}
