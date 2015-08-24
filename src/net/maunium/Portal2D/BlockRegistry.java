package net.maunium.Portal2D;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.maunium.Portal2D.Blocks.AbstractBlock;

/**
 * Block registry. Maps Color -> BlockType and BlockType -> Image.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class BlockRegistry {
	private static Map<Integer, AbstractBlock> blocks = new HashMap<Integer, AbstractBlock>();
	/** Contains the Color -> ID mappings. */
	private static Map<Color, Integer> colors = new HashMap<Color, Integer>();
	public static final int TILE_NULL = 0;
	
	/**
	 * Render the the texture of the given block ID at the given coordinates using the given Graphics object.
	 * 
	 * @param g The graphics object to use.
	 * @param id The integer ID of the block.
	 * @param x The X coordinate of the block
	 * @param y The Y coordinate of the block
	 */
	public static void render(Graphics g, int id, int x, int y, int shiftX, int shiftY) {
		if (blocks.containsKey(id)) {
			blocks.get(id).render(g, x * 32 + shiftX, y * 32 + shiftY);
		}
	}
	
	/**
	 * Get the block ID matching the given color.
	 * 
	 * @param c The color registered to the block.
	 * @return The block name, or null if color couldn't be identified.
	 */
	public static int getBlockId(Color c) {
		if (colors.containsKey(c)) return colors.get(c);
		else return TILE_NULL;
	}
	
	public static AbstractBlock getBlockHandler(int id) {
		if (blocks.containsKey(id)) return blocks.get(id);
		else return null;
	}
	
	/**
	 * Map the given color and image to the given block name.
	 */
	public static void registerBlock(int id, Color c, AbstractBlock block) {
		colors.put(c, id);
		blocks.put(id, block);
	}
	
	public static boolean canWalkThrough(int id) {
		if (id <= TILE_NULL || !blocks.containsKey(id)) return true;
		else return blocks.get(id).canWalkThrough();
	}
	
	public static boolean canShootThrough(int id) {
		if (id <= TILE_NULL || !blocks.containsKey(id)) return true;
		else return blocks.get(id).canShootThrough();
	}
	
	public static boolean canAttachPortal(int id) {
		if (id <= TILE_NULL || !blocks.containsKey(id)) return false;
		else return blocks.get(id).canAttachPortal();
	}
}
