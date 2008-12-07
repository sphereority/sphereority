package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

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
            value = lastValue;
            fill = GuiUtils.getBoxShape(x, y, value*width/maxValue, height);
        }
        
        g.setColor(c);
        g.draw(outline);
        g.fill(fill);
    }
}
