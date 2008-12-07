package client.gui;

import common.Constants;
import java.awt.*;
import java.awt.geom.*;
import java.util.logging.Logger;

/**
 * This class has static methods for drawing common aspects of the user interface
 * @author Daryl Van Humbeck
 */
public final class GuiUtils implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public static final int BEVEL_CORNER_TOP_LEFT = 0xf01;
	public static final int BEVEL_CORNER_TOP_RIGHT = 0xf02;
	public static final int BEVEL_CORNER_BOTTOM_RIGHT = 0xf04;
	public static final int BEVEL_CORNER_BOTTOM_LEFT = 0xf08;
	public static final int DEFAULT_BEVEL_CORNERS = BEVEL_CORNER_TOP_LEFT | BEVEL_CORNER_BOTTOM_RIGHT;
	public static final int DEFAULT_BEVEL_AMOUNT = 5;
	
	protected static Polygon octagon = null;
	
	/**
	 * Draw text centered on a point
	 * @param g			The Graphics context to use
	 * @param text		The text to draw
	 * @param x			The left side of the "bounding" box
	 * @param y			The top side of the "bounding" box
	 * @param align_x	The horizontal alignment of the text
	 * @param align_y	The vertical alignment of the text
	 * @param font_size	The font size to use
	 */
	public static void drawCenteredText(Graphics2D g, String text, int x, int y, float align_x, float align_y, float font_size)
	{
		drawCenteredText(g, text, x, y, 0, 0, align_x, align_y, font_size);
	}
	
	/**
	 * Draw text centered in a box
	 * @param g			The Graphics context to use
	 * @param text		The text to draw
	 * @param x			The left side of the "bounding" box
	 * @param y			The top side of the "bounding" box
	 * @param width		The width of the "bounding" box
	 * @param height	The height of the "bounding" box
	 * @param align_x	The horizontal alignment of the text
	 * @param align_y	The vertical alignment of the text
	 * @param font_size	The font size to use
	 */
	public static void drawCenteredText(Graphics2D g, String text, int x, int y, int width, int height, float align_x, float align_y, float font_size)
	{
		Font oldFont = null;
		if (font_size > 0)
		{
			oldFont = g.getFont();
			g.setFont(g.getFont().deriveFont(font_size));
		}
		
		Rectangle2D rect = g.getFontMetrics().getStringBounds(text, g);
		int x2 = (int)Math.round(x + align_x*(width - rect.getWidth()) - rect.getX());
		int y2 = (int)Math.round(y + align_y*(height - rect.getHeight()) - rect.getY());
		
		g.drawString(text, x2, y2);
		
		if (oldFont != null)
			g.setFont(oldFont);
	}
	
	/**
	 * Draw the outline of a box, with some of the edges beveled
	 * @param g			The graphics context to use
	 * @param x			The left side of the box
	 * @param y			The top of the box
	 * @param width		The box's width
	 * @param height	The box's height
	 */
	public static void drawOutlinedBox(Graphics2D g, int x, int y, int width, int height)
	{
		Polygon p = new Polygon();
		
		int bevel_amount = DEFAULT_BEVEL_AMOUNT;
		if (width < bevel_amount*2)
			bevel_amount = width/2;
		if (height < bevel_amount*2)
			bevel_amount = height/2;
		
		// Top left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_LEFT) == BEVEL_CORNER_TOP_LEFT)
		{
			p.addPoint(x, y + bevel_amount);
			p.addPoint(x + bevel_amount, y);
		}
		else
			p.addPoint(x, y);
		// Top right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_RIGHT) == BEVEL_CORNER_TOP_RIGHT)
		{
			p.addPoint(x + width - bevel_amount, y);
			p.addPoint(x + width, y + bevel_amount);
		}
		else
			p.addPoint(x + width, y);
		// Bottom right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_RIGHT) == BEVEL_CORNER_BOTTOM_RIGHT)
		{
			p.addPoint(x + width, y - bevel_amount + height);
			p.addPoint(x + width - bevel_amount, y + height);
		}
		else
			p.addPoint(x + width, y + height);
		// Bottom left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_LEFT) == BEVEL_CORNER_BOTTOM_LEFT)
		{
			p.addPoint(x + bevel_amount, y + height);
			p.addPoint(x, y + height - bevel_amount);
		}
		else
			p.addPoint(x, y + height);
		
		g.draw(p);
	}
	
	/**
	 * Draw a filled box, with some of the edges beveled
	 * @param g			The graphics context to use
	 * @param x			The left side of the box
	 * @param y			The top of the box
	 * @param width		The box's width
	 * @param height	The box's height
	 */
	public static void drawFilledBox(Graphics2D g, int x, int y, int width, int height)
	{
		Polygon p = new Polygon();
		
		int bevel_amount = DEFAULT_BEVEL_AMOUNT;
		if (width < bevel_amount*2)
			bevel_amount = width/2;
		if (height < bevel_amount*2)
			bevel_amount = height/2;
		
		// Top left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_LEFT) == BEVEL_CORNER_TOP_LEFT)
		{
			p.addPoint(x, y + bevel_amount);
			p.addPoint(x + bevel_amount, y);
		}
		else
			p.addPoint(x, y);
		// Top right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_RIGHT) == BEVEL_CORNER_TOP_RIGHT)
		{
			p.addPoint(x + width - bevel_amount, y);
			p.addPoint(x + width, y + bevel_amount);
		}
		else
			p.addPoint(x + width, y);
		// Bottom right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_RIGHT) == BEVEL_CORNER_BOTTOM_RIGHT)
		{
			p.addPoint(x + width, y - bevel_amount + height);
			p.addPoint(x + width - bevel_amount, y + height);
		}
		else
			p.addPoint(x + width, y + height);
		// Bottom left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_LEFT) == BEVEL_CORNER_BOTTOM_LEFT)
		{
			p.addPoint(x + bevel_amount, y + height);
			p.addPoint(x, y + height - bevel_amount);
		}
		else
			p.addPoint(x, y + height);
		
		g.fill(p);
	}
	
	public static Polygon getBoxShape(int x, int y, int width, int height)
	{
		Polygon result = new Polygon();
		
		int bevel_amount = DEFAULT_BEVEL_AMOUNT;
		if (width < bevel_amount*2)
			bevel_amount = width/2;
		if (height < bevel_amount*2)
			bevel_amount = height/2;
		
		// Top left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_LEFT) == BEVEL_CORNER_TOP_LEFT)
		{
			result.addPoint(x, y + bevel_amount);
			result.addPoint(x + bevel_amount, y);
		}
		else
			result.addPoint(x, y);
		// Top right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_TOP_RIGHT) == BEVEL_CORNER_TOP_RIGHT)
		{
			result.addPoint(x + width - bevel_amount, y);
			result.addPoint(x + width, y + bevel_amount);
		}
		else
			result.addPoint(x + width, y);
		// Bottom right
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_RIGHT) == BEVEL_CORNER_BOTTOM_RIGHT)
		{
			result.addPoint(x + width, y - bevel_amount + height);
			result.addPoint(x + width - bevel_amount, y + height);
		}
		else
			result.addPoint(x + width, y + height);
		// Bottom left
		if ((DEFAULT_BEVEL_CORNERS & BEVEL_CORNER_BOTTOM_LEFT) == BEVEL_CORNER_BOTTOM_LEFT)
		{
			result.addPoint(x + bevel_amount, y + height);
			result.addPoint(x, y + height - bevel_amount);
		}
		else
			result.addPoint(x, y + height);
		
		return result;
	}
	
	/**
	 * Draw a filled octagon at the specified position with the specified size
	 * @param g		The graphics context to use
	 * @param x		The x position to draw at
	 * @param y		The y position to draw at
	 * @param size	The size of the octagon (width and height)
	 */
	public static void drawFilledOctagon(Graphics2D g, int x, int y, float size)
	{
		if (octagon == null)
		{
			octagon = new Polygon();
			octagon.addPoint(-3, -7);	// 1
			octagon.addPoint(3, -7);	// 2
			octagon.addPoint(7, -3);	// 3
			octagon.addPoint(7, 3);		// 4
			octagon.addPoint(3, 7);		// 5
			octagon.addPoint(-3, 7);	// 6
			octagon.addPoint(-7, 3);	// 7
			octagon.addPoint(-7, -3);	// 8
		}
		
		AffineTransform oldTransform = g.getTransform();
		g.transform(AffineTransform.getTranslateInstance(x, y));
		g.transform(AffineTransform.getScaleInstance(size/14, size/14));
		
		g.fill(octagon);
		
		g.setTransform(oldTransform);
	}
	
	public static Color modulateColor(Color c, float alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.round(255 * alpha));
	}
}
