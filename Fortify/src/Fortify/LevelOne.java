package Fortify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;


import Fortify.Game;
import Fortify.Dijkstra;
import jig.ResourceManager;
import jig.Vector;


public class LevelOne extends BasicGameState {

	private static TiledMap map;
	int grassLayer, Collision, floorLayer, towerFront, towerBack;
	static Tile[][] tileSet;
	static ArrayList<Node> dijkstraGraph = new ArrayList<Node>();
	int wallType;
	static Graph graph;
	
	int posX, posY, xDelta, yDelta, xTile, yTile;
	static int time = 10;
	static int massHX = 0;
	static int massHY = 0;
	boolean massH;
	int currentDragged;
	double theta;
	static boolean gridChange = false;
	static int xCleric = 14;
	static int yCleric = 12;
	
	private static int countdown = 1000;
	private int monsterX;
	private int monsterY;
	private static int monsterQuantity = 10;
	private static int attackCountdown = 4000;
	private int unitCountdown = 0;
	private static int showCardCountdown = 1500;
	static ArrayList<EnemyCharacters> monsters = new ArrayList<EnemyCharacters>();
	static ArrayList<EnemyCharacters> attacking = new ArrayList<EnemyCharacters>();
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	static boolean debug = false;
	static boolean debugGrid = true;
	static boolean debugCollisions = false;
	static boolean debugDijkstra = true;
	static boolean debugWeight = false;
	private static int heal = 0;
	private static int massHeal = 0;
	private static int shield = 0;
	private static int bolt = 0;
	private static int swordsman = 0;
	private static int archer = 0;
	private static int wall = 0;
	private static boolean swordCard = false;
	private static boolean archerCard = false;
	private static boolean clericCard = true;
	String debugString;
	
	
	
//===================================================================================================================
//											Init 
//===================================================================================================================

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {


		//--------------------------
		//Load map built in tiled
		//--------------------------
		
		tileSet = new Tile[29][25];

		try{
			map = new TiledMap("Fortify/resource/test.tmx");
		} catch (SlickException e){
			System.out.println("Slick Exception Error: Level 1 map failed to load.");
		}
		
		grassLayer = map.getLayerIndex("grass");
		Collision = map.getLayerIndex("Collision");

		initVars();

				
	}

	
	
	
	
	
	
	
//===================================================================================================================
//												Render 
//===================================================================================================================

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		map.render(0, 0, grassLayer);

		//----------------------
		//Sidebar rendering
		//----------------------
		
		g.drawImage(ResourceManager.getImage(Game.SCROLL_RSC), 928, 0);	
		
		//upper banner of sidebar. offers monster information
		g.setColor(Color.black);
		g.drawString("Monsters Remaining: " + monsterQuantity, 990, 30);
		g.drawString("Next Monster in: " + time +"sec", 990, 50);
		
		//main sidebar information
		g.drawString("Shield", 1013, 165);
		g.drawString(shield + " spells", 1004, 180);
		
		g.drawString("Heal", 1022, 315);
		g.drawString(heal + " spells", 1004, 330);

		g.drawString("Guiding Bolt", 1138, 165);
		g.drawString(bolt + " spells", 1153, 180);

		g.drawString("Mass Heal", 1150, 315);
		g.drawString(massHeal + " spells", 1153, 330);

		g.drawString("Swordsman", 1008, 465);
		g.drawString(swordsman + " units", 1015, 480);

		g.drawString("Archer", 1169, 465);
		g.drawString(archer + " units", 1164, 480);

		g.drawString("Wall", 1109, 585);
		g.drawString(wall + " remaining", 1075, 600);

		g.drawImage(ResourceManager.getImage(Game.SHIELD_ICON_RSC).getScaledCopy((float) 2), 1000, 200);
		g.drawImage(ResourceManager.getImage(Game.GUIDING_BOLT_ICON_RSC).getScaledCopy((float) 2), 1150, 200, Color.darkGray);
		g.drawImage(ResourceManager.getImage(Game.HEAL_ICON_RSC).getScaledCopy((float) 2), 1000, 350);
		g.drawImage(ResourceManager.getImage(Game.MASS_HEAL_ICON_RSC).getScaledCopy((float) 2), 1150, 350, Color.darkGray);
		
