package net.maunium.Portal2D;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Portal2D extends BasicGame {
	private BlockRenderer br;
	
	public Portal2D() {
		super("Portal 2(D)");
		br = new BlockRenderer();
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
	
	}
	
	@Override
	public void update(GameContainer gc, int i) throws SlickException {
	
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
	
	}
	
	public static void main(String[] args) {
		try {
			AppGameContainer agc;
			agc = new AppGameContainer(new Portal2D());
			agc.setDisplayMode(640, 480, false);
			agc.start();
		} catch (SlickException e) {
			System.err.println("Fatal error:");
			e.printStackTrace();
		}
		
	}
}
