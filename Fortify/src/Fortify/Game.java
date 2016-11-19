package Fortify;



import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends StateBasedGame {
	
	public static final int MENU = 0;
	public static final int LEVELONE = 1;
	public static final int LEVELTWO = 2;

	public static final String SCROLL_RSC = "Fortify/resource/scroll8.png";
	public static final String SHIELD_ANIMATION_RSC = "Fortify/resource/shield.png";

	public static final String SHIELD_ICON_RSC = "Fortify/resource/shieldIcon.png";
	public static final String MASS_HEAL_ICON_RSC = "Fortify/resource/massHealIcon.png";
	public static final String HEAL_ICON_RSC = "Fortify/resource/healIcon.png";
	public static final String GUIDING_BOLT_ICON_RSC = "Fortify/resource/guidingBoltIcon.png";

	public static final String SWORDSMAN_RSC = "Fortify/resource/swordsman.png";
	public static final String ARCHER_RSC = "Fortify/resource/archer.png";
	
	public static final String WALL_ICON_RSC = "Fortify/resource/wallIcon.png";
	public static final String WALL_RSC = "Fortify/resource/wall.png";
	public static final String WALL_LEFT_RSC = "Fortify/resource/wallLeft.png";
	public static final String WALL_RIGHT_RSC = "Fortify/resource/wallRight.png";
	
	public static final String WALL_SINGLE_RIGHT_RSC = "Fortify/resource/wallSingleRight.png";
	public static final String WALL_SINGLE_LEFT_RSC = "Fortify/resource/wallSingleLeft.png";
	public static final String WALL_SINGLE_RSC = "Fortify/resource/wallSingle.png";


	public static final String CLERIC_RSC = "Fortify/resource/cleric.png";
	public static final String GOBLIN_RSC = "Fortify/resource/goblin.png";	
	public static final String SKELLY_RSC = "Fortify/resource/skeleton.png";
	public static final String ARROW_RSC = "Fortify/resource/arrow.png";
	
	public static final String SWORD_CARD_RSC = "Fortify/resource/swordCard.png";
	public static final String ARCHER_CARD_RSC = "Fortify/resource/archerCard.png";
	public static final String CLERIC_CARD_RSC = "Fortify/resource/clericCard.png";

	public static final String MENU_ONE_RSC = "Fortify/resource/FortifyL1.png";
	public static final String MENU_TWO_RSC = "Fortify/resource/FortifyL2.png";
	public static final String MENU_END_RSC = "Fortify/resource/FortifyEnd.png";
	public static final String MENU_GOVER_RSC = "Fortify/resource/FortifyGOver.png";

	

	public final int ScreenWidth;
	public final int ScreenHeight;
	

	public Game(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
				
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new Game("Fortify", 1328, 800));
			app.setDisplayMode(1328, 800, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new Menu());
		addState(new LevelOne());
		addState(new LevelTwo());
		
		ResourceManager.loadImage(SCROLL_RSC);
		ResourceManager.loadImage(SHIELD_ANIMATION_RSC);
		
		ResourceManager.loadImage(SHIELD_ICON_RSC);
		ResourceManager.loadImage(MASS_HEAL_ICON_RSC);
		ResourceManager.loadImage(HEAL_ICON_RSC);
		ResourceManager.loadImage(GUIDING_BOLT_ICON_RSC);

		ResourceManager.loadImage(SWORDSMAN_RSC);
		ResourceManager.loadImage(ARCHER_RSC);
		
		ResourceManager.loadImage(WALL_ICON_RSC);
		ResourceManager.loadImage(WALL_RSC);
		ResourceManager.loadImage(WALL_LEFT_RSC);
		ResourceManager.loadImage(WALL_RIGHT_RSC);
		
		ResourceManager.loadImage(WALL_SINGLE_RIGHT_RSC);
		ResourceManager.loadImage(WALL_SINGLE_LEFT_RSC);
		ResourceManager.loadImage(WALL_SINGLE_RSC);
		
		ResourceManager.loadImage(CLERIC_RSC);
		
		ResourceManager.loadImage(GOBLIN_RSC);
		ResourceManager.loadImage(SKELLY_RSC);
		ResourceManager.loadImage(ARROW_RSC);
		
		ResourceManager.loadImage(SWORD_CARD_RSC);
		ResourceManager.loadImage(ARCHER_CARD_RSC);
		ResourceManager.loadImage(CLERIC_CARD_RSC);
		
		ResourceManager.loadImage(MENU_ONE_RSC);
		ResourceManager.loadImage(MENU_TWO_RSC);
		ResourceManager.loadImage(MENU_END_RSC);
		ResourceManager.loadImage(MENU_GOVER_RSC);



	}
		
}
