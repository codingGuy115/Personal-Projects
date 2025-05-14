import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import projectiles.Bullet;
//Game specific import statements 
import projectiles.Grenade;
import projectiles.ShotgunPellet;
import user_interface_components.GunDisplay;
import user_interface_components.SelectBar;
import environment.Ground;
import obstacles.SolidBlock;
import obstacles.WoodBlock;
import player.Player;

public class gameMain extends JFrame implements ActionListener
{
	
	private Timer timer;
	private double preserved_energy = 0.8; /**(this doesn't apply to the grenades. originally, was going to make it
	so only 1 grenade can be fired at a time, but i figured that would be boring. (can also make it a 
	power up to have multiple at a time)*/
	
	private int gravStrength = 1;
	private Ground ground;
	private Player player;
	private ArrayList<Grenade> grenades;
	private ArrayList<Bullet> bullets;
	private ArrayList<ShotgunPellet> shotgunPellets;
	private ArrayList<SolidBlock> blocks;
	private GunDisplay gunDisplay;
	private SelectBar sb1;
	private SelectBar sb2;
	private String[] guns = {"Pistol", "Shotgun", "Grenade Launcher"};
	
	
	public gameMain()
	{
		grenades = new ArrayList<Grenade>();
		bullets = new ArrayList<Bullet>();
		shotgunPellets = new ArrayList<ShotgunPellet>();
		blocks = new ArrayList<SolidBlock>();
		
		this.setBounds(100, 100, 1300, 600);
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setTitle("Grenade Launcher Game!");
		
		player = new Player(10, 10);
		this.add(player);
		player.setJumpHeld(false);
		player.setFireHeld(false);
		player.setGunHeld("Pistol");
		
		ground = new Ground(0, 512, 1300, 50, Color.BLACK);
		this.add(ground);
		
		gunDisplay = new GunDisplay(200, 20, 200, 60);
		this.add(gunDisplay);
		sb1 = new SelectBar(200, 8, 60, 10);
		this.add(sb1);
		sb2 = new SelectBar(200, 81, 60, 10);
		this.add(sb2);
		
		//TEMP - SPAWNING BLOCKS
		for (int x = 290; x<= 1200; x+=70)
		{
			for (int y = 260; y<=400; y+=70)
			{
				blocks.add(0, new WoodBlock(x, y, Color.ORANGE));
				//blocks.get(0).setDx(-1);
				this.add(blocks.get(0));
			}
		}
		
		
		timer = new Timer(25, this);
		timer.start();
		
		/**
		 * NEED TO MULTITHREAD THESE MOUSE AND KEY EVENTS
		 */
		// -- ADDING MOUSE LISTENER (to the frame itself)
		this.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
//				int code = e.getID();
//				switch(code)
//				{
//				case MouseEvent.BUTTON1:
//					System.out.println("left click");
//					break;
//				case MouseEvent.BUTTON2:
//					System.out.println("Right click");
//				}
			}
			public void mouseReleased(MouseEvent e)
			{
				
			}
			public void mouseEntered(MouseEvent e)
			{
				
			}
			public void mouseExited(MouseEvent e)
			{
				
			}
			
