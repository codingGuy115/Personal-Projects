package projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class ShotgunPellet extends JComponent
{
	private Ellipse2D.Double pellet;
	private Color color;
	
	private int dx = 20;
	private int dy = (int)(Math.random()*-9 + 6);
	
	private boolean inStasis;
	
	public ShotgunPellet(int x, int y)
	{
		inStasis = false;
		this.color = Color.RED;
		this.setBounds(x, y, 6, 6);
		pellet = new Ellipse2D.Double(0,0,5,5);
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
	
	public void setDy(int dy)
	{
		this.dy = dy;
	}
	
	public void update()
	{
		this.setLocation(getX() + dx, getY() + dy);
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
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(color);
		//using inherited method
		g2.draw(pellet);
		g2.fill(pellet);
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
