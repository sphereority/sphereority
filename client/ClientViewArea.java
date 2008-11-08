package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import java.util.Vector;

import client.audio.*;
import client.gui.*;
import common.*;

/**
 * This class manages displaying the current play area
 * @author dvanhumb
 */
public class ClientViewArea extends JComponent implements MouseMotionListener, MouseListener, KeyListener, ActionListener, Constants
{
	private static final long serialVersionUID = 23498751L;
	public static int TIMER_TICK = 75;
	public static int MAP_WIDTH = 16;
	public static int MAP_HEIGHT = 16;
	
	// Drawing-related variables
	protected boolean antialiasing;
	protected float scale;
	// Colour-defining variables
	protected Color playerColor;
	
	// Gui-related stuff:
	protected Vector<Widget> widgetList;
	
	// Game-related stuff:
	protected LocalPlayer localPlayer;
	protected WeightedPosition viewTracker;
	protected Vector<Actor> actorList;
	protected Map map;
	protected int mapWidth, mapHeight;
	
	// Temporary testing stuff:
	protected Timer gameTimer;
	protected long lastTime;
	protected boolean[] keysPressed;
	protected SoundEffect soundBump;
	protected GameSoundSystem gameSoundSystem;
	
	public ClientViewArea()
	{
		Dimension d = new Dimension(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
		setMinimumSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		
		setBackground(Color.black);
		setForeground(new Color(0.8f, 0.4f, 0.2f));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		widgetList = new Vector<Widget>();
		playerColor = Color.green;
		scale = 50;
		
		gameTimer = new Timer(TIMER_TICK, this);
		gameTimer.start();
		lastTime = System.currentTimeMillis();
		
		keysPressed = new boolean[1024];
		antialiasing = false;
		
		actorList = new Vector<Actor>();
		
		mapWidth = MAP_WIDTH;
		mapHeight = MAP_HEIGHT;
		
		gameSoundSystem = new GameSoundSystem();
		soundBump = gameSoundSystem.loadSoundEffect(SOUND_BUMP);
	}
	
	public void setLocalPlayer(LocalPlayer p)
	{
		if (!actorList.contains(p))
			actorList.add(p);
		localPlayer = p;
		if (viewTracker == null)
			viewTracker = new WeightedPosition(localPlayer.getX(), localPlayer.getY());
		viewTracker.setTarget(localPlayer);
		repaint();
	}
	
	public LocalPlayer getLocalPlayer()
	{
		return localPlayer;
	}
	
	public Color getPlayerColor()
	{
		return playerColor;
	}
	
	public void setPlayerColor(Color color)
	{
		playerColor = color;
		repaint();
	}
	
	public void setMap(Map m)
	{
		map = m;
		if (this.isVisible())
			repaint();
		if (map != null)
		{
			mapWidth = map.getXSize();
			mapHeight = map.getYSize();
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
			offset_x -= Math.round(viewTracker.getX()*scale);
			offset_y -= Math.round(viewTracker.getY()*scale);
		}
		
		g2.translate(offset_x, offset_y);		
		
		// Draw all actors:
		for (Actor a : actorList)
			a.draw(g2, scale);
		
		// Draw the player
		/*if (localPlayer != null)
		{
			g2.setColor(playerColor);
			GuiUtils.drawFilledOctagon(g2, Math.round(localPlayer.getX()*scale), Math.round(localPlayer.getY()*scale), scale*PLAYER_SIZE);
		}*/
		
		// Draw the walls
		if (map != null)
		{
			int left, right, top, bottom;
			left = Math.round((clip.x - offset_x) / scale - 0.5f);
			right = Math.round((clip.x + clip.width - offset_x) / scale);
			top = Math.round((clip.y - offset_y) / scale - 0.5f);
			bottom = Math.round((clip.y + clip.height - offset_y) / scale);
			
			left = Math.max(0, Math.min(map.getXSize()-1, left));
			right = Math.max(0, Math.min(map.getXSize()-1, right));
			top = Math.max(0, Math.min(map.getYSize()-1, top));
			bottom = Math.max(0, Math.min(map.getYSize()-1, bottom));
			
			g2.setColor(getForeground());
			for (int x=left; x <= right; x++)
				for (int y=top; y <= bottom; y++)
				{
					if (map.isWall(x, y))
						g2.fillRect(Math.round(x*scale), Math.round(y*scale), Math.round(scale), Math.round(scale));
				}
		} // end draw map
		
		// Restore the view so the widgets are in the right spot
		g2.setTransform(oldTransform);
		
		// TEMP: Mark the center of the window
		g2.setColor(Color.red);
		g2.fillRect(getWidth()/2 - 1, getHeight()/2 - 1, 3, 3);
		
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
		else if (e.getKeyCode() == KeyEvent.VK_INSERT)
		{
			antialiasing = !antialiasing;
			repaint();
		}
		else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			soundBump.play();
		
		keysPressed[e.getKeyCode()] = true;
	}
	
	public void keyReleased(KeyEvent e)
	{
		keysPressed[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(gameTimer))
		{
			long thisTime = System.currentTimeMillis();
			float dTime = 0.001f * (thisTime - lastTime);
			boolean repaint = false;
			
			for (Actor a : actorList)
				if (a.animate(dTime))
					repaint = true;
			
			if (viewTracker != null && viewTracker.animate(dTime)) repaint = true;
			
			if (repaint) repaint();
			lastTime = thisTime;
		}
	} // end actionPerformed()
} // end ClientViewArea clas
