package net.maunium.Portal2D;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import net.maunium.Portal2D.BlockRenderer.BlockType;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Map.Player;

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
		BlockRenderer.loadImage(BlockType.DARK, new Color(50, 50, 50), getImage("blocks/tile_dark"));
		BlockRenderer.loadImage(BlockType.LIGHT, new Color(200, 200, 200), getImage("blocks/tile_light"));
		map = new Map(this, getImage("maps/map1"));
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Player p = map.getPlayer();
		
		if (gc.getInput().isKeyDown(Keyboard.KEY_A)) p.dx = -0.005f;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_D)) p.dx = 0.005f;
		else p.dx = 0.0f;
		
		if (gc.getInput().isKeyDown(Keyboard.KEY_SPACE) && p.dy == 0 && map.getBlockAt((int) (p.x + 0.5), (int) p.y + 1) != null) p.dy = 0.004f;
		// TODO: Proper collision checking
//		else if (p.dy == 0 && ((int) p.x < p.x || map.getBlockAt((int) (p.x + 0.5), (int) p.y + 1) == null)) p.dy = -0.01f;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_LSHIFT)) p.dy = -0.004f;
		else if (p.dy > 0.0f) p.dy -= 0.0001f;
		else if (p.dy < 0.0f) p.dy = 0.0f;
		
		p.x += delta * p.dx;
		p.y -= delta * p.dy;
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		map.render(g);
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
			agc.setDisplayMode(12 * 32, 12 * 32, false);
			agc.start();
		} catch (SlickException e) {
			System.err.println("Fatal error:");
			e.printStackTrace();
		}
	}
}
