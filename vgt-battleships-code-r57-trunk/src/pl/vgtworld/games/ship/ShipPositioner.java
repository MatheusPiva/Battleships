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
 * - dodanie parametru bStraightLines do metody {@link #rozmiescStatki(StatekIterator, boolean)}.<br />
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
	private ShipIterator oStatki;
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
		oStatki = null;
		oBoard = null;
		oRand = new Random();
		}
	/**
	 * @deprecated
	 */
	public boolean rozmiescStatki(ShipIterator oStatki) throws ParameterException
		{
		return rozmiescStatki(oStatki, false);
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
	 * @param oStatki Kontener z utworzonymi statkami, ktore maja byc rozmieszczone na planszy.
	 * @param bStraightLines Okresla, czy statki maja byc tylko pionowymi / poziomymi liniami.
	 * @return Zwraca TRUE, jesli statki zostaly prawidlowo rozmieszczone, lub FALSE jesli wystapil blad.
	 * @throws ParameterException Wyrzuca wyjatek, jesli kontener nie zawiera zadnych statkow.
	 */
	public boolean rozmiescStatki(ShipIterator oStatki, boolean bStraightLines) throws ParameterException
		{
		if (oStatki.getNumberOfShips() == 0)
			throw new ParameterException("oStatki.iNumberOfShips = 0");
		try
			{
			importujStatki(oStatki);
			this.oStatki.resetFields();
			//ustalenie Sizeu najwieszego statku
			int iMaxSize = 0;
			for (int i = 1; i <= this.oStatki.getNumberOfShips(); ++i)
				if (oStatki.getShip(i).getSize() > iMaxSize)
					iMaxSize = this.oStatki.getShip(i).getSize();
			//rozmieszczenie statkow rozpoczynajac od najwiekszych
			while (iMaxSize > 0)
				{
				for (int i = 1; i <= this.oStatki.getNumberOfShips(); ++i)
					if (oStatki.getShip(i).getSize() == iMaxSize)
						{
						int iPodejscie = 1; // ktora proba umieszczenia statku na planszy
						boolean bUmieszczony = false;
						while (bUmieszczony == false)
							{
							try
								{
								umiescStatekNaPlanszy(oStatki.getShip(i), bStraightLines);
								bUmieszczony = true;
								}
							catch (StatkiPozycjonerException e)
								{
								if (iPodejscie >= 3)
									throw new StatkiPozycjonerException();
								++iPodejscie;
								oStatki.getShip(i).resetFields();
								}
							}
						}
				--iMaxSize;
				}
			}
		catch (StatkiPozycjonerException e)
			{
			oStatki.resetFields();
			return false;
			}
		return true;
		}
	/**
	 * Zaladowanie obiektow statkow i planszy do lokalnych wlasciwosci obiektu.
	 * 
	 * @param oStatki Kontener statkow wymagajacy rozmieszczenia statkow na planszy.
	 */
	private void importujStatki(ShipIterator oStatki)
		{
		this.oStatki = oStatki;
		this.oBoard = oStatki.getBoard();
		}
	/**
	 * Losowo umieszcza pola przekazanego w obiekcie statku na planszy.
	 * 
	 * @param oShip Ship, ktorego pola nalezy umiescic na planszy.
	 * @throws StatkiPozycjonerException Wyrzuca wyjatek, jesli umieszczenie statku na planszy zakonczylo sie niepowodzeniem.
	 */
	private void umiescStatekNaPlanszy(Ship oShip, boolean bStraightLines) throws StatkiPozycjonerException
		{
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				if (i == 1)
					{
					//pierwsze pole
					Position oPustePole = wylosujPustePole();
					oShip.setField(i, oPustePole.getX(), oPustePole.getY());
					}
				else
					{
					//kolejne pola
					//tworzenie listy kandydatow na kolejne pola
					int iIloscKandydatow = 0;
					ArrayList<Position> oKandydaci = new ArrayList<Position>();
					for (int j = 1; j < i; ++j)
						{
						Position oPole = oShip.getField(j);
						for (int k = -1; k <= 1; ++k)
							for (int l = -1; l <= 1; ++l)
								if (k + l == -1 || k + l == 1)
									if (oPole.getX() + k >= 0 && oPole.getX() + k < oBoard.getWidth()
										&& oPole.getY() + l >= 0 && oPole.getY() + l < oBoard.getHeight()
										&& oBoard.getField(oPole.getX() + k, oPole.getY() + l) == FieldTypeBoard.BOARD_FIELD_EMPTY
										)
										{
										Position oKandydat = new Position(2);
										oKandydat.setX(oPole.getX() + k);
										oKandydat.setY(oPole.getY() + l);
										if (weryfikujKandydata(oKandydat, oShip))
											{
											++iIloscKandydatow;
											oKandydaci.add(oKandydat);
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
							for (int j = oKandydaci.size() - 1; j >= 0; --j)
								if (oShip.getField(1).getX() != oKandydaci.get(j).getX())
									{
									--iIloscKandydatow;
									oKandydaci.remove(j);
									}
							}
						else if (oShip.getField(1).getY() == oShip.getField(2).getY())
							{
							//wspolrzedne Y musza byc takie same
							for (int j = oKandydaci.size() - 1; j >= 0; --j)
								if (oShip.getField(1).getY() != oKandydaci.get(j).getY())
									{
									--iIloscKandydatow;
									oKandydaci.remove(j);
									}
							}
						else
							throw new DeveloperException("nie mozna ustalic kierunku statku");
						}
					if (iIloscKandydatow == 0)
						throw new StatkiPozycjonerException();
					//wylosowanie kandydata
					int iLos = oRand.nextInt(iIloscKandydatow);
					//ustawienie pola statku na wylosowanym kandydacie
					oShip.setField(i, oKandydaci.get(iLos).getX(), oKandydaci.get(iLos).getY());
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
	private Position wylosujPustePole() throws StatkiPozycjonerException
		{
		try
			{
			Position oWspolrzedne = new Position(2);
			//policzenie pustych pol na planszy
			int iIloscPustych = 0;
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						Position oKandydat = new Position(2);
						oKandydat.setX(i);
						oKandydat.setY(j);
						if (weryfikujKandydata(oKandydat, null))
							++iIloscPustych;
						}
			//blad jesli pustych pol nie ma
			if (iIloscPustych == 0)
				throw new StatkiPozycjonerException();
			int iWylosowanePole = oRand.nextInt(iIloscPustych) + 1;
			//ponowne przejscie po planszy i zwrocenie pustego pola o wylosowanym numerze
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						Position oKandydat = new Position();
						oKandydat.setX(i);
						oKandydat.setY(j);
						if (weryfikujKandydata(oKandydat, null))
							--iWylosowanePole;
						if (iWylosowanePole == 0)
							{
							oWspolrzedne.setX(i);
							oWspolrzedne.setY(j);
							return oWspolrzedne;
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
	 * @param oPole Wspolrzedne pola do sprawdzenia.
	 * @param oShip Jesli jest porzekazany obiekt statku, jest to informacja,
	 * ze moga wystepowac pola tego statku i kandydat jest nadal prawidlowy.
	 * @return Zwraca TRUE, jesli pole nie ma niechcianych sasiadow, lub false w przeciwnym wypadku.
	 */
	private boolean weryfikujKandydata(Position oPole, Ship oShip)
		{
		try
			{
			for (int i = -1; i <= 1; ++i)
				for (int j = -1; j <= 1; ++j)
					if (oPole.getX() + i >= 0 && oPole.getX() + i < oBoard.getWidth()
						&& oPole.getY() + j >= 0 && oPole.getY() + j < oBoard.getHeight()
						&& oBoard.getField(oPole.getX() + i, oPole.getY() + j) == FieldTypeBoard.SHIP_BOARD
						)
						{
						if (oShip == null)
							return false;
						else if(oShip.getNumerPola(oPole.getX() + i, oPole.getY() + j) == 0)
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
class StatkiPozycjonerException extends Exception {}
