package net.maunium.Portal2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Map.PortalBullet;

/**
 * Main class for the Portal 2(D) game.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Portal2D extends StateBasedGame {
	public int points = 0;
	/* Default tile IDs */
	public static final int TILE_DARK = 1, TILE_LIGHT = 2, TILE_POINT = 3, TILE_FINISH = 4, TILE_BOMB = 5, TILE_NONE = 0;
	
	public Portal2D() {
		super("Portal 2(D)");
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// Add the menu state.
		addState(new Menu(this));
		
		// Register the default blocks.
		BlockRegistry.registerBlock(TILE_DARK, new Color(50, 50, 50), getImage("blocks/tile_dark"), true);
		BlockRegistry.registerBlock(TILE_LIGHT, new Color(200, 200, 200), getImage("blocks/tile_light"), true);
		BlockRegistry.registerBlock(TILE_POINT, new Color(0, 100, 0), getImage("blocks/point"), false);
		BlockRegistry.registerBlock(TILE_FINISH, new Color(0, 255, 0), getImage("blocks/finish"), false);
		BlockRegistry.registerBlock(TILE_BOMB, new Color(255, 0, 255), getImage("blocks/bomb"), true);
		
		// Load the images for the portal bullets.
		PortalBullet.BLUE_BULLET = getImage("bullet_blue");
		PortalBullet.ORANGE_BULLET = getImage("bullet_orange");
		
		loadMaps();
	}
	
	public void loadMaps() throws SlickException {
		int i = 100;
		// Get the file pointing to the res/maps directory embedded in the jar.
//		File defMaps = new File(ResourceLoader.getResource("res/maps").getFile());
		File defMaps = new File("./maps");
		// Loop through map files.
		for (File f : sort(defMaps.listFiles())) {
			// Get the name of the map.
			String name = f.getName().split(Pattern.quote("."), 3)[1];
			// Try to add the map as a state.
			try {
				addState(new Map(this, new Image(new FileInputStream(f), name, false), name, i++));
			} catch (FileNotFoundException e) {
				System.err.println("Failed to load map " + name + ":");
				e.printStackTrace();
			}
		}
		
		File userMaps = new File(System.getProperty("user.home") + "/.portal2d");
		if (userMaps.isDirectory() && userMaps.exists()) {
			for (File f : sort(userMaps.listFiles())) {
				if (!f.getName().endsWith(".png")) continue;
				// Get the name of the map.
				String name = f.getName().split(Pattern.quote("."), 2)[0];
				// Try to add the map as a state.
				try {
					addState(new Map(this, new Image(new FileInputStream(f), name, false), name, i++));
				} catch (FileNotFoundException e) {
					System.err.println("Failed to load map " + name + ":");
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<File> sort(File[] f) {
		if (f == null || f.length == 0) return new ArrayList<File>();
		List<File> fs = Arrays.asList(f);
		Collections.sort(fs);
		return fs;
	}
	
	/**
	 * Get the image in the given path (without .png) from the jar.
	 */
	public Image getImage(String name) throws SlickException {
//		return new Image(ResourceLoader.getResourceAsStream("res/" + name + ".png"), name, false);
		try {
			return new Image(new FileInputStream(new File("./res/" + name + ".png")), name, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
//		System.setProperty("org.lwjgl.librarypath", new File(ResourceLoader.getResource("natives").getFile()).getAbsolutePath());
		System.setProperty("org.lwjgl.librarypath", new File("./natives").getAbsolutePath());
		
		try {
			// Create the game container.
			AppGameContainer agc = new AppGameContainer(new Portal2D());
			Display.setResizable(true);
			// Set the default resolution to 480p.
			agc.setDisplayMode(640, 480, false);
			// Enable vsync.
			agc.setVSync(true);
			// Set tick rate to 20.
			agc.setMaximumLogicUpdateInterval(20);
			agc.setMinimumLogicUpdateInterval(20);
			// Don't show FPS.
			agc.setShowFPS(false);
			// Start the game.
			agc.start();
		} catch (SlickException e) {
			// Print stack trace if an error occurs.
			System.err.println("Fatal error:");
			e.printStackTrace();
		}
	}
}
