package pl.vgtworld.games.ship;

import java.util.ArrayList;
import java.util.Random;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa zajmuje sie losowym rozmieszczeniem statkow na planszy.<br />
 *
 * <p>
 * aktualizacje:<br />
 * 1.1<br />
 * - dodanie parametru bStraightLines do metody {@link #shipSpaces(Ship Iterator, boolean)}.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public class ShipPositioner
	{
	/**
	 * Kontener, ktorego statki maja byc rozmieszczone na planszy.
	 */
	private ShipIterator oShips;
	/**
	 * Board nalezaca do danego kontenera statkow.
	 */
	private Board oBoard;
	/**
	 * Generator liczb losowych.
	 */
	private Random oRand;
	/**
	 * Konstruktor domyslny.
	 */
	public ShipPositioner()
		{
		oShips = null;
		oBoard = null;
		oRand = new Random();
		}
	/**
	 * @deprecated
	 */
	public boolean shipSpaces(ShipIterator oShips) throws ParameterException
		{
		return shipSpaces(oShips, false);
		}
	/**
	 * Glowna metoda rozpoczynajaca procedure rozmieszczania statkow na planszy.<br />
	 * 
	 * Udane rozmieszczenie statkow nie jest gwarantowane. Im mniej miejsca na planszy i/lub wiecej statkow, tym wieksze szanse
	 * na blad podczas rozmieszczenie statkow. Jesli rozmieszczenie statkow jest niemozliwe do zrealizowania, metoda zwraca wartosc FALSE
	 * i wszelkie czesciowe rozmieszczenie statkow jest zerowane.<br />
	 * 
	 * Dla standardowej gry (plansza 10x10 i 10 statkow o Sizeach od 1 do 4)
	 * aktualna skutecznosc rozmieszczenia statkow jest na poziomie 99,93%.<br />
	 * 
	 * aktualizacje:<br />
	 * 
	 * 1.1 - dodanie drugiego parametru.
	 * 
	 * @param oShips Kontener z utworzonymi statkami, ktore maja byc rozmieszczone na planszy.
	 * @param bStraightLines Okresla, czy statki maja byc tylko pionowymi / poziomymi liniami.
	 * @return Zwraca TRUE, jesli statki zostaly prawidlowo rozmieszczone, lub FALSE jesli wystapil blad.
	 * @throws ParameterException Wyrzuca wyjatek, jesli kontener nie zawiera zadnych statkow.
	 */
	public boolean shipSpaces(ShipIterator oShips, boolean bStraightLines) throws ParameterException
		{
		if (oShips.getNumberOfShips() == 0)
			throw new ParameterException("oShips.iNumberOfShips = 0");
		try
			{
			importShips(oShips);
			this.oShips.resetFields();
			//ustalenie Sizeu najwieszego statku
			int iMaxSize = 0;
			for (int i = 1; i <= this.oShips.getNumberOfShips(); ++i)
				if (oShips.getShip(i).getSize() > iMaxSize)
					iMaxSize = this.oShips.getShip(i).getSize();
			//rozmieszczenie statkow rozpoczynajac od najwiekszych
			while (iMaxSize > 0)
				{
				for (int i = 1; i <= this.oShips.getNumberOfShips(); ++i)
					if (oShips.getShip(i).getSize() == iMaxSize)
						{
						int iApproach = 1; // ktora proba umieszczenia statku na planszy
						boolean bPlaced = false;
						while (bPlaced == false)
							{
							try
								{
								placeShipsOnTheBoard(oShips.getShip(i), bStraightLines);
								bPlaced = true;
								}
							catch (ShipPositionerException e)
								{
								if (iApproach >= 3)
									throw new ShipPositionerException();
								++iApproach;
								oShips.getShip(i).resetFields();
								}
							}
						}
				--iMaxSize;
				}
			}
		catch (ShipPositionerException e)
			{
			oShips.resetFields();
			return false;
			}
		return true;
		}
	/**
	 * Zaladowanie obiektow statkow i planszy do lokalnych wlasciwosci obiektu.
	 * 
	 * @param oShips Kontener statkow wymagajacy rozmieszczenia statkow na planszy.
	 */
	private void importShips(ShipIterator oShips)
		{
		this.oShips = oShips;
		this.oBoard = oShips.getBoard();
		}
	/**
	 * Losowo umieszcza pola przekazanego w obiekcie statku na planszy.
	 * 
	 * @param oShip Ship, ktorego pola nalezy umiescic na planszy.
	 * @throws ShipPositionerException Wyrzuca wyjatek, jesli umieszczenie statku na planszy zakonczylo sie niepowodzeniem.
	 */
	private void placeShipsOnTheBoard(Ship oShip, boolean bStraightLines) throws ShipPositionerException
		{
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				if (i == 1)
					{
					//pierwsze pole
					Position oEmptyField = drawEmptyField();
					oShip.setField(i, oEmptyField.getX(), oEmptyField.getY());
					}
				else
					{
					//kolejne pola
					//tworzenie listy kandydatow na kolejne pola
					int iCandidatesQuantity = 0;
					ArrayList<Position> oCandidates = new ArrayList<Position>();
					for (int j = 1; j < i; ++j)
						{
						Position oField = oShip.getField(j);
						for (int k = -1; k <= 1; ++k)
							for (int l = -1; l <= 1; ++l)
								if (k + l == -1 || k + l == 1)
									if (oField.getX() + k >= 0 && oField.getX() + k < oBoard.getWidth()
										&& oField.getY() + l >= 0 && oField.getY() + l < oBoard.getHeight()
										&& oBoard.getField(oField.getX() + k, oField.getY() + l) == FieldTypeBoard.BOARD_FIELD_EMPTY
										)
										{
										Position oCandidate = new Position(2);
										oCandidate.setX(oField.getX() + k);
										oCandidate.setY(oField.getY() + l);
										if (verifyCandidate(oCandidate, oShip))
											{
											++iCandidatesQuantity;
											oCandidates.add(oCandidate);
											}
										}
										
						}
					//jesli statki maja byc liniami, odrzucenie pol niepasujacych
					if (bStraightLines == true && i > 2)
						{
						//sprawdzenie, czy statek jest pionowy, czy poziomy i odrzucenie pol niepasujacych
						if (oShip.getField(1).getX() == oShip.getField(2).getX())
							{
							//wspolrzedne X musza byc takie same
							for (int j = oCandidates.size() - 1; j >= 0; --j)
								if (oShip.getField(1).getX() != oCandidates.get(j).getX())
									{
									--iCandidatesQuantity;
									oCandidates.remove(j);
									}
							}
						else if (oShip.getField(1).getY() == oShip.getField(2).getY())
							{
							//wspolrzedne Y musza byc takie same
							for (int j = oCandidates.size() - 1; j >= 0; --j)
								if (oShip.getField(1).getY() != oCandidates.get(j).getY())
									{
									--iCandidatesQuantity;
									oCandidates.remove(j);
									}
							}
						else
							throw new DeveloperException("The direction of the ship could not be determined");
						}
					if (iCandidatesQuantity == 0)
						throw new ShipPositionerException();
					//wylosowanie kandydata
					int iLos = oRand.nextInt(iCandidatesQuantity);
					//ustawienie pola statku na wylosowanym kandydacie
					oShip.setField(i, oCandidates.get(iLos).getX(), oCandidates.get(iLos).getY());
					}
				}
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda zwraca wspolrzedne losowo wybranego pustego pola na planszy,
	 * ktore nadaje sie do rozpoczecia nowego statku (nie posiada zadnych sasiadow).
	 * 
	 * @return Zwraca losowe puste pole z planszy.
	 */
	private Position drawEmptyField() throws ShipPositionerException
		{
		try
			{
			Position oCoordinates = new Position(2);
			//policzenie pustych pol na planszy
			int iEmptyQuantity = 0;
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						Position oCandidate = new Position(2);
						oCandidate.setX(i);
						oCandidate.setY(j);
						if (verifyCandidate(oCandidate, null))
							++iEmptyQuantity;
						}
			//blad jesli pustych pol nie ma
			if (iEmptyQuantity == 0)
				throw new ShipPositionerException();
			int iRandomlyDrawnField = oRand.nextInt(iEmptyQuantity) + 1;
			//ponowne przejscie po planszy i zwrocenie pustego pola o wylosowanym numerze
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						Position oCandidate = new Position();
						oCandidate.setX(i);
						oCandidate.setY(j);
						if (verifyCandidate(oCandidate, null))
							--iRandomlyDrawnField;
						if (iRandomlyDrawnField == 0)
							{
							oCoordinates.setX(i);
							oCoordinates.setY(j);
							return oCoordinates;
							}
						}
			throw new DeveloperException("koniec planszy");
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda sprawdza, czy pole o podanych wspolrzednych nie posiada w sasiedztwie zadnych statkow.
	 * 
	 * @param oField Wspolrzedne pola do sprawdzenia.
	 * @param oShip Jesli jest porzekazany obiekt statku, jest to informacja,
	 * ze moga wystepowac pola tego statku i kandydat jest nadal prawidlowy.
	 * @return Zwraca TRUE, jesli pole nie ma niechcianych sasiadow, lub false w przeciwnym wypadku.
	 */
	private boolean verifyCandidate(Position oField, Ship oShip)
		{
		try
			{
			for (int i = -1; i <= 1; ++i)
				for (int j = -1; j <= 1; ++j)
					if (oField.getX() + i >= 0 && oField.getX() + i < oBoard.getWidth()
						&& oField.getY() + j >= 0 && oField.getY() + j < oBoard.getHeight()
						&& oBoard.getField(oField.getX() + i, oField.getY() + j) == FieldTypeBoard.SHIP_BOARD
						)
						{
						if (oShip == null)
							return false;
						else if(oShip.getNumerPola(oField.getX() + i, oField.getY() + j) == 0)
							return false;
						}
			return true;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}

/**
 * Wyjatek wyrzucany w przypadku wystapienia bledow podczas umieszczania statku na planszy.<br />
 * 
 * Jego wystapienie informuje glowna metode pozycjonujaca o tym, ze rozmieszczenie statkow zakonczylo sie
 * bledem, ktorego nie mozna rozwiazac.
 * 
 * @author VGT
 * @version 1.0
 */
class ShipPositionerException extends Exception {}
