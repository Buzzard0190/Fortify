package Fortify;

public class PlayerCharacters {
	
	private boolean swordsman, archer, cleric;
	int health, shield, wallType, maxHealth, armorClass;
	int lastHit;
	
	
	//This is an init for any generated character or wall
	public PlayerCharacters(int type){
		
		swordsman = false;
		archer = false;
		cleric = false;
		
		if (type == 1){
			this.swordsman = true;
			health = 12;
			maxHealth = 12;
			armorClass = 14;
			shield = 0;
		} else if (type == 2){
			this.archer = true;
			health = 8;
			maxHealth = 8;
			armorClass = 10;
			shield = 0;
		} else if (type == 3){
			this.cleric = true;
			health = 50;
			maxHealth = 50;
			armorClass = 16;
			shield = 0;
		}

	}

	public PlayerCharacters(int wall, int type){
		if(wall == 0){
			wallType = type;
			health = 25;
			maxHealth = 25;
			armorClass = 5;
			shield = 0;
		}
	}
	
	public int playerType(){
		if(this.swordsman){
			return 1;
		} else if (this.archer){
			return 2;
		} else if (this.cleric){
			return 3;
		} else {
			return 0;
		}
	}
	
	public int wallType(){
		return this.wallType;
	}
	
	
}
