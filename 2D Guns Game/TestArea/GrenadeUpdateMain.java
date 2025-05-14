import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GrenadeUpdateMain extends JFrame implements ActionListener
{
	
	private JButton ground;
	private GrenadeUPDATE g;
	private Timer timer;
	//when multiplying by Dy, gets 80 percent of the dy
	private double groundEnergyFactor = 0.05;
	//keeping track of MIN dy value
	private double maxDyValue;
	private boolean threshold;
	
	public GrenadeUpdateMain()
	{
		maxDyValue = -100;
		threshold = false;
		
		this.setBounds(100, 100, 1300, 600);
		this.setLayout(null);
		this.setTitle("Gravity demo");
		
		ground =new JButton("-------------------------");
		ground.setBounds(0,500,1300,50);
		this.add(ground);
		
		//--
		g = new GrenadeUPDATE(250, 20, 0, 0);
		this.add(g);
		
		timer = new Timer(25, this);
		timer.start();
		
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		//updating the ball
		g.updateX();
		g.updateY();
		
		//This is for FREE FALL LOGIC-- ALWAYS PRESENT
		if (!g.isInStasis())
		{	// changing the rate of change
			g.setDy(g.getDy() + 1);
			
			//easier way to see if it intersects with block/ground
			if (g.getBounds().intersects(ground.getBounds()))
			{
				/** ^^ from above, we can create a variable using the object type the grenade
				 * collided with, and use this in the rest of the statements. 
				 * Ex: energyFactor = sb.getEnergyFactor() --> bouncier materials=lower energyFactor
				 */
				
				//this line ensures ball will bounce directly from the ground (or whatever block)
				g.setLocation(g.getX(), ground.getY() - g.getHeight());
				
				//this is the issue right here >
				//g.setDy(g.getDy() * -(g.getPreservedEnergy() - groundEnergyFactor));
				//					how will we make this ^ equal to zero eventually??
				g.setDy(g.getDy() * -g.calcNewPercentage(groundEnergyFactor));
				if (g.getDy()==0)
				{
					System.out.println("ball should be in stasis");
					g.setLocation(g.getX(), ground.getY() - g.getHeight());
					g.setInStasis(true);
				}
				
			}
		}
	}
	
	//MAIN
	public static void main(String[] args) 
	{
		new GrenadeUpdateMain();

	}
}