			public void mouseClicked(MouseEvent e)
			{
				if (player.getGunHeld().equals("Grenade Launcher"))
				{
					int mouseX = e.getX();
					int mouseY = e.getY();
					double dx = 0;
					double dy = 0;
					double distX = (player.getX()+player.getWidth()) - mouseX;
					double distY = player.getY() - mouseY;
					
				// -- WILL HAVE TO LIMIT RANGE LATER 
					dx= -((player.getX()+player.getWidth()) - mouseX)/11;
					dy = -(player.getY() - mouseY)/11;	
					
					grenades.add(0, new Grenade(player.getX()+player.getWidth(), player.getY(), (int)dx, (int)dy));
					add(grenades.get(0));
				}
			}
		});
		//adding keyListener
		this.addKeyListener(new KeyListener() 
		{

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) 
			{	
				int code = e.getKeyCode();
				switch(code)
				{
				//player 1
				case KeyEvent.VK_A:
					player.setDx(-3);
					break;
				case KeyEvent.VK_D:
					player.setDx(3);
					break;
				case KeyEvent.VK_SPACE:
					if (!player.isJumpHeld() && player.getJumps() == 1 && player.isGrounded())
					{
						player.setDy(-20);
						player.setGrounded(false);
						player.setJumpHeld(true);
						player.setJumps(0);
					}
					break;
			// firing the pistol and the shotgun
				case KeyEvent.VK_K:
				{
					if (!player.isFireHeld() && player.getGunHeld().equals("Pistol"))
					{
						//--TESTING
						//blocks.get(0).takeDamage(10);
						//System.out.println("Block HP: "+blocks.get(0).getHitpoints());
						
						bullets.add(0, new Bullet(player.getX()+player.getWidth(), player.getY()));
						add(bullets.get(0));
					}
					
					if (!player.isFireHeld() && player.getGunHeld().equals("Shotgun"))
					{	//this will be for ONE shotgun shot event (gets repeated obviously)
						for (int i=0; i<6; i++)
						{
							shotgunPellets.add(0, new ShotgunPellet(player.getX()+player.getWidth(), player.getY()));
							add(shotgunPellets.get(0));
						}
						//TEMP
						//System.out.println("ShotgunPellets: "+shotgunPellets.size());
					}
								
				}
				player.setFireHeld(true);
				break;
					
				case KeyEvent.VK_S:
				{
					//handling gun swaps
					if (sb1.getCurrentPos()==3)
					{
						sb1.setCurrentPos(1);
						sb1.setLocation(200, sb1.getY());
						sb2.setLocation(200, sb2.getY());
					}
					else
					{
						sb1.setCurrentPos(sb1.getCurrentPos()+1);
						sb1.setLocation(sb1.getX()+70, sb1.getY());
						sb2.setLocation(sb2.getX()+70, sb2.getY());
					}
					player.setGunHeld(guns[sb1.getCurrentPos()-1]);
					//System.out.println("gun: " + player.getGunHeld());
					break;
				}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				int code = e.getKeyCode();
				switch(code)
				{
				case KeyEvent.VK_A:
					player.setDx(0);
					break;
				case KeyEvent.VK_D:
					player.setDx(0);
					break;
				case KeyEvent.VK_SPACE:
					player.setJumpHeld(false);
					break;
			// for pistol and shotgun
				case KeyEvent.VK_K:
					player.setFireHeld(false);
					break;
					
				}
			}
		
		});
				
		player.setGrounded(false);
		
		//-last 3 lines
		this.setResizable(false);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	
	

	/**
	 * EACH TIMER TICK
	 */
	public void actionPerformed(ActionEvent e) 
	{
		this.repaint();
		player.updateX();
		for (int i=0; i<blocks.size(); i++)
		{
			SolidBlock sb = blocks.get(i);
			//player touches left of block
			if (sb.getBounds().intersects(player.getBounds()))
			{
				//player hit a block. now to determine left or right side
				if (player.getX() < sb.getX())
				{
					//System.out.println("Player hit LEFT of block");
					player.setDx(0);
					player.setLocation(sb.getX()-player.getWidth(), player.getY());
				}
				if (player.getX() > sb.getX())
				{
					//System.out.println("Player hit RIGHT of block");
					player.setDx(0);
					player.setLocation(sb.getX()+sb.getWidth(), player.getY());
				}
			}
		}
		
		player.updateY();
		for (int i=0; i<blocks.size(); i++)
		{
			SolidBlock sb = blocks.get(i);
			//player touches left of block
			if (sb.getBounds().intersects(player.getBounds()))
			{
				//player hit a block. now to determine top or bottom side
				if (player.getY() < sb.getY())
				{
					// - This is a special case, because it resets the players jumps and sets grounded=true
					//System.out.println("Player hit TOP of block");
					player.setDy(0);
					player.setJumps(1);
					player.setLocation(player.getX(), sb.getY()-player.getHeight());
					player.setGrounded(true);
				}
				if (player.getY() > sb.getY())
				{
					//System.out.println("Player hit BOTTOM of block");
					player.setDy(0);
					player.setLocation(player.getX(), sb.getY()+player.getHeight());
				}
			}
		}
		
		/**making sure that if player falls off block, does NOT retain a jump
		 * ... or fuck it we make it a feature???
		 */
		//System.out.println("Player Y:"+player.getY());
		
		
		//only if the player is not on ground, GRAVITY acts on them (obviously not IRL lmao)
		//if (!player.isGrounded())
		//{	// changing the rate of change
			player.setDy(player.getDy() + gravStrength);
			
			if (player.getBounds().intersects(ground.getBounds()))
			{
				player.setDy(0);
				player.setJumps(1);
				//System.out.println("Player touched ground.");
				//ensures player will land directly on ground
				player.setLocation(player.getX(), ground.getY() - player.getHeight());
				player.setGrounded(true);
			}
			
		//}
		
		//updating SolidBlocks
		for (int i=0; i<blocks.size(); i++)
		{
			SolidBlock sb = blocks.get(i);
			sb.update();
			sb.repaint();
			//removing if health on any block <= 0
			if (sb.getHitpoints()<=0)
			{
				blocks.remove(sb);
				this.remove(sb);
			}
			//handling case where player collides with blocks
//			if (sb.getBounds().intersects(player.getBounds()))
//			{
//				/**2 Cases: player is on top, or to side of a block*/
//				//TOP
//				if ()
//				
//			}
		}
		
		//updating bullets
		for (int i=0; i<bullets.size(); i++)
		{
			Bullet b = bullets.get(i);
			b.update();
			//handling bullets hitting obstacles
			for (int k=0; k<blocks.size(); k++)
			{
				SolidBlock block = blocks.get(k);
				if (b.getBounds().intersects(block.getBounds()))
				{
					block.takeDamage(10);
					bullets.remove(b);
					this.remove(b);
				}
			}
			//removing if leaves frame
			if (b.getX() > 1300)
			{
				bullets.remove(i);
				//System.out.println("Bullets array size: "+bullets.size());
			}
		}
		
		//updating shotgun pellets
		for (int i=0; i<shotgunPellets.size(); i++)
		{
			ShotgunPellet p = shotgunPellets.get(i);
			p.update();
			//handling shotgun pellets hitting obstacles
			for (int k=0; k<blocks.size(); k++)
			{
				SolidBlock block = blocks.get(k);
				if (p.getBounds().intersects(block.getBounds()))
				{
					//System.out.println("Pellet hit obstacle");
					block.takeDamage(10);
					shotgunPellets.remove(p);
					this.remove(p);
				}
			}
			//removing if leaves frame or hits ground
			if (p.getBounds().intersects(ground.getBounds()) || p.getX() > 1300)
			{
				//System.out.println("Shotgun Pellet hit ground/OOB");
				shotgunPellets.remove(p);
				this.remove(p);
			}
			
		}
		
		//updating grenades
		for (Grenade g: grenades)
		{
//			g.updateX();
//			g.updateY();
//			//checking if bounced off obstacle
//			for (int i=0; i<blocks.size(); i++)
//			{
//				SolidBlock sb = blocks.get(i);
//				if (g.getBounds().intersects(sb.getBounds()))
//				{
//					//WOODBLOCK
//					if (sb instanceof WoodBlock)
//					{
//						g.setLocation(g.getX(), sb.getY() - g.getHeight());
//						
//						if (!g.getThreshold())
//							g.setDy(g.getDy() * -g.getPreservedEnergy());
//						else
//						{
//							System.out.println("Threshold is true");
//							g.incPreservedEnergy(-0.15);
//							g.setDy(g.getDy() * -g.getPreservedEnergy());
//						}
//						
//						g.checkDyValue();
//					}
//					//OTHER...
//					
//				}
//				
//			}
			// TESTING implementing player collision logic
			g.updateX();
			for (int i=0; i<blocks.size(); i++)
			{
				SolidBlock sb = blocks.get(i);
				double blockPresE = sb.getBlockPreservedEnergy();
				
				if (sb.getBounds().intersects(g.getBounds()))
				{
					//grenade hit a block. now to determine left or right side
					if (g.getX() < sb.getX())
					{
						//System.out.println("grenade hit LEFT of block");
						g.setLocation(sb.getX()-player.getWidth(), g.getY());
						g.setDx(g.getDx() * -blockPresE);
					}
					if (g.getX() > sb.getX())
					{
						//System.out.println("grenade hit RIGHT of block");
						g.setLocation(sb.getX()+sb.getWidth(), g.getY());
						g.setDx(g.getDx() * -blockPresE);
					}
				}
			}
			
			g.updateY();
			for (int i=0; i<blocks.size(); i++)
			{
				SolidBlock sb = blocks.get(i);
				double blockPresE = sb.getBlockPreservedEnergy();
				
				if (sb.getBounds().intersects(g.getBounds()))
				{
					//player hit a block. now to determine top or bottom side
					if (g.getY() < sb.getY())
					{
						//System.out.println("grenade hit TOP of block");
						g.setLocation(g.getX(), sb.getY() - g.getHeight());				
						if (!g.getThreshold())
							g.setDy(g.getDy() * -blockPresE);
						else
						{
							//System.out.println("Threshold is true");
							blockPresE -= 0.15;
							g.setDy(g.getDy() * -blockPresE);
						}
						
						g.checkDyValue();
					}
					if (g.getY() > sb.getY())
					{
						//System.out.println("grenade hit BOTTOM of block");
						if (!g.getThreshold())
							g.setDy(0);
							//g.setDy(g.getDy() * -blockPresE);
						else
						{
							//System.out.println("Threshold is true");
							blockPresE -= 0.15;
							g.setDy(g.getDy() * -blockPresE);
						}
						
						g.checkDyValue();
					}
				}
			}
			// END of test
			
			
			g.setDy(g.getDy() + gravStrength);
			
			//checking if the threshold is reached
			if (g.getMaxDyValue() > -17)
			{
				g.setThreshold(true);
				//System.out.println("Threshold reached.");
			}
			
			/**if this is true, basically it means the value for the ball to bounce up(negative bc of frame axis)
			 * is small enough to be considered zero. 
			 */
			if(g.getMaxDyValue() > -0.3)
			{
				//System.out.println("ball put on ground");
				g.setLocation(g.getX(), ground.getY() - g.getHeight());
				g.setInStasis(true);
			}
			
			if (g.getBounds().intersects(ground.getBounds()))
			{
				//System.out.println(gball.getDy());
				//this line ensures ball will bounce directly from the ground
				g.setLocation(g.getX(), ground.getY() - g.getHeight());
				
				if (!g.getThreshold())
					g.setDy(g.getDy() * -g.getPreservedEnergy());
				else
				{
					//System.out.println("Threshold is true");
					g.incPreservedEnergy(-0.15);
					g.setDy(g.getDy() * -g.getPreservedEnergy());
				}
				
				g.checkDyValue();
			}
			

		}
		
	}
	
	//MAIN
	public static void main(String[] args) 
	{
		new gameMain();

	}
}