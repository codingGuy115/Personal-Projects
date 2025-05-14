package projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class Bullet extends JComponent
{
	/**
	 * NOTE: cannot instantiate Rectangle2D, as this is purely the abstact class for rectangles.
	 * You must use .Double
	 */
	private Rectangle2D.Double shell;
	private Color color;
	
	private int dx = 20;
	private double dy = 0;
	
	private boolean inStasis;
	
	public Bullet(int x, int y)
	{
		inStasis = false;
		this.color = Color.GREEN;
		this.setBounds(x, y, 10, 10);
		shell = new Rectangle2D.Double(0,0,9,9);
		this.setFocusable(false);
	}
	
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public void setDx(int dx)
	{
		this.dx = dx;
	}
	
	public void setDy(double dy)
	{
		this.dy = dy;
	}
	
	public void update()
	{
		this.setLocation(getX() + dx, (int)(this.getY() + dy));
	}
	
	public int getDx()
	{
		return dx;
	}
	
	public double getDy()
	{
		return dy;
	}
	
	public void paintComponent(Graphics g)
	{
		//type casting to a Graphics2D object in the method
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		//using inherited method
		g2.draw(shell);
		g2.fill(shell);
	}


	public boolean isInStasis() {
		return inStasis;
	}


	public void setInStasis(boolean inStasis) {
		this.inStasis = inStasis;
		this.dy = 0;
		this.dx = 0;
	}
	

}
