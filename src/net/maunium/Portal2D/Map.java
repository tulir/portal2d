package net.maunium.Portal2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Renderer.BlockType;

/**
 * Container for maps.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Map {
	private BlockType[][] blocks;
	private Portal portal_blue, portal_orange;
	private Player p;
	
	/**
	 * Construct a map based on the given Image.
	 */
	public Map(Portal2D host, Image img) throws SlickException {
		byte[] data = img.getTexture().getTextureData();
		p = new Player(host.getImage("player"));
		
		portal_blue = new Portal(host.getImage("blocks/portal_blue"));
		portal_orange = new Portal(host.getImage("blocks/portal_orange"));
		
		blocks = new BlockType[img.getWidth()][];
		for (int x = 0; x < img.getWidth(); x++) {
			blocks[x] = new BlockType[img.getHeight()];
			for (int y = 0; y < img.getHeight(); y++) {
				int r = data[(y * img.getWidth() + x) * 4 + 0];
				int g = data[(y * img.getWidth() + x) * 4 + 1];
				int b = data[(y * img.getWidth() + x) * 4 + 2];
				
				// The player spawn point
				if (r == 69 && g == 123 && b == 321) {
					p.setX(x);
					p.setY(y);
				} else blocks[x][y] = Renderer.getBlockType(r, g, b);
			}
		}
	}
	
	/**
	 * Render this map.
	 */
	public void render(Graphics g) {
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				Renderer.render(g, blocks[x][y], x, y);
			}
		}
		
		p.render(g);
		
		portal_blue.render(g);
		portal_orange.render(g);
	}
}
