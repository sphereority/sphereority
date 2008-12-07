package client;

import client.gui.*;
import common.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Vector;
import javax.swing.*;

/**
 * This class manages displaying the current play area
 * @author dvanhumb
 */
public class ClientViewArea extends JComponent implements MouseMotionListener, MouseListener, KeyListener, Constants, MapChangeListener {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	private static final long serialVersionUID = 23498751L;
	public static final int MAP_WIDTH = 16;
	public static final int MAP_HEIGHT = 16;
	public static final boolean DRAW_CENTER_DOT = false;

    public int framesPerSecond;	
	// Drawing-related variables
	protected boolean antialiasing;
	protected float scale;
	protected boolean drawMap;
	protected Point lastOffset;
	
	// Colour-defining variables
	
	// Gui-related stuff:
	protected Vector<Widget> widgetList;
	
	// Game-related stuff:
	protected LocalPlayer localPlayer;
	protected TrackingObject viewTracker;
	protected Map map;
	protected int mapWidth, mapHeight;
	protected GameEngine gameEngine;
	
	// Temporary testing stuff:
	
	public ClientViewArea(GameEngine engine)
	{
		gameEngine = engine;
		
        framesPerSecond = 0;
		Dimension d = new Dimension(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
		setMinimumSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		
		setBackground(Color.black);
		setForeground(STONE_COLOR);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		widgetList = new Vector<Widget>();
		scale = 50;
		
		antialiasing = false;
		drawMap = true;
		
		MapRadar radar = new MapRadar(5, -5, gameEngine, map);
		addWidget(radar);
		
		if (gameEngine != null)
		{
			gameEngine.addMapListener(this);
			gameEngine.addMapListener(radar);
		}
		else
			System.err.println("client.ClientViewArea.<init>: GameEngine not specified!");
		
		mapWidth = MAP_WIDTH;
		mapHeight = MAP_HEIGHT;
		
		lastOffset = new Point();
		
		setFocusable(true);
	}
	
	public Point getLastOffset()
	{
		return lastOffset;
	}
	
	public void setLocalPlayer(LocalPlayer p)
	{
		localPlayer = p;
		
//		if (viewTracker == null)
//		{
//			viewTracker = new TrackingObject(localPlayer);
//			gameEngine.addActor(viewTracker);
//		}
		
		repaint();
	}
	
	public LocalPlayer getLocalPlayer()
	{
		return localPlayer;
	}
	
	public void setMap(Map m)
	{
		map = m;
		if (this.isVisible())
			repaint();
		if (map != null)
		{
			mapWidth = map.getWidth();
			mapHeight = map.getHeight();
		}
		else
		{
			mapWidth = MAP_WIDTH;
			mapHeight = MAP_HEIGHT;
		}
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setDrawMap(boolean show)
	{
		drawMap = show;
		repaint();
	}
	
	public boolean getDrawMap()
	{
		return drawMap;
	}
	
	public void addWidget(Widget w)
	{
		widgetList.add(w);
	}
	
	public void removeWidget(Widget w)
	{
		widgetList.remove(w);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		if (antialiasing)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		Rectangle clip = g2.getClipBounds(); // The clipping bounds, so we don't draw stuff over again
		int offset_x=0, offset_y=0;
		offset_x = getWidth() / 2;
		offset_y = getHeight() / 2;
		
		// Save temporary copies of parameters changed
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		
		// Draw the background
		g2.setColor(getBackground());
		g2.fill(clip);
		
		if (viewTracker != null)
		{
			offset_x -= Math.round(viewTracker.getPosition().getX()*scale);
			offset_y -= Math.round(viewTracker.getPosition().getY()*scale);
		}
		
		lastOffset.x = offset_x;
		lastOffset.y = offset_y;
		
		g2.translate(offset_x, offset_y);		
		
		// Draw all actors:
//		for (Actor a : gameEngine.actorList)
//			a.draw(g2, scale);
		for (Actor a : gameEngine.bulletList)
			a.draw(g2, scale);
		for (Actor a : gameEngine.playerList)
			a.draw(g2, scale);
		
		// Draw the walls
		if (map != null && drawMap)
		{
			int left, right, top, bottom;
			left = Math.round((clip.x - offset_x) / scale - 0.5f);
			right = Math.round((clip.x + clip.width - offset_x) / scale);
			top = Math.round((clip.y - offset_y) / scale - 0.5f);
			bottom = Math.round((clip.y + clip.height - offset_y) / scale);
			
			left = Math.max(0, Math.min(map.getWidth()-1, left));
			right = Math.max(0, Math.min(map.getWidth()-1, right));
			top = Math.max(0, Math.min(map.getHeight()-1, top));
			bottom = Math.max(0, Math.min(map.getHeight()-1, bottom));
			
			g2.setColor(getForeground());
			for (int x=left; x <= right; x++)
				for (int y=top; y <= bottom; y++)
				{
					if (map.isWall(x, y))
						g2.fillRect(Math.round(x*scale), Math.round(y*scale), Math.round(scale), Math.round(scale));
				}
		} // end draw map
		
		// Draw everybody's labels
		for (Player p : gameEngine.playerList)
			p.drawLabel(g2, scale);
		
		// Restore the view so the widgets are in the right spot
		g2.setTransform(oldTransform);
		
		// Mark the center of the window
		if (DRAW_CENTER_DOT)
		{
			g2.setColor(Color.red);
			g2.fillRect(getWidth()/2 - 1, getHeight()/2 - 1, 3, 3);
		}
		
		final int width = getWidth(), height = getHeight();
		// Draw the widgets
		for (Widget w : widgetList)
			w.draw(g2, width, height);
		
		// Restore all parameters changed
		g2.setColor(oldColor);
        
	}
	
	// Called when the mouse is moved with at least one button down
	public void mouseDragged(MouseEvent e)
	{
		int width = getWidth(), height = getHeight(), x = e.getX(), y = e.getY();
		for (Widget w : widgetList)
		{
			if (!(w instanceof InteractiveWidget)) continue;
			InteractiveWidget iw = (InteractiveWidget) w;
			if (iw.containsFixed(x, y, width, height))
			{
				if (iw.getState() == InteractiveWidget.STATE_UNHOVERED)
				{
					iw.setState(InteractiveWidget.STATE_HOVERED);
					repaint(iw.getFixedBounds(width, height));
				}
			}
			else
			{
				if (iw.getState() == InteractiveWidget.STATE_HOVERED)
				{
					iw.setState(InteractiveWidget.STATE_UNHOVERED);
					repaint(iw.getFixedBounds(width, height));
				}
			}
		} // end for (w in widgetList)
	} // end mouseDragged()
	
	public void mouseMoved(MouseEvent e)
	{
		int width = getWidth(), height = getHeight(), x = e.getX(), y = e.getY();
		for (Widget w : widgetList)
		{
			if (!(w instanceof InteractiveWidget)) continue;
			InteractiveWidget iw = (InteractiveWidget) w;
			if (iw.containsFixed(x, y, width, height))
			{
				if (iw.getState() != InteractiveWidget.STATE_HOVERED)
				{
					iw.setState(InteractiveWidget.STATE_HOVERED);
					repaint(iw.getFixedBounds(width, height));
				}
			}
			else
			{
				if (iw.getState() != InteractiveWidget.STATE_READY)
				{
					iw.setState(InteractiveWidget.STATE_READY);
					repaint(iw.getFixedBounds(width, height));
				}
			}
		} // end for (Widget)
	} // end mouseMouse()
	
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{
		
	}
	
	// Called when the mouse button is released
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1) return;
		
		for (Widget w : widgetList)
		{
			if (!(w instanceof InteractiveWidget)) continue;
			InteractiveWidget iw = (InteractiveWidget) w;
			if (iw.getState() == InteractiveWidget.STATE_HOVERED) iw.trigger(e.getButton());
			iw.setState(InteractiveWidget.STATE_READY);
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) // TODO: replace this with a quit call so we go back to the login screen
		{
			if (gameEngine != null)
				gameEngine.gameOver();
			else
				System.exit(0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_INSERT)
		{
			antialiasing = !antialiasing;
			repaint();
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void mapChanged(Map newMap)
	{
		setMap(newMap);
	}

	public float getScale()
	{
		return scale;
	}
} // end ClientViewArea clas