		g.drawImage(ResourceManager.getImage(Game.SWORDSMAN_RSC).getScaledCopy((float) 1.3), 1025, 500);
		g.drawImage(ResourceManager.getImage(Game.ARCHER_RSC).getScaledCopy((float) 1.3), 1175, 500);
		g.drawImage(ResourceManager.getImage(Game.WALL_ICON_RSC).getScaledCopy((float) 1.3), 1060, 620);


		//--------------------------------------------------------------------------------
		//Cycle through tiles and render out items that are contained within the tiles
		//--------------------------------------------------------------------------------
		
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 29; j++) {

				if (tileSet[j][i].getPlayerType() == 0) {
					if (tileSet[j][i].getWallType() == 1) {
						g.drawImage(ResourceManager.getImage(Game.WALL_SINGLE_RSC), (j * 32), (i * 32) - 10);
					} else if (tileSet[j][i].getWallType() == 2) {
						g.drawImage(ResourceManager.getImage(Game.WALL_SINGLE_LEFT_RSC), (j * 32), (i * 32));
					} else if (tileSet[j][i].getWallType() == 3) {
						g.drawImage(ResourceManager.getImage(Game.WALL_SINGLE_RIGHT_RSC), (j * 32) - 10, (i * 32));
					}
				} else if (tileSet[j][i].getPlayerType() == 1) {
					//health bar for swordsman character
					g.setColor(Color.red);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getHealth())/((float) 12), 5);
					
					//Shield bar
					g.setColor(Color.blue);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getShield())/((float) 10), 5);
					
					//health bar outline
					g.setColor(Color.black);
					g.drawRect((j * 32)-2, (i * 32)-26, 40, 5);
					
					g.drawImage(ResourceManager.getImage(Game.SWORDSMAN_RSC), (j * 32) - 2, (i * 32) - 16);
				} else if (tileSet[j][i].getPlayerType() == 2) {
					//health bar for archer character
					g.setColor(Color.red);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getHealth())/((float) 8), 5);

					//Shield bar
					g.setColor(Color.blue);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getShield())/((float) 10), 5);
					
					//health bar outline
					g.setColor(Color.black);
					g.drawRect((j * 32)-2, (i * 32)-26, 40, 5);
					
					g.drawImage(ResourceManager.getImage(Game.ARCHER_RSC), (j * 32), (i * 32) - 16);
				} else if (tileSet[j][i].getPlayerType() == 3) {
					//health bar for archer character
					g.setColor(Color.red);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getHealth())/((float) 50), 5);

					//Shield bar
					g.setColor(Color.blue);
					g.fillRect((j * 32)-2, (i * 32)-26, 40*((float)tileSet[j][i].getShield())/((float) 10), 5);
					
					//health bar outline
					g.setColor(Color.black);
					g.drawRect((j * 32)-2, (i * 32)-26, 40, 5);
					
					g.drawImage(ResourceManager.getImage(Game.CLERIC_RSC), (j * 32), (i * 32) - 16);
				}

				//---------------------------
				//Render out the monsters
				//---------------------------
				for (EnemyCharacters enemy : monsters) enemy.render(g);
				for (Projectile proj : projectiles) proj.render(g);
			}
		}
		
		//-------------------------------------------
		//Show cards when new units are available
		//-------------------------------------------
		if(clericCard){
			if(showCardCountdown < 50){
				clericCard = false;
				showCardCountdown = 1500;
			} else if(showCardCountdown > 0){
				g.drawImage(ResourceManager.getImage(Game.CLERIC_CARD_RSC).getScaledCopy((float) .4), 980, 200);
			}
		} else if (archerCard){
			if(showCardCountdown < 50){
				archerCard = false;
				showCardCountdown = 1500;
			} else if(showCardCountdown > 0){
				g.drawImage(ResourceManager.getImage(Game.ARCHER_CARD_RSC).getScaledCopy((float) .4), 980, 200);
			}
		} else if (swordCard){
			if(showCardCountdown < 50){
				swordCard = false;
				showCardCountdown = 1500;
			} else if(showCardCountdown > 0){
				g.drawImage(ResourceManager.getImage(Game.SWORD_CARD_RSC).getScaledCopy((float) .4), 980, 200);
			}
		}
		
				
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&----------------debug graphic print section----------------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
			if(debug){

				for(int i = 0; i < 25; i++){
					for(int j = 0; j < 29; j++){
						//sets a grid
						if(tileSet[j][i].getCollision() == 1){
							debugString = "1";
						} else {
							debugString = "0";
						}
						if(debugCollisions){
							g.setColor(Color.orange);
							g.drawString(debugString, j*32, i*32);
						}
						if(debugGrid){
							g.setColor(Color.black);
							g.drawRect(j*32, i*32, 32, 32);
						}
						if(debugWeight){
							g.setColor(Color.blue);
							g.drawString(String.valueOf(tileSet[j][i].getWeight()), j*32, i*32+16);	
						}
					}
				}
				
				//print out paths for dijkstras
				if(debugDijkstra){
					g.setColor(Color.white);
					for(Iterator<Node> dijkstraNode = dijkstraGraph.iterator(); dijkstraNode.hasNext();){
						Node printPath = dijkstraNode.next();
						g.drawLine(printPath.x*32+16, printPath.y*32+16, printPath.px*32+16, printPath.py*32+16);
					}
				}
				g.setColor(Color.black);
			}	
		
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&-------------------------------------------------------------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

				
		//This will pick up an item and allow it to be dragged around the screen
		if( xTile < 29 && yTile < 25){
			
			if(currentDragged == 1){
				
				if(posX <= 928 && tileSet[xTile][yTile].getPlayerType() > 0 && posY <= 800){
					g.drawImage(ResourceManager.getImage(Game.SHIELD_ICON_RSC), posX-20, posY-20, Color.green);
				} else {
					g.drawImage(ResourceManager.getImage(Game.SHIELD_ICON_RSC), posX-20, posY-20);
				}
				
			} else if(currentDragged == 3){
				
				if(posX <= 928 && tileSet[xTile][yTile].getPlayerType() > 0 && posY <= 800){
					g.drawImage(ResourceManager.getImage(Game.HEAL_ICON_RSC), posX-20, posY-20, Color.green);
				} else {
					g.drawImage(ResourceManager.getImage(Game.HEAL_ICON_RSC), posX-20, posY-20);
				}
				
			} else if(currentDragged == 5){
				
				if(posX <= 928 && tileSet[xTile][yTile].getCollision() == 1 && posY <= 800){
					g.drawImage(ResourceManager.getImage(Game.SWORDSMAN_RSC), posX-20, posY-45, Color.red);
				} else {
					g.drawImage(ResourceManager.getImage(Game.SWORDSMAN_RSC), posX-20, posY-45);
				}

			} else if(currentDragged == 6){
				
				if(posX <= 928 && tileSet[xTile][yTile].getCollision() == 1 && posY <= 800){
					g.drawImage(ResourceManager.getImage(Game.ARCHER_RSC), posX-15, posY-45, Color.red);
				} else {
					g.drawImage(ResourceManager.getImage(Game.ARCHER_RSC), posX-15, posY-45);
				}

			} else if(currentDragged == 7){

				//------------------------------------------------------------------------
				//rotate wall while being dragged based on theta/position of mouse
				//------------------------------------------------------------------------
				
				if((theta >= 135 && theta <= 225) || (theta >= 315 || theta <= 45) || posX >= 928){
					g.drawImage(ResourceManager.getImage(Game.WALL_RSC), posX-50, posY-40);
				} else if (theta >= 225 && theta <= 315){
					g.drawImage(ResourceManager.getImage(Game.WALL_LEFT_RSC), posX, posY-50);
				} else if (theta >= 45 && theta <= 135){
					g.drawImage(ResourceManager.getImage(Game.WALL_RIGHT_RSC), posX-40, posY-50);
				}		
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
//===================================================================================================================
//											Update 
//===================================================================================================================

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

		Game myGame = (Game) game;
		//added for use to find what sidebar item was clicked also used to find where the 
		//mouse might be at any given time while over the game board
		Input input = container.getInput();
		posX = input.getMouseX();
		posY = input.getMouseY();
		
		//------------------------------------------------------------------------
		//finds the tile x and y value that the mouse is currently hovering over
		//------------------------------------------------------------------------
		
		if(posX <= 928){
			xTile = (int) Math.floor(posX/32); yTile = (int) Math.floor(posY/32);
		}
		
		//------------------------------------------------------------------------------
		//This will check what sidebar item was click and set a button to be dragged.		
		//------------------------------------------------------------------------------

		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			
			//Angle calc found at http://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
			//My original used simply atan and was not calculating right for some reason, this one works!
			//Calculates angle around the board from center to where mouse currently is
			theta = (float) Math.toDegrees(Math.atan2(posX - 464, posY - 400));
		    if(theta < 0){
		        theta += 360;
		    }
		    
			if(currentDragged == 0) {
				if(posX <= 1100 && posX >= 1000 && posY >= 200 && posY <= 285){
					currentDragged = 1;
				} else if(posX <= 1100 && posX >= 1000 && posY >= 350 && posY <= 435){
					currentDragged = 3;
				} else if(posX <= 1070 && posX >= 1025 && posY >= 500 && posY <= 570){
					currentDragged = 5;
				} else if(posX <= 1225 && posX >= 1175 && posY >= 500 && posY <= 565){
					currentDragged = 6;
				} else if(posX <= 1200 && posX >= 1060 && posY >= 620 && posY <= 715){
					currentDragged = 7;
				}
			}
			
		} else {
		//------------------------------------------------------------
		//checks for a mouse button release to perform an action
		//------------------------------------------------------------
			
			if(posX <= 928 && currentDragged != 0){
				
				if(currentDragged == 1 && shield > 0){
					//shield dropped on character
					if(posX <= 928 && tileSet[xTile][yTile].getPlayerType() > 0 && posY <= 800){
						tileSet[xTile][yTile].setShield();
						shield--;
					}
					
				} else if(currentDragged == 3 && heal > 0){
					//heal a character
					if(posX <= 928 && tileSet[xTile][yTile].getPlayerType() > 0 && posY <= 800){
						tileSet[xTile][yTile].setHeal();
						heal--;
					}
				} else if(currentDragged == 5 && swordsman > 0){
					gridChange = true;

					if(posX <= 928 && tileSet[xTile][yTile].getCollision() != 1) {
						tileSet[xTile][yTile].setCharacter(1);
						swordsman--;
					}
					
				} else if(currentDragged == 6 && archer > 0){
					gridChange = true;

					if(posX <= 928 && tileSet[xTile][yTile].getCollision() != 1) {
						tileSet[xTile][yTile].setCharacter(2);
						archer--;
					}
					
				} else if(currentDragged == 7 && wall > 0){
					gridChange = true;
					wall--;
					//rotate wall based on theta
					if((theta >= 135 && theta <= 225) || (theta >= 315 || theta <= 45)){
						tileSet[xTile][yTile].setWall(1);
						tileSet[xTile-1][yTile].setWall(1);
						tileSet[xTile+1][yTile].setWall(1);
						
					} else if (theta >= 225 && theta <= 315){
						tileSet[xTile][yTile].setWall(2);
						tileSet[xTile][yTile-1].setWall(2);
						tileSet[xTile][yTile+1].setWall(2);
						
					} else if (theta >= 45 && theta <= 135){
						tileSet[xTile][yTile].setWall(3);
						tileSet[xTile][yTile-1].setWall(3);
						tileSet[xTile][yTile+1].setWall(3);
					}		
					
				}
			}
			currentDragged = 0;
		}
		
		
		//-------------------------------------------------
		//Choose new units to give player
		//-------------------------------------------------
		if(unitCountdown < 0){
			newUnits();	
			unitCountdown = 10000;
		}
		
		
		//----------------------------------
		//adjust countdowns
		//----------------------------------
		countdown -= delta;
		attackCountdown -= delta;
		unitCountdown -= delta; 
		showCardCountdown -= delta;
		
		if(monsterQuantity > 0) {
			time = (int) Math.floor((countdown*10)/10000);
		} else {
			time = 0;
		}
		//-------------------------------
		//Add monsters to the screen
		//-------------------------------
		if(countdown < 0 && monsterQuantity > 0){
			generateMonsterLoc();
			monsters.add(new EnemyCharacters(1, (monsterX * 32), (monsterY * 32)+5));
			countdown = 11000;
			monsterQuantity--;
		}
	
		//------------------------------------------------------------------------
		//Monsters follow path
		//------------------------------------------------------------------------

		for(Iterator<Node> n = dijkstraGraph.iterator(); n.hasNext();){
			Node cycleNode = n.next();
			
			for(Iterator<EnemyCharacters> e = monsters.iterator(); e.hasNext();){
			
				EnemyCharacters enemy = e.next();			
				int myX = (int) Math.floor(enemy.getX()/32); int myY = (int) Math.floor(enemy.getY()/32);
				float vx, vy;
				
				if(enemy.health <= 0){
					e.remove();
				}
				
				//------------------------------------------------------------------------
				//check for collisions between static tile characters and moving entity
				//------------------------------------------------------------------------
				if(tileSet[cycleNode.x][cycleNode.y].getCollision() == 1 && tileSet[cycleNode.x][cycleNode.y].pc != null){
					int distX = cycleNode.x - myX;
					int distY = cycleNode.y - myY;
					double distFight = Math.sqrt(distX*distX + distY*distY);
					
					if(distFight < 1.1 && tileSet[cycleNode.x][cycleNode.y].getPlayerType() > 0){
						enemy.inCombat = true;
						enemy.attackingX = cycleNode.x; enemy.attackingY = cycleNode.y;
						attacking.add(enemy);
					} else if (distFight < .5 && tileSet[cycleNode.x][cycleNode.y].getPlayerType() == 0){
						enemy.inCombat = true;
						enemy.attackingX = cycleNode.x; enemy.attackingY = cycleNode.y;
						attacking.add(enemy);
					}

					if (distFight > 1.1 && tileSet[cycleNode.x][cycleNode.y].getPlayerType() == 2 && attackCountdown < 0){
						if(distFight < 4){
							if(tileSet[cycleNode.x][cycleNode.y].rollAttack() > enemy.getAC()){
									int damage = tileSet[cycleNode.x][cycleNode.y].rollDamage();
									System.out.println("archer damage is " + damage);
									enemy.characterHit(damage);
									float archerTheta = (float) Math.toDegrees(Math.atan2(myX-cycleNode.x, myY-cycleNode.y));
								    if(archerTheta < 0){
								    	archerTheta += 360;
								    }
									projectiles.add(new Projectile(1, cycleNode.x*32, cycleNode.y *32, myX, myY, Vector.getVector(archerTheta+90, (float) .7), archerTheta));
							}
						}
					}
				}
				 
				//------------------------------------------------------------------------
				//Check to find a vector for character to move. Uses dijkstra graph
				//------------------------------------------------------------------------
				if(cycleNode.x == myX && cycleNode.y == myY && enemy.inCombat == false){
					if((cycleNode.px - myX) == 0){
						vx = .0f;
					} else if ((cycleNode.px - myX) > 0){
						vx = .02f;
					} else {
						vx = -.02f;
					}
					
					if((cycleNode.py - myY) == 0){
						vy = .0f;
					} else if ((cycleNode.py - myY) > 0){
						vy = .02f;
					} else {
						vy = -.02f;
					}
					enemy.setVelocity(vx, vy);
					enemy.update(delta);
				} else if (enemy.inCombat == true){
					
					//-------------------------------------------------------------
					//If the enemy is in combat it will set the velocity to 0
					//-------------------------------------------------------------
					enemy.setVelocity(.0f, .0f);
					enemy.update(delta);
				}
			}
		}
		

		//------------------------------------------------------------------------
		//Fighting takes place here
		//------------------------------------------------------------------------
			
			for(Iterator<EnemyCharacters> a = attacking.iterator(); a.hasNext();){
				EnemyCharacters enemyAttack = a.next();

				if(attackCountdown < 0){

					if(enemyAttack.rollAttack()  > tileSet[enemyAttack.attackingX][enemyAttack.attackingY].getAC()){
						tileSet[enemyAttack.attackingX][enemyAttack.attackingY].characterHit(enemyAttack.rollDamage());
					}
					if(tileSet[enemyAttack.attackingX][enemyAttack.attackingY].rollAttack() > enemyAttack.getAC()){
						if(tileSet[enemyAttack.attackingX][enemyAttack.attackingY].getPlayerType() != 0){
							enemyAttack.characterHit(tileSet[enemyAttack.attackingX][enemyAttack.attackingY].rollDamage());
						}
					}
					attackCountdown = 1000;
				}
				if(tileSet[enemyAttack.attackingX][enemyAttack.attackingY].pc == null){
					enemyAttack.inCombat=false;
					a.remove();
					gridChange = true;
				} else if(enemyAttack.health <= 0){
					a.remove();
				}
			}	
		
			for(Iterator<Projectile> p = projectiles.iterator(); p.hasNext();){
				Projectile proj = p.next();
				int currentX = (int) Math.floor(proj.getX()/32); int currentY = (int) Math.floor(proj.getY()/32);
				if(currentX == proj.destX && currentY == proj.destY || proj.getX() > 928 || proj.getX() < 0 || proj.getY() > 800 || proj.getY() < 0){
					p.remove();
				} else {
					proj.update(delta);
				}
			}
			
			//------------------------------------------------------------------------------------
			//if a change is indicated on the grid it rebuilds the graph and runs dijkstra again
			//------------------------------------------------------------------------------------
			if(gridChange){
				buildGraph();
				gridChange = false;
			}
			
			//-----------------------------------------------
			//Check for level cheats or debug keys pressed
			//-----------------------------------------------
			
			if((input.isKeyDown(Input.KEY_LSHIFT) && input.isKeyDown(Input.KEY_1))){
				Menu.setLevel(1);
				myGame.enterState(Game.MENU);
			} else if (input.isKeyDown(Input.KEY_LSHIFT) && input.isKeyDown(Input.KEY_2)){
				Menu.setLevel(2);
				myGame.enterState(Game.MENU);
			}
			
			if(input.isKeyDown(Input.KEY_D) && input.isKeyPressed(Input.KEY_1)){
				debug = !debug;
			} else if (input.isKeyDown(Input.KEY_D) && input.isKeyPressed(Input.KEY_2)){
				debugGrid = !debugGrid;
			} else if (input.isKeyDown(Input.KEY_D) && input.isKeyPressed(Input.KEY_3)){
				debugDijkstra = !debugDijkstra;
			} else if (input.isKeyDown(Input.KEY_D) && input.isKeyPressed(Input.KEY_4)){
				debugCollisions = !debugCollisions;
			} else if (input.isKeyDown(Input.KEY_D) && input.isKeyPressed(Input.KEY_5)){
				debugWeight = !debugWeight;
			} 
			
			if(attackCountdown < 0){
				attackCountdown = 1000;
			}
		
			if(monsterQuantity == 0 && monsters.isEmpty()){
				Menu.setLevel(2);
				myGame.enterState(Game.MENU);
			} else if (tileSet[xCleric][yCleric].pc == null){
				Menu.setLevel(0);
				myGame.enterState(Game.MENU);
			}
	}
	
	
	
	
	
