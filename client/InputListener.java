package client;

import common.Constants;
import java.awt.Component;
import java.awt.event.*;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a handy place to put keyboard/mouse event handling code
 * @author dvanhumb
 */
public class InputListener implements MouseListener, MouseMotionListener, KeyListener, KeyEventDispatcher, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	// Key event-related variables
	private boolean[] keysPressed;
	private int numKeysPressed;
	
	// Mouse-related variables
	private int mouseX, mouseY;
	private boolean mouseFiring, mouseFired;
	
	// Configuration-related variables
	private int keyLeft, keyRight, keyUp, keyDown;
	private int mouseFireButton;
	
	public InputListener()
	{
		// Set up keyboard variables
		keysPressed = new boolean[256];
		numKeysPressed = 0;
		
		// Set up mouse variables
		mouseFiring = mouseFired = false;
		
		// Default bindings:
//		keyLeft = KeyEvent.VK_LEFT;
//		keyRight = KeyEvent.VK_RIGHT;
//		keyUp = KeyEvent.VK_UP;
//		keyDown = KeyEvent.VK_DOWN;
		keyLeft = KeyEvent.VK_A;
		keyRight = KeyEvent.VK_D;
		keyUp = KeyEvent.VK_W;
		keyDown = KeyEvent.VK_S;
		mouseFireButton = MouseEvent.BUTTON1;
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
	
	/**
	 * Find out which key is currently being used for the Down movement
	 * @return	The keycode for Down from KeyEvent.VK_* 
	 */
	public int getKeyDown()
	{
		return keyDown;
	}

	public void setKeyDown(int keyDown)
	{
		this.keyDown = keyDown;
	}

	/**
	 * Find out which key is currently being used for the Left movement
	 * @return	The keycode for Left from KeyEvent.VK_* 
	 */
	public int getKeyLeft()
	{
		return keyLeft;
	}

	public void setKeyLeft(int keyLeft)
	{
		this.keyLeft = keyLeft;
	}

	/**
	 * Find out which key is currently being used for the Right movement
	 * @return	The keycode for Right from KeyEvent.VK_* 
	 */
	public int getKeyRight()
	{
		return keyRight;
	}

	public void setKeyRight(int keyRight)
	{
		this.keyRight = keyRight;
	}

	/**
	 * Find out which key is currently being used for the Up movement
	 * @return	The keycode for Up from KeyEvent.VK_* 
	 */
	public int getKeyUp()
	{
		return keyUp;
	}

	public void setKeyUp(int keyUp)
	{
		this.keyUp = keyUp;
	}
	
	/**
	 * Asks if the user has hit the fire button recently
	 * @return
	 */
	public boolean isButtonFiring()
	{
		boolean result = mouseFiring || mouseFired;
		mouseFired = false;
		return result;
	}
	
	public int getMousePosX()
	{
		return mouseX;
	}
	
	public int getMousePosY()
	{
		return mouseY;
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
	
	public void attachListeners(Window w)
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}
	
	public void detachListeners(Component c)
	{
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
		c.removeKeyListener(this);
	}
	
	public void detachListeners(Window w)
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
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
		updateMousePosition(e);
	}

	public void mouseEntered(MouseEvent e)
	{
		updateMousePosition(e);
	}

	public void mouseExited(MouseEvent e)
	{
		updateMousePosition(e);
	}

	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == mouseFireButton)
			mouseFiring = mouseFired = true;
		
		updateMousePosition(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == mouseFireButton)
			mouseFiring = false;
		
		updateMousePosition(e);
	}

	public void mouseDragged(MouseEvent e)
	{
		updateMousePosition(e);
	}

	public void mouseMoved(MouseEvent e)
	{
		updateMousePosition(e);
	}
	
	private void updateMousePosition(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
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

	public boolean dispatchKeyEvent(KeyEvent e)
	{
		if (e.getID() == KeyEvent.KEY_PRESSED)
			keyPressed(e);
		else if (e.getID() == KeyEvent.KEY_RELEASED)
			keyReleased(e);
		else if (e.getID() == KeyEvent.KEY_TYPED)
			keyTyped(e);
		
		return false;
	}
}
