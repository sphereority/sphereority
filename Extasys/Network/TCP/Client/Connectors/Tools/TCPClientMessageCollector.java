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
package Extasys.Network.TCP.Client.Connectors.Tools;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;

/**
 *
 * @author Nikos Siatras
 */
public class TCPClientMessageCollector
{

    private TCPConnector fMyConnector;
    private String fETXStr;
    private StringBuilder fIncomingDataBuffer;
    private int fIndexOfETX;

    public TCPClientMessageCollector(TCPConnector connector, char ETX)
    {
        Initialize(connector, String.valueOf(ETX));
    }

    public TCPClientMessageCollector(TCPConnector connector, String splitter)
    {
        Initialize(connector, splitter);
    }

    private void Initialize(TCPConnector connector, String splitter)
    {
        fMyConnector = connector;
        fETXStr = splitter;
        fIncomingDataBuffer = new StringBuilder();
    }

    public void AppendData(String data)
    {
        try
        {
            fIncomingDataBuffer.append(data);
            fIndexOfETX = fIncomingDataBuffer.indexOf(fETXStr);
            while (fIndexOfETX > -1)
            {
                fMyConnector.getMyExtasysTCPClient().OnDataReceive(fMyConnector, new DataFrame(fIncomingDataBuffer.substring(0, fIndexOfETX).getBytes()));
                fIncomingDataBuffer = fIncomingDataBuffer.delete(0, fIndexOfETX + 1);
            }
        }
        catch (Exception ex)
        {
        }
    }

    public void Dispose()
    {
        try
        {
            fIncomingDataBuffer = null;
            fETXStr = null;
            fMyConnector = null;
        }
        catch (Exception ex)
        {
        }
    }
}
