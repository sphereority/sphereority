package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class FrameRateCounter extends Widget
{
    private long timeStarted = -1, numRepaints;
    private boolean ready;
    
    public FrameRateCounter(int x, int y, int width, int height, Color c)
    {
        super(x, y, width, height, c);
        
        numRepaints = 0;
        ready = false;
    }
    
    public float getFrameRate()
    {
        return 1000.0f * (float)numRepaints / (System.currentTimeMillis() - timeStarted);
    }
    
    public void draw(Graphics2D g, int windowWidth, int windowHeight)
    {
        if (timeStarted < 0)
            timeStarted = System.currentTimeMillis();
        
        numRepaints ++;
        
        g.setColor(color);
        
        if (ready || System.currentTimeMillis() - timeStarted > 10000)
        {
            ready = true;
            
            GuiUtils.drawCenteredText(g, String.format("FPS: %.1f", getFrameRate()), x, y, width, height, 0.5f, 0.5f, 10);
        }
        else
        {
            GuiUtils.drawCenteredText(g, "Gathering data", x, y, width, height, 0.5f, 0.5f, 10);
        }
    }
}
