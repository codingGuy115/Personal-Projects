package obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

//I want this to be a Parent class for most obstacles.
public abstract class SolidBlock extends JComponent
{
	//fields
	private int dx;
	private int dy;
	private int blockWidth = 70;
	private int blockHeight = 70;
	private double startingHp;
	private double hitpoints; //>when updating healthBar, need to take PERCENTAGE of this
	private double barWidthConstant = blockWidth-6;
	private double hbarWidthVal = blockWidth-6;
	private Color color; //> might change this field to store an image for graphics instead
	private Rectangle2D.Double blockDrawing;
	private Rectangle2D.Double healthBarOutline;
	private Rectangle2D.Double healthBar;
	private boolean isHit;
	private double blockPreservedEnergy = 0.5;
	
	//constructors
	public SolidBlock(int x, int y, int width, int height)
	{
		this.setBounds(x, y, width+1, height+1);
		blockDrawing = new Rectangle2D.Double(0,0,width,height);
		healthBarOutline = new Rectangle2D.Double(2,2,width-4, 10);
		healthBar = new Rectangle2D.Double(3,3,(int)hbarWidthVal, 8);
		isHit = false;
	}

	//methods
	public double getHbarWidthVal()
	{
		return hbarWidthVal;
	}	
	
	//WHEN BLOCK GETS HIT
	public void takeDamage(double amount)
	{
		isHit = true;
		hitpoints -= amount;
		//updating value for actual width of visible Healthbar
		double HpPerPixel = startingHp / barWidthConstant;
		hbarWidthVal = hitpoints / HpPerPixel;
		healthBar.setFrame(3, 3, hbarWidthVal, 8);
	}
	
	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void update()
	{
		this.setLocation(getX() + dx, getY() + dy);
	}
	
	public void paintComponent(Graphics g)
	{
		//type casting to a Graphics2D object in the method
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		//using inherited method
		g2.draw(blockDrawing);
		g2.fill(blockDrawing);
		
		if (isHit) {
		g2.setColor(Color.black);
		g2.draw(healthBarOutline);
		g2.setColor(Color.green);
		g2.draw(healthBar);
		g2.fill(healthBar); }
	}

	public double getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(double hitpoints) {
		this.hitpoints = hitpoints;
	}

	public double getStartingHp() {
		return startingHp;
	}

	public void setStartingHp(double startingHp) {
		this.startingHp = startingHp;
	}

	public double getBlockPreservedEnergy() {
		return blockPreservedEnergy;
	}

	public void setBlockPreservedEnergy(double blockPreservedEnergy) {
		this.blockPreservedEnergy = blockPreservedEnergy;
	}
	
	

}
