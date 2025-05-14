package user_interface_components;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

// This will be the gun selection thing at the top of the screen. you can press a button to swap guns (s)
public class GunDisplay extends JComponent
{
	private Rectangle2D pistolSelect; //green for now
	private Rectangle2D shotgunSelect; //red for now
	private Rectangle2D glSelect; //blue for now
	private int boxLength = 59;
	
	private Rectangle2D outline;
	
	public GunDisplay(int x, int y, int width, int height)
	{
		this.setBounds(x, y, width, height);
		outline = new Rectangle2D.Double(0,0,width-1,height-1);
		pistolSelect = new Rectangle2D.Double(0,0,boxLength,boxLength);
		shotgunSelect = new Rectangle2D.Double(70,0,boxLength, boxLength);
		glSelect = new Rectangle2D.Double(140,0,boxLength,boxLength);
		this.setFocusable(false);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		//g2.draw(outline);	
		g2.setColor(Color.GREEN);
		g2.draw(pistolSelect);
		g2.fill(pistolSelect);
		g2.setColor(Color.RED);
		g2.draw(shotgunSelect);
		g2.fill(shotgunSelect);
		g2.setColor(Color.BLUE);
		g2.draw(glSelect);
		g2.fill(glSelect);
	}
}
