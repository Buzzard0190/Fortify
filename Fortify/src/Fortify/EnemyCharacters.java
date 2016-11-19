package Fortify;

import java.util.Random;

import jig.Entity;
import jig.Vector;
import jig.ResourceManager;


public class EnemyCharacters extends Entity{

	boolean skeleton, goblin, inCombat;
	int futureX, futureY;
	int health, attackingX, attackingY, lastHit;
	private int armorClass;
	
	private Vector velocity;
	
	public EnemyCharacters(int type, int x, int y){

		super(x, y);

		if(type == 1){
			addImage(ResourceManager.getImage(Game.GOBLIN_RSC));
			goblin = true;
			health = 10;
			armorClass = 13;
			velocity = new Vector(.0f, .0f);
			inCombat = false;
		} else if (type == 2){
			addImage(ResourceManager.getImage(Game.SKELLY_RSC).getScaledCopy((float) .12));
			skeleton = true;
			health = 15;
			armorClass = 15;
			velocity = new Vector(.0f, .0f);
			inCombat = false;
		}
	}
	
	public void setVelocity(float vx, float vy) {
		velocity = new Vector(vx, vy);
	}
	
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return velocity;
	}

	
	//=============================================================
	//Combat section
	//=============================================================	
	
	public int getAC(){
		return this.armorClass;
	}
	
	public int rollAttack(){
		Random r = new Random();
		return (r.nextInt(20)+1);
	}
	
	public int rollDamage(){
		Random r = new Random();
		
		if(this.goblin){
			return (r.nextInt(6)+1);
		} else {
			return 2;
		}
	}
	
	public void characterHit(int i){
		//removes hit points from shield if exists otherwise attacks character
		if(this.health - i <= 0){
			this.health = 0;
		} else {
			this.health-=i;
		}
		
	}
	
	
}
