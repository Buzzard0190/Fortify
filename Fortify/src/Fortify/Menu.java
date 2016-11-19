package Fortify;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;


public class Menu extends BasicGameState{

	private static int levelNumber = 1;
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
		if(levelNumber == 1){
			g.setColor(Color.white);
			g.drawString("MENU HERE LEVEL 1", 400, 400);
			g.drawImage(ResourceManager.getImage(Game.MENU_ONE_RSC), 0,0);
		} else if (levelNumber == 2){
			g.setColor(Color.white);
			g.drawImage(ResourceManager.getImage(Game.MENU_TWO_RSC), 0,0);
		} else if (levelNumber == 3){
			g.setColor(Color.white);
			g.drawImage(ResourceManager.getImage(Game.MENU_END_RSC), 0,0);
		} else if (levelNumber == 0){
			g.setColor(Color.white);
			g.drawImage(ResourceManager.getImage(Game.MENU_GOVER_RSC), 0,0);
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Game myGame = (Game) game;
		Input input = container.getInput();

		if(levelNumber == 1){
			if(input.isKeyDown(Input.KEY_SPACE)){
				LevelOne.initVars();
				myGame.enterState(Game.LEVELONE);
			}
		} else if (levelNumber == 2){
			if(input.isKeyDown(Input.KEY_SPACE)){
				LevelTwo.initVars();
				myGame.enterState(Game.LEVELTWO);
			}
		} else if (levelNumber == 3){
			if(input.isKeyDown(Input.KEY_SPACE)){
				levelNumber = 1;
				myGame.enterState(Game.MENU);
			}
		} else if (levelNumber == 0){
			if(input.isKeyDown(Input.KEY_SPACE)){
				levelNumber = 1;
				myGame.enterState(Game.MENU);
			}
		}
		
		
	}
	
	static public void setLevel(int i){
		levelNumber = i;
	}

	static public int getLevel(){
		return levelNumber;
	}
	
	@Override
	public int getID() {
		return Game.MENU;
	}

}
