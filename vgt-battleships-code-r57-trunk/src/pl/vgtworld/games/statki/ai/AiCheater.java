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
	 * @param oStatki Kontener statkow nalezacych do gracza sterowanego przez dany obiekt Ai.
	 */
	public AiCheater(ShipIterator oStatki)
		{
		super(oStatki);
		}
	/**
	 * Implementacja metody interface'u Ai.
	 */
	public boolean shot(ShipIterator oStatkiPrzeciwnika)
		{
		if (oUzyteczneTrafienia.size() > 0)
			{
			return shotSasiadujacy(oStatkiPrzeciwnika);
			}
		else
			{
			//ustalenie, czy komputer przegrywa
                        int iAiTotal = oStatki.getNumberOfShipsHit() + oStatki.getNumberOfSunkenShips();
                        int iEnemyTotal = oStatkiPrzeciwnika.getNumberOfShipsHit() - oStatkiPrzeciwnika.getNumberOfSunkenShips();
			int iDifference = iAiTotal - iEnemyTotal;
			if (iDifference > 0)
				{
				//komputer przegrywa
				int iIloscDozwolonychProb;
                                iIloscDozwolonychProb = (1 + (oStatkiPrzeciwnika.getNumberOfSunkenShips() - oStatki.getNumberOfSunkenShips()));
				return shotWielokrotny(oStatkiPrzeciwnika, iIloscDozwolonychProb);
				}
			else
				{
				//komputer wygrywa
				return shotLosowy(oStatkiPrzeciwnika);
				}
			}
		}
	}
