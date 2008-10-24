package client.gui;

public abstract class InteractiveWidget extends Widget
{
	public static final int STATE_READY = 0x501;
	public static final int STATE_HOVERED = 0x502;
	public static final int STATE_PRESSED = 0x503;
	public static final int STATE_UNHOVERED = 0x504;
	
	protected int cur_state;
	
	public InteractiveWidget(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		cur_state = STATE_READY;
	}
	
	public int getState()
	{
		return cur_state;
	}
	
	public void setState(int state)
	{
		cur_state = state;
	}
	
	/**
	 * This function gets called when this widget gets clicked on
	 * @param buttons	The mouse buttons that were pressed 
	 */
	public abstract void trigger(int buttons);
}
