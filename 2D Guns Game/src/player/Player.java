package player;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class Player extends JComponent
{
	private Rectangle2D humanoid;
	private int dx = 0;
	private int dy = 0;
	private boolean inStasis;
	private boolean grounded;
	private boolean jumpHeld;
	private int jumps;
	private boolean fireHeld;
	private String gunHeld;
	
	public Player(int x, int y)
	{
		this.setBounds(x, y, 30, 80);
		humanoid = new Rectangle2D.Double(0,0,29,79);
		this.setFocusable(false);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		g2.draw(humanoid);	
		g2.fill(humanoid);
		//
	}
	
	//getter and setter for dx
	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	//getter and setter for dy
	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
	
	//update method
//	public void update()
//	{
//		this.setLocation(getX() + dx, getY() + dy);
//	}
	public void updateX()
	{
		this.setLocation(getX()+dx, getY());
	}
	public void updateY()
	{
		this.setLocation(getX(), getY()+dy);
	}

	public boolean isInStasis() {
		return inStasis;
	}

	public void setInStasis(boolean inStasis) {
		this.inStasis = inStasis;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public boolean isJumpHeld() {
		return jumpHeld;
	}

	public void setJumpHeld(boolean jumpHeld) {
		this.jumpHeld = jumpHeld;
	}

	public int getJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	public boolean isFireHeld() {
		return fireHeld;
	}

	public void setFireHeld(boolean fireHeld) {
		this.fireHeld = fireHeld;
	}

	public String getGunHeld() {
		return gunHeld;
	}

	public void setGunHeld(String gunHeld) {
		this.gunHeld = gunHeld;
	}

}
