package net.maunium.Portal2D;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Class for the Menu game state.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Menu extends BasicGameState {
	private Image start;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		start = ((Portal2D) game).getImage("start");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.drawImage(start, gc.getWidth() / 2 - start.getWidth() / 2, gc.getHeight() / 2 - start.getHeight() / 2);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		int mX = gc.getInput().getMouseX(), mY = gc.getInput().getMouseY();
		
		if (mX > gc.getWidth() / 2 - start.getWidth() / 2 && mX < gc.getWidth() / 2 + start.getWidth() / 2 && mY > gc.getHeight() / 2 - start.getHeight() / 2
				&& mY < gc.getHeight() / 2 + start.getHeight() / 2) {
			game.enterState(1);
		}
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
		((AppGameContainer) gc).setDisplayMode(640, 480, false);
	}
	
	@Override
	public int getID() {
		return 0;
	}
}
