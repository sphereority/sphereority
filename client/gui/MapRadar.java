package client.gui;

import client.*;
import common.*;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapRadar extends Widget implements MapChangeListener, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public static final Color RADAR_COLOR = new Color(120, 120, 120);
	public static final Color RADAR_BACKGROUND_COLOR = new Color(200, 200, 200, 60);
	
	protected int scale;
	protected boolean showMap;
	protected boolean showName;
	protected boolean clipping;
	
	protected int winWidth = GAME_WINDOW_WIDTH, winHeight = GAME_WINDOW_HEIGHT;
	
	protected Map map;
	protected GameEngine engine;
	
	public MapRadar(int x, int y, GameEngine engine, Map map)
	{
		super(x, y, 10, 10);
		setMap(map);
		setup();
		this.engine = engine;
	}
	
	public MapRadar(int x, int y, GameEngine engine, Map map, Color c)
	{
		super(x, y, 10, 10, c);
		setMap(map);
		setup();
		this.engine = engine;
	}

	private void setup()
	{
		scale = 4;
		showName = true;
		showMap = true;
	}
	
	public void setShowName(boolean flag)
	{
		showName = flag;
	}
	
	public boolean getShowName()
	{
		return showName;
	}
	
	public void setMap(Map map)
	{
		if (map == null)
			return;
		
		this.map = map;
		if (winWidth < 0)
			scale = 1;
		else
		{
			scale = 16;
			while (RADAR_SIZE * scale * map.getWidth() > winWidth || RADAR_SIZE * scale * map.getHeight() > winHeight)
			{
				scale --;
				if (scale < 1)
				{
					scale = 1;
					clipping = true;
					break;
				}
			}
		}
		width = scale * map.getWidth();
		height = scale * map.getHeight();
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void draw(Graphics2D g, int windowWidth, int windowHeight)
	{
		if (map == null || !showMap)
			return;
		
		winWidth = windowWidth;
		winHeight = windowHeight;
		
		// Draw background
//		g.setColor(RADAR_BACKGROUND_COLOR);
//		g.fill(getFixedBounds(windowWidth, windowHeight));
		
		// Draw walls
		g.setColor(RADAR_COLOR);
		final int offsetX = getFixedX(windowWidth), offsetY = getFixedY(windowHeight); 
		for (int x=0; x < map.getWidth(); x++)
			for (int y=0; y < map.getHeight(); y++)
			{
				if (map.isWall(x, y))
					g.fillRect(scale * x + offsetX,
					           scale * y + offsetY,
					           scale, scale);
			}
		
		// Draw players:
		final LocalPlayer localPlayer = engine.getLocalPlayer();
		final int localID = localPlayer.getPlayerID();
		float time;
		int px, py;
		for (Player p : engine.playerList)
		{
			// The local player
			if (p.getPlayerID() == localID || p.equals(localPlayer))
				g.setColor(Color.green);
			// A player with no team
			else if (p.getTeam() != TEAM_A && p.getTeam() != TEAM_B)
			{
				time = p.getTimeSinceLastSound();
				if (time > BLIP_TIME)
					continue;
				g.setColor(GuiUtils.modulateColor(TEAMLESS_COLOR, 1 - (float)time/BLIP_TIME));
			}
			// A player on the same team
			else if (p.getTeam() == localPlayer.getTeam())
				g.setColor(Color.blue);
			// An enemy player
			else
			{
				time = p.getTimeSinceLastSound();
				if (time > BLIP_TIME)
					continue;
				g.setColor(GuiUtils.modulateColor(Color.red, 1 - (float)time/BLIP_TIME));
			}
			
			px = Math.round(p.getPosition().getX() * scale) + offsetX;
			py = Math.round(p.getPosition().getY() * scale) + offsetY;
			px = Math.min(getFixedX(windowWidth) + width, Math.max(getFixedX(windowWidth), px));
			py = Math.min(getFixedY(windowHeight) + height, Math.max(getFixedY(windowHeight), py));
			
			g.fillRect(px - 1, py - 1, 3, 3);
		} // end if draw players
		
		if (showName)
		{
			String name = map.getName();
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(name, g);
			
			int xpos = x, ypos = y + height;
			if (x < 0)
				xpos = (int)Math.round(windowWidth - bounds.getWidth() + x - width - bounds.getX());
			if (y < 0)
				ypos = (int)Math.round(windowHeight - bounds.getHeight() + y - height - bounds.getY());
			
			g.setColor(Color.yellow);
			g.drawString(name, xpos, ypos);
		}
	} // end draw()

	public void mapChanged(Map newMap)
	{
		setMap(newMap);
	}

	public boolean isShowMap()
	{
		return showMap;
	}

	public void setShowMap(boolean showMap)
	{
		this.showMap = showMap;
	}
}
