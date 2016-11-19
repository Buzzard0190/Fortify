package Fortify;


import jig.Entity;
import jig.Vector;
import jig.ResourceManager;

public class Projectile extends Entity{

	int destX, destY;
	private Vector velocity = new Vector(.2f, .2f);

	public Projectile(int type, int x, int y, int dx, int dy, Vector newVelocity, double archerTheta){

		super(x, y);
		destX = dx; destY = dy;
		if(type == 1){
			org.newdawn.slick.Image arrow = ResourceManager.getImage(Game.ARROW_RSC);
			arrow.setCenterOfRotation(6, 20);
			arrow.setRotation((float) (360-archerTheta));
			addImage(arrow);
			velocity = newVelocity;
			velocity = velocity.setX(-velocity.getX());
		} else if (type == 2){
			
		}
	}
	
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
	
	public int getDestX(){
		return destX;
	}
	
	public int getDestY(){
		return destY;
	}
}
