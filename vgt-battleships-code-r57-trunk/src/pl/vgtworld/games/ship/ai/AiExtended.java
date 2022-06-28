package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Simple Ai implementation. <br />
 * 
 * Shoots a randomly selected field until it hits the ship.
 * After hitting a ship, if it has more than half,
 * are fired at adjacent positions until the ship is sunk.
 *  
 * @author VGT
 * @version 1.0
 */
public class AiExtended
	extends AiGeneric
	implements Ai
	{
	/**
	 * Constructor.
	 * 
	 * @param oShips A container of ships belonging to the player controlled by the given object Ai.
	 */
	public AiExtended(ShipIterator oShips)
		{
		super(oShips);
		}
	/**
	 * Implementation of the Ai interface method.
	 */
	public boolean shot(ShipIterator oOpponentShip)
		{
		if (oUsefulHits.size() > 0)
			{
			return shotNeighbor(oOpponentShip);
			}
		else
			return shotRandom(oOpponentShip);
		}
	}
