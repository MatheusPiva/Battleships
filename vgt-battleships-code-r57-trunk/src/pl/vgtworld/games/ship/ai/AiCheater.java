package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Oszukujaca implmentacja interface'u AI.<br />
 * 
 * Dopoki komputer wygrywa, lub rozgrwka jest wyrownana, jedyne co AI realizuje, to ostrzeliwanie wczesniej
 * trafionych pol staku, lub strzelanie losowe, jesli zadnych pol trafionych nie ma.<br />
 * 
 * Gdy AI przegrywa, zaczyna oszukiwac i po wylosowaniu miejsca shotu sprawdza, czy na danym polu przeciwnika
 * jest statek. Jesli pole jest puste, nie oddaje shotu, tylko losuje ponownie inne pole.<br />
 * 
 * Ilosc prob uzalezniona jest od tego, jak bardzo AI przegrywa wzgledem gracza.
 * 
 * @author VGT
 * @version 1.0
 */
public class AiCheater
	extends AiGeneric
	implements Ai
	{
	/**
	 * Konstruktor.
	 * 
	 * @param oShips Kontener statkow nalezacych do gracza sterowanego przez dany obiekt Ai.
	 */
	public AiCheater(ShipIterator oShips)
		{
		super(oShips);
		}
	/**
	 * Implementacja metody interface'u Ai.
	 */
	public boolean shot(ShipIterator oOpponentShip)
		{
		if (oUsefulHits.size() > 0)
			{
			return shotNeighbor(oOpponentShip);
			}
		else
			{
			//ustalenie, czy komputer przegrywa
                        int iAiTotal = oShips.getNumberOfShipsHit() + oShips.getNumberOfUndamagedShips();
                        int iEnemyTotal = oOpponentShip.getNumberOfShipsHit() - oOpponentShip.getNumberOfUndamagedShips();
			int iDifference = iAiTotal - iEnemyTotal;
			if (iDifference > 0)
				{
				//komputer przegrywa
				int iQuantityAllowedTrials;
                                iQuantityAllowedTrials = (1 + (oOpponentShip.getNumberOfUndamagedShips() - oShips.getNumberOfUndamagedShips()));
				return multipleShot(oOpponentShip, iQuantityAllowedTrials);
				}
			else
				{
				//komputer wygrywa
				return shotRandom(oOpponentShip);
				}
			}
		}
	}
