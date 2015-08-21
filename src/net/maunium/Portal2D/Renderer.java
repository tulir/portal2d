package net.maunium.Portal2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Block registry. Maps Color -> BlockType and BlockType -> Image.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Renderer {
	/** Contains the BlockType -> Image mappings. */
	private static Map<BlockType, Image> images = new HashMap<BlockType, Image>();
	/** Contains the Color -> BlockType mappings. */
	private static Map<Color, BlockType> colors = new HashMap<Color, BlockType>();
	
	/**
	 * Render the the texture of the given block type at the given coordinates using the given Graphics object.
	 */
	public static void render(Graphics g, BlockType bt, int x, int y) {
		g.drawImage(images.get(bt), x * 32, y * 32);
	}
	
	/**
	 * Get the block type matching the given color.
	 * 
	 * @return The block type, or null if color couldn't be identified.
	 */
	public static BlockType getBlockType(Color c) {
		for (Entry<Color, BlockType> e : colors.entrySet()) {
			if (e.getKey().equals(c)) return e.getValue();
		}
		return null;
	}
	
	/**
	 * Map the given color and image to the given block type.
	 */
	public static void loadImage(BlockType bt, Color c, Image i) {
		images.put(bt, i);
		colors.put(c, bt);
	}
	
	/**
	 * Temporary enum to store the type of the block. This should be changed to something more modular.
	 */
	public static enum BlockType {
		LIGHT, DARK, SPIKE;
	}
}
