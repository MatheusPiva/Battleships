package pl.vgtworld.games.statki.ai;

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
	public boolean shot(ShipIterator oShipsPrzeciwnika)
		{
		if (oUzyteczneHits.size() > 0)
			{
			return shotSasiadujacy(oShipsPrzeciwnika);
			}
		else
			{
			//ustalenie, czy komputer przegrywa
                        int iAiTotal = oShips.getNumberOfShipsHit() + oShips.getNumberOfSunkenShips();
                        int iEnemyTotal = oShipsPrzeciwnika.getNumberOfShipsHit() - oShipsPrzeciwnika.getNumberOfSunkenShips();
			int iDifference = iAiTotal - iEnemyTotal;
			if (iDifference > 0)
				{
				//komputer przegrywa
				int iQuantityDozwolonychProb;
                                iQuantityDozwolonychProb = (1 + (oShipsPrzeciwnika.getNumberOfSunkenShips() - oShips.getNumberOfSunkenShips()));
				return shotWielokrotny(oShipsPrzeciwnika, iQuantityDozwolonychProb);
				}
			else
				{
				//komputer wygrywa
				return shotLosowy(oShipsPrzeciwnika);
				}
			}
		}
	}
