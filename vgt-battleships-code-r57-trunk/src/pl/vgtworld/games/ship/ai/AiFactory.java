package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Fabryka obiektow AI.
 * 
 * @author VGT
 * @version 1.1
 */
abstract public class AiFactory
	{
	/**
	 * Metoda zwracajaca rozne obiekty AI na podstawie przekazanego w parametrze oczekiwanego poziomu trudnosci.
	 * 
	 * @param iPoziomTrudnosci Liczba z zakresu 1-100 informujaca o oczekiwanym poziomie trudnosci gracza komputerowego.
	 * @param bStraightLines Okresla, czy statki moga byc tylko pionowymi/poziomymi liniami.
	 * Informacja jest zapisywana w tworzonym obiekcie Ai, gdyz jest niezbedna przy pozniejszym wyszukiwaniu pol do oshotu.
	 * @param oShips Kontener statkow nalezacy do generowanego gracza komputerowego.
	 * @return Zwraca obiekt Ai zawierajacy sztuczna inteligencje gracza komputerowego.
	 */
	public static Ai getAi(int iPoziomTrudnosci, boolean bStraightLines, ShipIterator oShips)
		{
		Ai oAi;
		if (iPoziomTrudnosci > 66)
			oAi = new AiCheater(oShips);
		else if (iPoziomTrudnosci > 33)
			oAi = new AiExtended(oShips);
		else
			oAi = new AiBasic(oShips);
		AiGeneric oAi2 = (AiGeneric)oAi;
		oAi2.setProsteLinie(bStraightLines);
		return oAi;
		}
	}
