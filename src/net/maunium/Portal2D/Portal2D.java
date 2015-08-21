package net.maunium.Portal2D;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Renderer.BlockType;

/**
 * Main class for the Portal 2(D) game.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Portal2D extends BasicGame {
	private Map map;
	
	public Portal2D() {
		super("Portal 2(D)");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		Renderer.loadImage(BlockType.DARK, new Color(1, 2, 3), getImage("blocks/tile_dark"));
		Renderer.loadImage(BlockType.LIGHT, new Color(1, 2, 3), getImage("blocks/tile_light"));
		map = new Map(this, getImage("maps/default"));
	}
	
	@Override
	public void update(GameContainer gc, int i) throws SlickException {
	
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		map.render(g);
	}
	
	/**
	 * Get the image in the given path (without .png) from the jar.
	 */
	Image getImage(String name) throws SlickException {
		return new Image(this.getClass().getClassLoader().getResourceAsStream("res/" + name + ".png"), name, false);
	}
	
	public static void main(String[] args) {
		try {
			AppGameContainer agc;
			agc = new AppGameContainer(new Portal2D());
			agc.setDisplayMode(640, 480, false);
			agc.start();
		} catch (SlickException e) {
			System.err.println("Fatal error:");
			e.printStackTrace();
		}
	}
}
