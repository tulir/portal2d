package net.maunium.Portal2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Renderer.BlockType;
import net.maunium.Portal2D.Vector.SideHit;

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
				Color c = img.getColor(x, y);
				
				// The player spawn point
				if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0) {
					p.x = x;
					p.y = y;
				} else blocks[x][y] = Renderer.getBlockType(c);
			}
		}
	}
	
	/**
	 * Render this map.
	 */
	public void render(Graphics g) {
		g.setBackground(new Color(50, 50, 50));
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				if (blocks[x][y] != null) Renderer.render(g, blocks[x][y], x, y);
			}
		}
		
		p.render(g);
		
		portal_blue.render(g);
		portal_orange.render(g);
	}
	
	public BlockType getBlockAt(int x, int y) {
		return blocks[x][y];
	}
	
	public Player getPlayer() {
		return p;
	}
	
	private Vector rayTrace(double angle, int playerX, int playerY) {
		if (angle % 90 == 0) {
			angle += 1 / 10000;
		}
		Vector portalHit = new Vector(0, 0, Vector.SideHit.TOP);
		double b = Math.tan(Math.toRadians(angle));
		double c = playerY - b * playerX;
		
		Vector directionVector = new Vector(0, 0, Vector.SideHit.TOP);
		
		if (angle < 180) {
			directionVector.y = 1;
		} else {
			directionVector.y = -1;
		}
		if (angle > 90 && angle < 270) {
			directionVector.x = -1;
		} else {
			directionVector.x = 1;
		}
		
		double currentX = playerX;
		int nextVerticalLimit = 0; // Y-position of the limit
		int nextHorizontalLimit = 0; // X-position of the limit
		double distToVertLim;
		double distToHorLim;
		while (true) {
			// Getting the next limits.
			// x
			if (currentX % 32 == 0) {
				nextHorizontalLimit = (int) (currentX / 32 + directionVector.x) * 32;
			} else {
				nextHorizontalLimit = (int) (currentX - currentX % 32);
				if (directionVector.x > 0) {
					nextHorizontalLimit += 32;
				}
			}
			
			// y
			if ((currentX * b + c) % 32 == 0) {
				nextVerticalLimit = (int) ((currentX * b + c) / 32 + directionVector.y) * 32;
			} else {
				nextVerticalLimit = (int) (currentX * b + c - (currentX * b + c) % 32);
				if (directionVector.y > 0) {
					nextVerticalLimit += 32;
				}
			}
			
			// Calculating the distances to the limits.
			distToVertLim = Math.abs((nextVerticalLimit - b * currentX - c) / b - currentX);
			distToHorLim = Math.abs(nextHorizontalLimit - currentX);
			
			// Testing which limit is closer.
			if (distToVertLim <= distToHorLim) {
				Renderer.BlockType blockType = blocks[(int) ((currentX - currentX % 32) / 32)][nextVerticalLimit / 32 + directionVector.y];
				playerX += directionVector.y * distToVertLim;
				if (blockType == Renderer.BlockType.LIGHT) {
					// 0 top 1 left 2 bottom 3 right
					portalHit.sideHit = SideHit.fromInt(directionVector.y + 1);
					// We get the x and y values of the blockwe're in.
					portalHit.x = (int) ((currentX - currentX % 32) / 32);
					portalHit.y = nextVerticalLimit / 32 + directionVector.y;
					return portalHit;
					// It's either null (there's nothing there) or there's a place we can add a portal or
					// There is a place we can't add a portal to. So, If we can't add a portal there we
					// Just return.
				} else if (blockType != null) return null;
			} else {
				// We get the block that is at the position we crashed to.
				Renderer.BlockType blockType = blocks[nextHorizontalLimit / 32 + directionVector.x][(int) ((b * currentX + c - (b * currentX + c) % 32) / 32)];
				playerX += directionVector.x * distToVertLim;
				// If we can place a portal on the surface, we do so, returning the surface.
				if (blockType == Renderer.BlockType.LIGHT) {
					// 0 top 1 left 2 bottom 3 right
					portalHit.sideHit = SideHit.fromInt(directionVector.x + 2);
					portalHit.x = nextHorizontalLimit / 32 + directionVector.x;
					// From x we calculate y.
					portalHit.y = (int) ((b * currentX + c - (b * currentX + c) % 32) / 32);
					return portalHit;
					// It's either null (there's nothing there) or there's a place we can add a portal or
					// There is a place we can't add a portal to. So, If we can't add a portal there we
					// Just return.
				} else if (blockType != null) return null;
			}
		}
	}
}
