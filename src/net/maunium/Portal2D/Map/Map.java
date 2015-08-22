package net.maunium.Portal2D.Map;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.BlockRenderer;
import net.maunium.Portal2D.BlockRenderer.BlockType;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Util.Vector;
import net.maunium.Portal2D.Util.Vector.SideHit;

/**
 * Container for maps.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Map extends BasicGameState {
	private final int id;
	private final Image img;
	private final Portal2D host;
	private BlockType[][] blocks;
	private Portal portal_blue, portal_orange;
	private Player p;
	
	/**
	 * Construct a map based on the given Image.
	 */
	public Map(Portal2D host, Image img, int id) throws SlickException {
		this.id = id;
		this.host = host;
		this.img = img;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
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
				} else blocks[x][y] = BlockRenderer.getBlockType(c);
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setBackground(new Color(50, 50, 50));
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				if (blocks[x][y] != null) BlockRenderer.render(g, blocks[x][y], x, y);
			}
		}
		
		p.render(g);
		
		portal_blue.render(g);
		portal_orange.render(g);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		Player p = getPlayer();
		
		if (gc.getInput().isKeyDown(Keyboard.KEY_A)) p.dx = -0.005f;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_D)) p.dx = 0.005f;
		else p.dx = 0.0f;
		
		if (gc.getInput().isKeyDown(Keyboard.KEY_SPACE) && p.dy == 0 && getBlockAt((int) (p.x + 0.5), (int) p.y + 1) != null) p.dy = 0.004f;
		// TODO: Proper collision checking
//		else if (p.dy == 0 && ((int) p.x < p.x || map.getBlockAt((int) (p.x + 0.5), (int) p.y + 1) == null)) p.dy = -0.01f;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_LSHIFT)) p.dy = -0.004f;
		else if (p.dy > 0.0f) p.dy -= 0.0001f;
		else if (p.dy < 0.0f) p.dy = 0.0f;
		
		p.x += delta * p.dx;
		p.y -= delta * p.dy;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	/**
	 * Get the block type at the given location.
	 * 
	 * @return The block type, or null if no block at given location.
	 */
	public BlockType getBlockAt(int x, int y) {
		return blocks[x][y];
	}
	
	/**
	 * Get the player in this map.
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * Ray trace method. TODO: Add proper javadoc
	 * 
	 * @author Antti
	 */
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
				BlockRenderer.BlockType blockType = blocks[(int) ((currentX - currentX % 32) / 32)][nextVerticalLimit / 32 + directionVector.y];
				playerX += directionVector.y * distToVertLim;
				if (blockType == BlockRenderer.BlockType.LIGHT) {
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
				BlockRenderer.BlockType blockType = blocks[nextHorizontalLimit / 32
						+ directionVector.x][(int) ((b * currentX + c - (b * currentX + c) % 32) / 32)];
				playerX += directionVector.x * distToVertLim;
				// If we can place a portal on the surface, we do so, returning the surface.
				if (blockType == BlockRenderer.BlockType.LIGHT) {
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
