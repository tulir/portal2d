package net.maunium.Portal2D;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Block registry. Maps Color -> BlockType and BlockType -> Image.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class BlockRegistry {
	/** Contains the ID -> Image mappings. */
	private static Map<Integer, Image> images = new HashMap<Integer, Image>();
	/** Contains the ID -> Solid flag mappings. */
	private static Map<Integer, Boolean> solid = new HashMap<Integer, Boolean>();
	/** Contains the Color -> ID mappings. */
	private static Map<Color, Integer> colors = new HashMap<Color, Integer>();
	
	/**
	 * Render the the texture of the given block ID at the given coordinates using the given Graphics object.
	 * 
	 * @param g The graphics object to use.
	 * @param id The integer ID of the block.
	 * @param x The X coordinate of the block
	 * @param y The Y coordinate of the block
	 */
	public static void render(Graphics g, int id, int x, int y) {
		if (images.containsKey(id)) g.drawImage(images.get(id), x * 32, y * 32);
	}
	
	/**
	 * Get the block ID matching the given color.
	 * 
	 * @param c The color registered to the block.
	 * @return The block name, or null if color couldn't be identified.
	 */
	public static int getBlockId(Color c) {
		if (colors.containsKey(c)) return colors.get(c);
		else return -1;
	}
	
	/**
	 * Map the given color and image to the given block name.
	 */
	public static void registerBlock(int id, Color c, Image i, boolean s) {
		images.put(id, i);
		colors.put(c, id);
		solid.put(id, s);
	}
	
	/**
	 * Get whether or not the block with the given id is solid.
	 */
	public static boolean isSolid(int id) {
		if (id <= Portal2D.TILE_NONE || !solid.containsKey(id)) return false;
		else return solid.get(id);
	}
}
