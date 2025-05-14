import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class test extends JComponent
{
	//fields 
	private Rectangle2D ground;
	private Color color;
	
	//constructors
	public test(int x, int y, int width, int height, Color c)
	{
		this.setBounds(x, y, width, height);
		ground = new Rectangle2D.Double(x, y, width-1, height-1);
		color = c;
	}
	
	//methods
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.draw(ground);	
		g2.fill(ground);
	}
	
	public void setColor(Color c)
	{
		color = c;
	}

}