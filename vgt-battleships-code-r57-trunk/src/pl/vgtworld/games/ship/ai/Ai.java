package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Interface zawierajacy metody wymagane dla klas AI.
 * 
 * @author VGT
 * @version 1.0
 */
public interface Ai
	{
	/**
	 * Glowna metoda obslugujaca shot na plansze przeciwnika.
	 * 
	 * @param oOpponentShip Kontener statkow przeciwnika.
	 * @return Zwraca TRUE w przypadku trafenia statku, lub FALSE, jesli shot byl niecelny.
	 */
	public boolean shot(ShipIterator oOpponentShip);
	}
