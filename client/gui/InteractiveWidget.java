package client.gui;

public abstract class InteractiveWidget extends Widget {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public static final int STATE_READY = 0x501;
	public static final int STATE_HOVERED = 0x502;
	public static final int STATE_PRESSED = 0x503;
	public static final int STATE_UNHOVERED = 0x504;
	
	protected int cur_state;
	protected String label;
	protected float fontSize;
	
	public InteractiveWidget(int x, int y, int width, int height)
	{
		this(x, y, width, height, null);
	}
	
	public InteractiveWidget(int x, int y, int width, int height, String label)
	{
		super(x, y, width, height);
		cur_state = STATE_READY;
		setLabel(label);
		fontSize = -1;
	}
	
	public int getState()
	{
		return cur_state;
	}
	
	public void setState(int state)
	{
		cur_state = state;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public float getFontSize()
	{
		return fontSize;
	}
	
	public void setFontSize(float size)
	{
		fontSize = size;
	}
	
	/**
	 * This function gets called when this widget gets clicked on
	 * @param buttons	The mouse buttons that were pressed 
	 */
	public abstract void trigger(int buttons);
}
