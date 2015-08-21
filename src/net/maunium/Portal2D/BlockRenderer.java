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
public class BlockRenderer {
	private static Map<BlockType, Image> images = new HashMap<BlockType, Image>();
	private static Map<Color, BlockType> colors = new HashMap<Color, BlockType>();
	
	public static void render(Graphics g, BlockType bt, int x, int y) {
		g.drawImage(images.get(bt), x * 32, y * 32);
	}
	
	public static BlockType getBlockType(int r, int g, int b) {
		Color c = new Color(r, g, b);
		for (Entry<Color, BlockType> e : colors.entrySet()) {
			if (e.getValue().equals(c)) return e.getValue();
		}
		return null;
	}
	
	public static void loadImage(BlockType bt, Color c, Image i) {
		images.put(bt, i);
		colors.put(c, bt);
	}
	
	public static enum BlockType {
		LIGHT, DARK, SPIKE;
	}
}