//===================================================================================================================
//											Support functions 
//===================================================================================================================
	
	
	//this builds a new graph based on changes made to the map and will rerun dijkstras to produce a usable graph
	public static void buildGraph(){

		graph = new Graph();

		for(Iterator<Node> i = graph.nodes.iterator(); i.hasNext();){
			Node n = i.next();
			for(Iterator<Edge> j = n.edges.iterator(); j.hasNext();){
				Edge e = j.next();
				e.weight = tileSet[e.myX][e.myY].getWeight();
			}
		}		
		
		dijkstraGraph = Dijkstra.runDijkstra(graph, xCleric, yCleric);		

	}
	
	
	//Finds a place to put a monster on the game baord
	public void generateMonsterLoc(){
		Random r = new Random();
		int xory = r.nextInt(2);
		
		if(xory == 0){
			//on x axis
			r = new Random();
			int topOrBottom = r.nextInt(2);
			int tileLoc = r.nextInt(29);
			monsterX = tileLoc;
			if(topOrBottom == 0){
				//top of grid
				monsterY = 0;
			} else {
				//bottom of grid
				monsterY = 24;
			}
			
		} else {
			//on y axis
			r = new Random();
			int leftOrRight = r.nextInt(2);
			int tileLoc = r.nextInt(25);
			monsterY = tileLoc;
			if(leftOrRight == 0){
				//left of grid
				monsterX = 0;
			} else {
				//right of grid
				monsterX = 28;
			}
		}
	}
	
	
	public void newUnits(){
		for(int i = 0; i < 2; i++){
			Random r = new Random();
			int unit = (r.nextInt(100)+1);
			
			if(unit <= 30){
				//swordsman
				swordsman++;
				swordCard = true;
				showCardCountdown = 1500;
			} else if (unit > 30 && unit <= 50){
				//wall
				wall++;
			} else if (unit > 50 && unit <= 70){
				//shield
				shield++;
			} else if (unit > 70 && unit <= 85){
				//heal
				heal++;
			} else if (unit > 85 && unit <= 100){
				//archer
				archer++;
				archerCard = true;
				showCardCountdown = 1500;
			}
		}	
	}
	
	public static void initVars(){
		time = 10;
		massHX = 0; massHY = 0;
		gridChange = false;
		xCleric = 14; yCleric = 12;
		
		countdown = 1000; monsterQuantity = 30; attackCountdown = 4000; showCardCountdown = 1500;
		
		debug = false; debugGrid = true; debugCollisions = false; debugDijkstra = true; debugWeight = false;
		heal = 0; massHeal = 0; shield = 0; bolt = 0; swordsman = 0; archer = 0; wall = 0;
		swordCard = false; archerCard = false; clericCard = true;
		
		monsters = new ArrayList<EnemyCharacters>();
		attacking = new ArrayList<EnemyCharacters>();
		projectiles = new ArrayList<Projectile>();
		
				//------------------------------------------------------------------------------
				//cycle through collisions layer and mark any tiles with a collision as such
				//------------------------------------------------------------------------------
				
				for(int i = 0; i < 29; i++){
					for(int j = 0; j < 25; j++){
						if(map.getTileId(i, j, map.getLayerIndex("Collision")) > 0){
							tileSet[i][j] = new Tile(); 
							tileSet[i][j].setCollision();
							tileSet[i][j].setWeight(100);
						} else {
							tileSet[i][j] = new Tile(); 
							tileSet[i][j].setWeight(1);
						}
					}
				}

				//------------------------------------------------
				//sets the tile where the cleric will be placed
				//------------------------------------------------
				
				tileSet[xCleric][yCleric].setCharacter(3);

				//----------------------
				//set castle walls.
				//----------------------
				

				tileSet[13][10].setWall(1);tileSet[14][10].setWall(1);tileSet[15][10].setWall(1);
				tileSet[13][14].setWall(1);tileSet[14][14].setWall(1);tileSet[15][14].setWall(1);
				
				tileSet[12][11].setWall(2);tileSet[12][12].setWall(2);tileSet[12][13].setWall(2);
				
				tileSet[16][11].setWall(3);tileSet[16][12].setWall(3);tileSet[16][13].setWall(3);
				
				buildGraph();
		
	}
	
	@Override
	public int getID() {
		return Game.LEVELONE;
	}
}


