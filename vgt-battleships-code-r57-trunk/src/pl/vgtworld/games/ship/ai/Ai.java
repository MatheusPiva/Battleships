package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Interface containing methods required for AI classes.
 * 
 * @author VGT
 * @version 1.0
 */
public interface Ai
	{
	/**
	 * The main method of handling shots on the opponent's board.
	 * 
	 * @param oOpponentShip Enemy ships container.
	 * @return Returns TRUE if the ship is hit, FALSE if the shot was missed.
	 */
	public boolean shot(ShipIterator oOpponentShip);
	}
