package user_interface_components;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class SelectBar extends JComponent
{
	private Rectangle2D outline;
	private int currentPos;
	
	public SelectBar(int x, int y, int width, int height)
	{
		this.setBounds(x, y, width, height);
		outline = new Rectangle2D.Double(0,0,width-1,height-1);
		setCurrentPos(1);
		this.setFocusable(false);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.draw(outline);
		g2.fill(outline);
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}
	

}
