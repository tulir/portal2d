package net.maunium.Portal2D;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import net.maunium.Portal2D.BlockRenderer.BlockType;
import net.maunium.Portal2D.Map.Map;

/**
 * Main class for the Portal 2(D) game.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Portal2D extends StateBasedGame {
	public Portal2D() {
		super("Portal 2(D)");
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		BlockRenderer.loadImage(BlockType.DARK, new Color(50, 50, 50), getImage("blocks/tile_dark"));
		BlockRenderer.loadImage(BlockType.LIGHT, new Color(200, 200, 200), getImage("blocks/tile_light"));
		addState(new Menu());
		addState(new Map(this, getImage("maps/map1"), 1));
	}
	
	/**
	 * Get the image in the given path (without .png) from the jar.
	 */
	public Image getImage(String name) throws SlickException {
		return new Image(ResourceLoader.getResourceAsStream("/res/" + name + ".png"), name, false);
	}
	
	public static void main(String[] args) {
		try {
			AppGameContainer agc;
			agc = new AppGameContainer(new Portal2D());
			agc.setDisplayMode(640, 480, false);
			agc.setVSync(true);
			agc.setShowFPS(false);
			agc.start();
		} catch (SlickException e) {
			System.err.println("Fatal error:");
			e.printStackTrace();
		}
	}
}
