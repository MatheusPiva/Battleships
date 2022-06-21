package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Cheating AI implementation. <br />
 * 
 * As long as the computer wins, or the game is aligned, all AI does is shoot earlier
 * No hit squares, or random shooting if there are no hit squares. <br />
 * 
 * When AI loses, it starts to cheat and, after drawing the place to shoot, it checks to see if the opponent is in the given square
 * there is a ship. If the field is empty, it does not take the shot, but draws another field again. <br />
 * 
 * The number of attempts depends on how much AI loses to the player.
 * 
 * @author VGT
 * @version 1.0
 */
public class AiCheater
	extends AiGeneric
	implements Ai
	{
	/**
	 * Constructor.
	 * 
	 * @param oShips A container of ships belonging to the player controlled by the given object Ai.
	 */
	public AiCheater(ShipIterator oShips)
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
			{
			//determining if the computer is losing
                        int iAiTotal = oShips.getNumberOfShipsHit() + oShips.getNumberOfUndamagedShips();
                        int iEnemyTotal = oOpponentShip.getNumberOfShipsHit() - oOpponentShip.getNumberOfUndamagedShips();
			int iDifference = iAiTotal - iEnemyTotal;
			if (iDifference > 0)
				{
				//the computer is losing
				int iQuantityAllowedTrials;
                                iQuantityAllowedTrials = (1 + (oOpponentShip.getNumberOfUndamagedShips() - oShips.getNumberOfUndamagedShips()));
				return multipleShot(oOpponentShip, iQuantityAllowedTrials);
				}
			else
				{
				//the computer wins
				return shotRandom(oOpponentShip);
				}
			}
		}
	}
