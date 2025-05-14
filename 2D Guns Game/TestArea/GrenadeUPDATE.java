import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import java.awt.Color;

public class GrenadeUPDATE extends JComponent 
{
	private Ellipse2D.Double ball;
	private Color color;
	//sweetspot> dx:3, dy:-20
	private double dx;
	private double dy;
	
	private boolean inStasis;
	private boolean threshold = false;
	private double maxDyValue = -100;
	private double preservedEnergy = 0.5;
	private double remainingEnergy = 0.5;
	
	public GrenadeUPDATE(int x, int y, int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
		inStasis = false;
		this.color = Color.BLUE;
		this.setBounds(x, y, 16, 16);
		ball = new Ellipse2D.Double(0, 0, 15, 15);
		this.setFocusable(false);
	}
	
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public void setDx(double d)
	{
		this.dx = d;
	}
	
	public void setDy(double dy)
	{
		this.dy = dy;
	}
	
	public void updateX()
	{
		this.setLocation((int)(getX() + dx), (int)(this.getY()));
	}
	public void updateY()
	{
		this.setLocation((int)getX(), (int)(this.getY() + dy));
	}
	
	public double getDx()
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
		g2.draw(ball);
		g2.fill(ball);
	}


	public boolean isInStasis() {
		return inStasis;
	}


	public void setInStasis(boolean inStasis) {
		this.inStasis = inStasis;
		this.dy = 0;
		this.dx = 0;
	}
	
	public void checkDyValue()
	{
		if (getDy() > maxDyValue)
		{
			maxDyValue = getDy();
		}
	}


	public double getMaxDyValue() 
	{
		return maxDyValue;
	}


	public void setThreshold(boolean threshold) 
	{
		this.threshold = threshold;
		
	}


	public boolean getThreshold() 
	{
		return threshold;
	}


	public void incPreservedEnergy(double amount) 
	{
		preservedEnergy += amount;
		
	}
	
	public double getPreservedEnergy()
	{
		return preservedEnergy;
	}


	public double calcNewPercentage(double energyFactor) 
	{
		remainingEnergy -= energyFactor;
		if (remainingEnergy <= 0.12)
		{
			//if this is true, then the grenade should rest on ground (in stasis)
			return 0;
		}
		else
			return remainingEnergy;
		
	}
	

}