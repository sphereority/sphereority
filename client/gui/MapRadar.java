package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import common.Map;
import common.MapChangeListener;

public class MapRadar extends Widget implements MapChangeListener
{
	public static final Color RADAR_COLOR = new Color(180, 180, 180, 127);
	public static final Color RADAR_BACKGROUND_COLOR = new Color(200, 200, 200, 60);
	
	protected Map map;
	
	public MapRadar(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
	
	public MapRadar(int x, int y, int width, int height, Color c)
	{
		super(x, y, width, height, c);
	}
	
	public MapRadar(int x, int y, int width, int height, Map m)
	{
		super(x, y, width, height);
		setMap(m);
	}
	
	public MapRadar(int x, int y, int width, int height, Color c, Map m)
	{
		super(x, y, width, height, c);
		setMap(m);
	}
	 
	
	public void setMap(Map map)
	{
		this.map = map;
		width = map.getXSize();
		height = map.getYSize();
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void draw(Graphics2D g, int windowWidth, int windowHeight)
	{
		if (map == null)
			return;
		
		g.setColor(RADAR_BACKGROUND_COLOR);
		g.fill(getFixedBounds(windowWidth, windowHeight));
		
		g.setColor(RADAR_COLOR);
	}

	public void mapChanged(Map newMap)
	{
		setMap(newMap);
	}
}
