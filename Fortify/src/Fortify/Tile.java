package Fortify;

import java.util.Random;

public class Tile {
	
	private boolean collision, bolt = false;
	private int weight;	
	PlayerCharacters pc;
	EnemyCharacters npc;
	
	
	public Tile(){
		
		collision = false;
		pc = null;
		weight = 0;
		
	}

	
	
	public void setCollision(){
		this.collision = true;
	}
	
	public int getCollision(){
		if(this.collision == true || this.pc != null){
			return 1;
		} else {
			return 0;
		}
	}
	
	public void setCharacter(int type){
		this.pc = new PlayerCharacters(type);
		
		if (type == 1){
			weight = 5;
		} else if (type == 2){
			weight = 3;
		} else if (type == 3){
			weight = 1;
		}
	}
	
	public void setWall(int type){
		this.pc = new PlayerCharacters(0, type);
		weight = 10;
	}

	public int getPlayerType(){
		if(this.pc != null){
			return pc.playerType();
		}
		return 0;
	}
	
	public void setWeight(int w){
		weight = w;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getWallType(){
		if(this.pc != null){
			return pc.wallType();
		}
		return 0;
	}
	
	
	
	//=============================================================
	//Player action section
	//=============================================================	
	
	public int getHealth(){
		return pc.health;
	}
	
	public void setShield(){
		this.pc.shield = 10;
	}
	
	public int getShield(){
		return this.pc.shield;
	}
	
	public void setHeal(){
		if(this.pc.health + 8 > this.pc.maxHealth){
			this.pc.health = this.pc.maxHealth;
		} else {
			this.pc.health+=8;
		}
	}
	
	
	
	
	//=============================================================
	//Combat section
	//=============================================================	
	
	public int getAC(){
		return this.pc.armorClass;
	}
	
	public void characterHit(int i){
		//removes hit points from shield if exists otherwise attacks character
		if(this.pc.shield > 0){
			if(this.pc.shield - i < 0){
				this.pc.shield = 0;
			} else {
				this.pc.shield-=i;
			}
		} else {
			if(this.pc.health - i <= 0){
				collision = false;
				pc = null;
				weight = 1;
			} else {
				this.pc.health-=i;
			}
		}
	}
	
	public int rollAttack(){
		Random r = new Random();
		return (r.nextInt(20)+3);	
	}
	
	public int rollDamage(){
		Random r = new Random();
		
		if(bolt == true){
			return (r.nextInt(12)+1);
		}
		
		if(this.getPlayerType() == 1){
			return (r.nextInt(6)+1);
		} else if (this.getPlayerType() == 2){
			return (r.nextInt(8)+1);
		} else {
			return (r.nextInt(12)+1);
		}
	}
	
	public void setBolt(boolean b){
		bolt = b;
	}
	
	public Boolean getBolt(){
		return bolt;
	}
	
}
