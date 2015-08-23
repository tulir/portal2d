package net.maunium.Portal2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.Map.Map;

/**
 * Class for the Menu game state.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Menu extends BasicGameState {
	private Portal2D host;
	
	public Menu(Portal2D host) {
		this.host = host;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		for (int i = 1; i < game.getStateCount(); i++) {
			g.setColor(Color.darkGray);
			g.fillRect(gc.getWidth() / 2 - 100, i * 25, 200, 20);
			g.setColor(Color.pink);
			g.drawString(((Map) game.getState(i + 99)).name, gc.getWidth() / 2 - 95, i * 25 + 1);
		}
		
		g.setColor(Color.white);
		g.drawString("Points: " + host.points, 5, 5);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		int mX = gc.getInput().getMouseX(), mY = gc.getInput().getMouseY();
		
		if (gc.getInput().isMousePressed(0) && mX > gc.getWidth() / 2 - 100 && mX < gc.getWidth() / 2 + 100) {
			int i = mY / 25;
			game.enterState(99 + i);
		}
	}
	
	@Override
	public int getID() {
		return 0;
	}
}
