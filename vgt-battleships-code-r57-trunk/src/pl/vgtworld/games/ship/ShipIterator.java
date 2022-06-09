package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa kontener przechowujaca statki gracza.<br />
 * 
 * <p>
 * aktualizacje:<br />
 * 1.2<br />
 * - dodanie parametru bStraightLines do metody {@link #verifyApplication(boolean)}.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.2
 */
public class ShipIterator
	{
	/**
	 * Referencja do obiektu board, na ktorej umieszczone sa przechowywane statki.
	 */
	private Board oBoard;
	/**
	 * Tablica przechowujaca statki.
	 */
	private Ship[] aShips;
	/**
	 * Ilosc statkow przechowywana aktualnie w obiekcie.
	 */
	private int iNumberOfShips;
	/**
	 * Obiekt przechowujacy co-ordinates ostatniego obslugiwanego shotu.
	 */
	private Position oLastShot;
	/**
	 * Konstruktor domyslny.
	 * 
	 * @param oBoard Referencja do obiektu board, na ktorej umieszczone zostana statki.
	 */
	public ShipIterator(Board oBoard)
		{
		this.oBoard = oBoard;
		aShips = new Ship[0];
		iNumberOfShips = 0;
		oLastShot = new Position(2);
		oLastShot.setX(-1);
		oLastShot.setY(-1);
		}
	/**
	 * Wyswietlenie listy statkow przechowywanych przez obiekt na standardowym wyjsciu.
	 */
	@Override public String toString()
		{
		String sReturn = "Ship Iterator\n";
		sReturn+= "Number of Ships: " + iNumberOfShips + "\n";
		sReturn+= "=================\n";
		for (int i = 0; i < iNumberOfShips; ++i)
			sReturn+= aShips[i] + "\n";
		return sReturn;
		}
	/**
	 * Metoda zwraca referencje do obiektu statku o podanym numerze.
	 * 
	 * @param iNumber Numer statku, ktory ma byc zwrocony (counted from 1).
	 * @return Zwraca referencje do statku o podanym numerze.
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer statku jest mniejszy, lub rowny 0,
	 * lub wiekszy from ilosci statkow przechowywanych w obiekcie.
	 */
	public Ship getShip(int iNumber) throws ParameterException
		{
		if (iNumber > iNumberOfShips || iNumber <= 0)
			throw new ParameterException("iNumber = " + iNumber);
		return aShips[iNumber - 1];
		}
	/**
	 * Zwraca co-ordinates na ktorych jest umieszczone pole o numerze przekazanym w drugim parametrze
	 * nalezace do statku o numerze przekazanym w pierwszym parametrze.
	 * 
	 * @param iShipNumber Numer statku, ktorego pole ma byc zwrocone (counted from 1).
	 * @param iFieldNumber Numer position, ktore ma byc zwrocone (counted from 1).
	 * @return Zwraca obiekt zawierajacy co-ordinates pobieranego position.
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer statku, lub nr position sa poza dostepnym zakresem.
	 */
	public Position getField(int iShipNumber, int iFieldNumber) throws ParameterException
		{
		if (iShipNumber > iNumberOfShips || iShipNumber <= 0)
			throw new ParameterException("iShipNumber = " + iShipNumber);
		return aShips[ iShipNumber - 1 ].getField(iFieldNumber);
		}
	/**
	 * Zwraca referencje do obiektu board, na ktorej sa umieszczone statki przechowywane przez obiekt.
	 * 
	 * @return Referencja do board.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Zwraca co-ordinates ostatniego shotu.
	 * 
	 * @return co-ordinates ostatniego shotu.
	 */
	public Position getLastShot()
		{
		return oLastShot;
		}
	/**
	 * Zwraca ilosc statkow przechowywanych aktualnie w obiekcie
	 * (nie ma znaczenia, czy statki zostaly umieszczone na board).
	 * 
	 * @return Ilosc statkow.
	 */
	public int getNumberOfShips()
		{
		return iNumberOfShips;
		}
	/**
	 * Zwraca ilosc statkow przechowywanych aktualnie w obiekcie o Sizeze podanym w parametrze
	 * (nie ma znaczenia, czy statki zostaly umieszczone na board).
	 * 
	 * @param iSize Size statkow, ktore maja byc policzone.
	 * @return Ilosc statkow o podanym Sizeze.
	 */
	public int getNumberOfShips(int iSize)
		{
		int iQuantity = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].getSize() == iSize)
				++iQuantity;
		return iQuantity;
		}
	/**
	 * Metoda zwraca ilosc trafionych, ale nie zatopionych statkow.
	 * 
	 * @return Ilosc trafionych, ale nie zatopionych statkow.
	 */
	public int getNumberOfShipsHit()
		{
		int iHitNotSunk = 0;
		for (Ship oShip: aShips)
			{
			if (oShip.getHits() == true && oShip.getSunk() == false)
				++iHitNotSunk;
			}
		return iHitNotSunk;
		}
	/**
	 * Metoda zwraca ilosc zatopionych statkow.
	 * 
	 * @return Ilosc zatopionych statkow.
	 */
	public int getNumberOfSunkenShips()
		{
		int iSunken = 0;
		for (Ship oShip: aShips)
			{
			if (oShip.getSunk() == true)
				++iSunken;
			}
		return iSunken;
		}
	/**
	 * Zwraca informacje o ilosci statkow, ktore jeszcze nie zostaly trafione.
	 * 
	 * @return Ilosc nietrafionych statkow.
	 * @since 1.1 
	 */
	public int getNumberOfUndamagedShips()
		{
		return getNumberOfShips() - getNumberOfShipsHit() - getNumberOfSunkenShips();
		}
	/**
	 * Zwraca Size, jaki ma najwiekszy statek aktualnie przechowywany w kontenerze.
	 */
	public int getMaxShipSize()
		{
		int iMax = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].getSize() > iMax)
				iMax = aShips[i].getSize();
		return iMax;
		}
	/**
	 * Metoda oblicza laczna ilosc pol zajmowanych na board przez poszczegolne statki.<br />
	 * 
	 * Nie ma znaczenia, czy position zostaly umieszczone na board - metoda oblicza wymagana a nie rzeczywista ilosc pol na board
	 * zajmowana przez statki.
	 * 
	 * @return Laczny Size wszystkich statkow.
	 */
	public int getTotalShipSize()
		{
		try
			{
			int iSize = 0;
			for (int i = 1; i <= getNumberOfShips(); ++i)
				iSize+= getShip(i).getSize();
			return iSize;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda oblicza laczna ilosc trafionych pol wszystkich statkow przechowywanych przez kontener.
	 * 
	 * @return Laczna ilosc trafionych pol we wszystkich statkach.
	 */
	public int getTotalHits()
		{
		int iHits = 0;
		try
			{
			for (int i = 1; i <= iNumberOfShips; ++i)
				iHits+= getShip(i).getNumberOfHits();
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		return iHits;
		}
	/**
	 * Metoda pozwala ustawic co-ordinates na board dla wskazanego position statku.
	 * 
	 * @param iShipNumber Numer statku, dla ktorego sa ustawiane co-ordinates position (counted from 1).
	 * @param iFieldNumber Numer position danego statku, dla ktorego sa ustawiane co-ordinates (counted from 1).
	 * @param iX Coordinate X pozycji na board, na ktora ma byc ustawione pole (counted from 0).
	 * @param iY Coordinate Y pozycji na board, na ktora ma byc ustawione pole (counted from 0).
	 * @throws ParameterException Wyrzuca wyjatek w przypadku przekroczenia zakresu numeracji statkow, pol danego statku, lub podania
	 * wspolrzednych poza zakresem board.
	 */
	public void setField(int iShipNumber, int iFieldNumber, int iX, int iY) throws ParameterException
		{
		if (iShipNumber > iNumberOfShips || iShipNumber <= 0)
			throw new ParameterException("iShipNumber = " + iShipNumber);
		if (iX >= oBoard.getWidth() || iX < -1)
			throw new ParameterException("iX = " + iX);
		if (iY >= oBoard.getHeight() || iY < -1)
			throw new ParameterException("iY = " + iY);
		aShips[ iShipNumber - 1 ].setField(iFieldNumber, iX, iY);
		}
	/**
	 * Metoda ustawia wszystkie position dla wszystkich statkow na pozycje startowa (-1, -1).
	 */
	public void resetFields()
		{
		for (int i = 0; i < iNumberOfShips; ++i)
			aShips[i].resetFields();
		}
	/**
	 * Glowna metoda przekazujaca informacje o shote do wszystkich obiektow ktore tego wymagaja.<br />
	 * 
	 * Informacja o shote jest przekazywana kolejno do wszystkich statkow znajdujacych sie w kontenerze, dopoki ktorys
	 * nie przekaze informacji o udanym trafieniu. Obiekty statkow zajmuja sie obsluga tej informacji na swoje potrzeby,
	 * a takze dokonuja odpowiednich oznaczen na board.
	 * 
	 * @param iX Coordinate X position na board, na ktore jest oddany shot.
	 * @param iY Coordinate Y position na board, na ktore jest oddany shot.
	 * @return Zwraca TRUE, jesli ktorys statek zostal trafiony, lub FALSE, jesli shot byl niecelny.
	 * @throws ParameterException Wyrzuca wyjatek, jesli podane co-ordinates znajduja sie poza wymiarami board.
	 */
	public boolean shot(int iX, int iY) throws ParameterException
		{
		if (iX >= oBoard.getWidth() || iX < 0)
			throw new ParameterException("Ix = " + iX);
		if (iY >= oBoard.getHeight() || iY < 0)
			throw new ParameterException("iY = " + iY);
		if (oBoard.getField(iX, iY) != FieldTypeBoard.BOARD_FIELD_EMPTY && oBoard.getField(iX, iY) != FieldTypeBoard.SHIP_BOARD)
			throw new DeveloperException();
		oLastShot.setX(iX);
		oLastShot.setY(iY);
		for (int i = 0; i < iNumberOfShips; ++i)
			if (aShips[i].shot(iX, iY) == true)
				return true;
		return false;
		}
	/**
	 * Dodaje do kontenera statek o podanej Sizei.<br />
	 * 
	 * Po utworzeniu statku wszystkie jego position sa ustawione na domyslna pozycje (-1, -1).
	 * 
	 * @param iSize Size tworzonego statku.
	 */
	public void addAShip(int iSize)
		{
		//utworzenie nowej tablicy statkow i przepisanie referek dotychczasowych do nowej tablicy
		Ship[] aNewShips = new Ship[ iNumberOfShips + 1 ];
		for (int i = 0; i < iNumberOfShips; ++i)
			aNewShips[i] = aShips[i];
		//utworzenie nowego statku
		Ship oObj = new Ship(iSize, oBoard);
		aNewShips[iNumberOfShips] = oObj;
		++iNumberOfShips;
		aShips = null;
		aShips = aNewShips;
		}
	/**
	 * Usuwa statek o podanym numerze.
	 * 
	 * @param iNumber Numer statku do usuniecia (counted from 1).
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer statku jest poza zakresem dostepnych statkow.
	 */
	public void removeShip(int iNumber) throws ParameterException
		{
		if (iNumber > iNumberOfShips || iNumber <= 0)
			throw new ParameterException("iNumber = " + iNumber);
		//utworzenie nowej tablicy statkow i przepisanie referek z pominieciem usuwanego
		Ship[] aNewShips = new Ship[ iNumberOfShips - 1 ];
		int iLocalIndex = 0;
		for (int i = 0; i < iNumberOfShips; ++i)
			{
			if (i + 1 == iNumber)
				aShips[i] = null;
			else
				aNewShips[iLocalIndex++] = aShips[i];
			}
		--iNumberOfShips;
		aShips = null;
		aShips = aNewShips;
		}
	/**
	 * Metoda sprawdza, czy wszystkie statki zostaly umieszczone na board i czy ich rozmieszczenie jest zgodne z zasadami gry.<br />
	 * 
	 * Sprawdzane sa kolejno: czy wszystkie position znajduja sie na board, czy position sa polaczone w jeden element,
	 * oraz czy zadne z pol nie styka sie bokiem lub naroznikiem z innym statkiem.
	 *
	 * aktualizacje:<br />
	 * 
	 * 1.2 - dodanie pierwszego parametru
	 * 
	 * @param bStraightLines Okresla, czy statki moga byc tylko pionowymi/poziomymi liniami.
	 * @return Zwraca TRUE w przypadku prawidlowego rozmieszczenia statkow, lub FALSE, jesli zostal znaleziony blad.
	 */
	public boolean verifyApplication(boolean bStraightLines)
		{
		try
			{
			//petla wykonywana dla kazdego kolejnego statku
			for (int iShipNumber = 1; iShipNumber <= iNumberOfShips; ++iShipNumber)
				{
				Ship oShip = getShip(iShipNumber);
				ShipVerification oVerifier = new ShipVerification();
				oVerifier.importShip(oShip);
				//sprawdzenie, czy wszystkie position znajduja sie na board
				if (oVerifier.spacesOnBoard() == false)
					return false;
				//sprawdzenie, czy wszystkie position tworza jednolita strukture (stykaja sie ze soba)
				if (oVerifier.fieldsConnected(bStraightLines) == false)
					return false;
				//sprawdzenie, czy statek nie styka sie bokiem, lub naroznikiem z innym statkiem
				if (oVerifier.NoNeighbors() == false)
					return false;
				}
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		return true;
		}
	}
