package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Najprostrza mozliwa implementacja Ai.<br />
 * 
 * Strzela zawsze w losowo wybrane pole na planszy niezaleznie od tego, czy sa jakies trafione, niezatopione statki.
 * 
 * @author VGT
 * @version 1.1
 */
public class AiBasic
	extends AiGeneric
	implements Ai
	{
	/**
	 * Konstruktor.
	 * 
	 * @param oShips Kontener statkow nalezacych do gracza sterowanego przez dany obiekt AI.
	 */
	public AiBasic(ShipIterator oShips)
		{
		super(oShips);
		}
	/**
	 * Implementacja metody interface'u Ai.
	 */
	public boolean shot(ShipIterator oOpponentShip)
		{
		return shotRandom(oOpponentShip);
		}
	}
