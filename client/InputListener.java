package client;

import java.awt.Component;
import java.awt.event.*;

/**
 * This class provides a handy place to put keyboard/mouse event handling code
 * @author dvanhumb
 */
public class InputListener implements MouseListener, MouseMotionListener, KeyListener
{
	protected boolean[] keysPressed;
	protected int numKeysPressed;
	
	protected int keyLeft, keyRight, keyUp, keyDown;
	
	public InputListener()
	{
		keysPressed = new boolean[256];
		numKeysPressed = 0;
		
		// Default key bindings:
		keyLeft = KeyEvent.VK_LEFT;
		keyRight = KeyEvent.VK_RIGHT;
		keyUp = KeyEvent.VK_UP;
		keyDown = KeyEvent.VK_DOWN;
	}
	
	/**
	 * This method returns the number of keys that are currently pressed
	 * @return	The number of keys currently held
	 */
	public int getNumKeysPressed()
	{
		return numKeysPressed;
	}
	
	/**
	 * Find out if a particular key is currently pressed
	 * @param keyCode	The code (in KeyEvent.VK_*) that we want to know about
	 * @return			Whether the key is currently pressed
	 */
	public boolean isKeyPressedRaw(int keyCode)
	{
		return keysPressed[keyCode];
	}
	
	/**
	 * Returns true when the key used for left movement is pressed
	 * @return
	 */
	public boolean isLeftKeyPressed()
	{
		return keysPressed[keyLeft];
	}
	
	/**
	 * Returns true when the key used for right movement is pressed
	 * @return
	 */
	public boolean isRightKeyPressed()
	{
		return keysPressed[keyRight];
	}
	
	/**
	 * Returns true when the key used for up movement is pressed
	 * @return
	 */
	public boolean isUpKeyPressed()
	{
		return keysPressed[keyUp];
	}
	
	/**
	 * Returns true when the key used for down movement is pressed
	 * @return
	 */
	public boolean isDownKeyPressed()
	{
		return keysPressed[keyDown];
	}
	
	public int getKeyDown()
	{
		return keyDown;
	}

	public void setKeyDown(int keyDown)
	{
		this.keyDown = keyDown;
	}

	public int getKeyLeft()
	{
		return keyLeft;
	}

	public void setKeyLeft(int keyLeft)
	{
		this.keyLeft = keyLeft;
	}

	public int getKeyRight()
	{
		return keyRight;
	}

	public void setKeyRight(int keyRight)
	{
		this.keyRight = keyRight;
	}

	public int getKeyUp()
	{
		return keyUp;
	}

	public void setKeyUp(int keyUp)
	{
		this.keyUp = keyUp;
	}
	
	/**
	 * This method attaches all required event listeners to the specified component 
	 * @param c		The component to attach listeners to
	 */
	public void attachListeners(Component c)
	{
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addKeyListener(this);
	}
	
	public void detachListeners(Component c)
	{
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
		c.removeKeyListener(this);
	}

	/* *********************************************** *
	 * These methods are for handling input events and *
	 * can be safely ignored by game engine code(rs)   *
	 * *********************************************** */
	
	/* *********************************** *
	 * These are the mouse-related methods * 
	 * *********************************** */
	
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

	public void mouseReleased(MouseEvent e)
	{
		
	}

	public void mouseDragged(MouseEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		
	}
	
	/* ******************* *
	 * Key-related methods *
	 * ******************* */
	
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (keysPressed[code])
			return;
		
		keysPressed[code] = true;
		numKeysPressed ++;
	}

	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (!keysPressed[code])
			return;
		
		keysPressed[code] = false;
		numKeysPressed --;
	}
	
	// Don't need this one
	public void keyTyped(KeyEvent e) { }
}
