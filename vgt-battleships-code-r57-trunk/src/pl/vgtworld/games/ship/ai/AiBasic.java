package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * The simplest possible implementation of Ai. <br />
 * 
 * It always shoots a randomly selected space on the board, regardless of whether there are any hit or sunken ships.
 * 
 * @author VGT
 * @version 1.1
 */
public class AiBasic
	extends AiGeneric
	implements Ai
	{
	/**
	 * Constructor.
	 * 
	 * @param oShips A container of ships belonging to the player controlled by the given AI object.
	 */
	public AiBasic(ShipIterator oShips)
		{
		super(oShips);
		}
	/**
	 * Implementation of the Ai interface method.
	 */
	public boolean shot(ShipIterator oOpponentShip)
		{
		return shotRandom(oOpponentShip);
		}
	}
