package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class ProgressMeter extends Widget
{
    private int value, maxValue, lastValue;
    private Color c;
    
    private Polygon outline, fill;
    
    public ProgressMeter(int x, int y, int width, int height, Color c)
    {
        super(x, y, width, height);
        
        value = 0;
        maxValue = 100;
        lastValue = -1;
        this.c = c;
    }
    
    public ProgressMeter(int x, int y, int width, int height, Color c, int value, int maxValue)
    {
        super(x, y, width, height);
        
        this.value = value;
        this.maxValue = maxValue;
        lastValue = -1;
        this.c = c;
    }
    
    public int getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public void draw(Graphics2D g, int windowWidth, int windowHeight)
    {
        if (outline == null)
            outline = GuiUtils.getBoxShape(x, y, width, height);
        
        if (fill == null || value != lastValue)
        {
            lastValue = value;
            fill = GuiUtils.getBoxShape(x+2, y+2, Math.max(0, value)*(width-4)/maxValue, height-4);
        }
        
        int px = getFixedX(windowWidth), py = getFixedY(windowHeight);
        AffineTransform t = null;
        if (px != x || py != y)
        {
            t = g.getTransform();
            if (px != x)
                g.translate(windowWidth-width, 0);
            if (py != y)
                g.translate(0, windowHeight-height);
        }
        
        g.setColor(c);
        g.draw(outline);
        g.fill(fill);
        
        g.setColor(Color.white);
        GuiUtils.drawCenteredText(g, Integer.toString(value), x + width/2, y + height/2, 0.5f, 0.5f, 12);
        
        if (t != null)
            g.setTransform(t);
    }
}
