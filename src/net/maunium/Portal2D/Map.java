package net.maunium.Portal2D;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.BlockRenderer.BlockType;

/**
 * Container for maps.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Map {
	private BlockType[][] blocks;
	
	public Map(Image img) throws SlickException {
		byte[] data = img.getTexture().getTextureData();
		
		for (int i = 0; i < data.length; i += 4) {
			int r = data[i], g = data[i + 1], b = data[i + 2];
			BlockRenderer.getBlockType(r, g, b);
		}
	}
}
