package net.maunium.Portal2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
		addState(new Menu());
		
		BlockRenderer.loadImage(BlockType.DARK, new Color(50, 50, 50), getImage("blocks/tile_dark"));
		BlockRenderer.loadImage(BlockType.LIGHT, new Color(200, 200, 200), getImage("blocks/tile_light"));
		BlockRenderer.loadImage(BlockType.POINT, new Color(0, 100, 0), getImage("blocks/point"));
		BlockRenderer.loadImage(BlockType.FINISH, new Color(0, 255, 0), getImage("blocks/finish"));
		BlockRenderer.loadImage(BlockType.BOMB, new Color(255, 0, 255), getImage("blocks/bomb"));
		
		int i = 100;
		for (File f : getMaps()) {
			String name = f.getName().split(Pattern.quote("."), 3)[1];
			try {
				addState(new Map(this, new Image(new FileInputStream(f), name, false), name, i++));
			} catch (FileNotFoundException e) {
				System.err.println("Failed to load map " + name + ":");
				e.printStackTrace();
			}
		}
	}
	
	public List<File> getMaps() {
		List<File> files = new ArrayList<File>();
		File f = new File(ResourceLoader.getResource("res/maps").getFile());
		for (File ff : f.listFiles())
			files.add(ff);
		f = new File(System.getProperty("user.home") + "/.portal2d");
		if (f.isFile()) f.delete();
		if (!f.exists()) f.mkdir();
		for (File ff : f.listFiles())
			files.add(ff);
		return files;
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
