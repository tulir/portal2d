package net.maunium.Portal2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.Blocks.BlockDark;
import net.maunium.Portal2D.Blocks.BlockLight;
import net.maunium.Portal2D.Blocks.Bomb;
import net.maunium.Portal2D.Blocks.Bullethole;
import net.maunium.Portal2D.Blocks.Finish;
import net.maunium.Portal2D.Blocks.Point;
import net.maunium.Portal2D.Blocks.PortalDestroyer;
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
	
	public Portal2D() {
		super("Portal 2(D)");
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// Add the menu state.
		addState(new Menu(this));
		
		// Register the default blocks.
		BlockRegistry.registerBlock(BlockDark.ID, BlockDark.COLORID, new BlockDark(this));
		BlockRegistry.registerBlock(BlockLight.ID, BlockLight.COLORID, new BlockLight(this));
		BlockRegistry.registerBlock(Point.ID, Point.COLORID, new Point(this));
		BlockRegistry.registerBlock(Finish.ID, Finish.COLORID, new Finish(this));
		BlockRegistry.registerBlock(Bomb.ID, Bomb.COLORID, new Bomb(this));
		BlockRegistry.registerBlock(PortalDestroyer.ID, PortalDestroyer.COLORID, new PortalDestroyer(this));
		BlockRegistry.registerBlock(Bullethole.ID, Bullethole.COLORID, new Bullethole(this));
		
		// Load the images for the portal bullets.
		PortalBullet.BLUE_BULLET = getImage("bullet_blue");
		PortalBullet.ORANGE_BULLET = getImage("bullet_orange");
		
		loadMaps();
	}
	
	public void loadMaps() throws SlickException {
		int i = 100;
		// Get the file pointing to the res/maps directory embedded in the jar.
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
		} else {
			userMaps.delete();
			userMaps.mkdir();
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
		try {
			return new Image(new FileInputStream(new File("./res/" + name + ".png")), name, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("./natives").getAbsolutePath());
		
		try {
			// Create the game container.
			AppGameContainer agc = new AppGameContainer(new Portal2D());
			// Set the resolution to 39 * 25 blocks.
			agc.setDisplayMode(32 * 39, 32 * 25, false);
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
