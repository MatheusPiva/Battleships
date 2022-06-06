package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa reprezentujaca statek na planszy.
 * 
 * @author VGT
 * @version 1.0
 */
public class Ship
	{
	/**
	 * Referencja do obiektu planszy, na ktorej bedzie umieszczony statek.
	 */
	private Board oBoard;
	/**
	 * Size statku podany w ilosci zajmowanych przez niego pol na planszy.
	 */
	private int iSize;
	/**
	 * Tablica zawierajaca wspolrzedne poszczegolnych pol statku.
	 */
	private Position[] aCoordenates;
	/**
	 * Tablica przechowujaca informacje o tym, czy poszczegolne pola statku zostaly trafione.
	 */
	private boolean[] aHits;
	/**
	 * Ilosc trafionych pol statku.
	 */
	private int iHitsQuantity;
	/**
	 * Konstruktor domyslny - tworzy nowy statek o podanym Sizeze.
	 * 
	 * @param iSize Ilosc pol, ktore statek zajmuje na planszy.
	 * @param oBoard Referencja do obiektu planszy, na ktorej ma byc umieszczony statek.
	 */
	public Ship(int iSize, Board oBoard)
		{
		this.oBoard = oBoard;
		this.iSize = iSize;
		aCoordenates = new Position[ iSize ];
		//wypelnienie tablicy wspolrzednych Valuesami domyslnymi (-1, -1)
		for (int i = 0; i < iSize; ++i)
			{
			aCoordenates[i] = new Position(2);
			aCoordenates[i].setX(-1);
			aCoordenates[i].setY(-1);
			}
		aHits = new boolean[ iSize ];
		for (int i = 0; i < iSize; ++i)
			aHits[i] = false;
		iHitsQuantity = 0;
		}
	/**
	 * Wyswietlenie informacji o statku na standardowym wyjsciu.
	 */
	@Override public String toString()
		{
		String sReturn = "Ship\n";
		sReturn+= "Size: " + iSize + "\n";
		sReturn+= "Position:";
		for(Position oObj: aCoordenates)
			sReturn+= " " + oObj;
		sReturn+= "\nHits: [";
		for(boolean bHit: aHits)
			sReturn+= " " + bHit;
		sReturn+= " ]";
		return sReturn;
		}
	/**
	 * Zwraca obiekt zawierajacy wspolrzedne pola o podanym numerze.
	 * 
	 * @param iNumberFields Numer pola ktorego wspolrzedne maja byc zwrocone (liczone od 1).
	 * @return Wspolrzedne pola o podanym numerze.
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer pola jest mniejszy od 1, lub wiekszy od Sizeu statku. 
	 */
	public Position getField(int iNumberFields) throws ParameterException
		{
		if (iNumberFields > iSize || iNumberFields <= 0)
			throw new ParameterException("iNumberFields = " + iNumberFields);
		return aCoordenates[ iNumberFields - 1 ];
		}
	/**
	 * Jesli na podanych wspolrzednych znajduje sie pole statku, metoda zwraca jego numer,
	 * w przeciwnym wypadku zwraca 0.
	 * 
	 * @param iX Wspolrzedna X na planszy.
	 * @param iY Wspolrzedna Y outna planszy.
	 * @return Numer pola statku (liczone od 1).
	 * @throws ParameterException Wyrzuca wyjatek, jesli podane wspolrzedne znajduje sie poza zakresem planszy.
	 */
	public int getNumerPola(int iX, int iY) throws ParameterException
		{
		if (iX + 1 > oBoard.getWidth() || iX < 0)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > oBoard.getHeight() || iY < 0)
			throw new ParameterException("iY = " + iY);
		for (int i = 0; i < iSize; ++i)
			if (aCoordenates[i].getX() == iX && aCoordenates[i].getY() == iY)
				return (i + 1);
		return 0;
		}
	/**
	 * Metoda zwraca Size statku.
	 * 
	 * @return Size statku.
	 */
	public int getSize()
		{
		return iSize;
		}
	/**
	 * Metoda zwraca ilosc trafionych pol statku.
	 * 
	 * @return Ilosc trafionych pol.
	 */
	public int getNumberOfHits()
		{
		return iHitsQuantity;
		}
	/**
	 * Metoda zwraca informacje, czy statek nie otrzymal zadnych trafien.
	 * 
	 * @return Zwraca TRUE, jesli zadne z pol statku nie zostalo trafione, lub FALSE,
	 * jesli jest conajmniej jedno trafienie.
	 */
	public boolean getUntouched()
		{
		if (iHitsQuantity == 0)
			return true;
		else
			return false;
		}
	/**
	 * Metoda zwraca informacje, czy statek zostal chociaz raz trafiony.
	 * 
	 * @return Zwraca TRUE, jesli statek ma conajmniej jedno trafione pole, lub FALSE,
	 * jesli wszystkie pola sa nietkniete.
	 */
	public boolean getHits()
		{
		if (iHitsQuantity > 0)
			return true;
		else
			return false;
		}
	/**
	 * Metoda zwraca informacje, czy statek zostal zatopiony, czyli wszystkie jego pola zostaly trafione.
	 * 
	 * @return Zwraca TRUE, jesli wszystkie pola statku zostaly trafione, lub FALSE,
	 * jesli choc jedno pole pozostaje nietrafione.
	 */
	public boolean getSunk()
		{
		if (iHitsQuantity > 0 && iHitsQuantity == iSize)
			return true;
		else
			return false;
		}
	/**
	 * Metoda zwraca referencja obiektu planszy, na ktorej sie znajduje.
	 * 
	 * @return Board, na ktorej umieszczony jest statek.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Pozwala ustawic wspolrzedne pola o podanym numerze.<br />
	 * 
	 * Metoda dba takze o to, aby na planszy odpowiednio oznaczyc zajmowane przez statek miejsca.
	 * 
	 * @param iNumberFields Numer pola statku (liczone od 1).
	 * @param iX Wspolrzedna X na planszy (liczone od 0).
	 * @param iY Wspolrzedna Y na planszy (liczone od 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer pola jest poza zakresem pol statku,
	 * gdy wspolrzedne znajduja sie poza zakresem planszy, lub gdy na danym polu planszy nie mozna umiescic statku
	 */
	public void setField(int iNumberFields, int iX, int iY) throws ParameterException
		{
		if (iNumberFields > iSize)
			throw new ParameterException("iNumberFields = " + iNumberFields);
		if (iX + 1 > oBoard.getWidth() || iX < -1)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > oBoard.getHeight() || iY < -1)
			throw new ParameterException("iY = " + iY);
		if (iX >= 0 && iY >= 0 && oBoard.getField(iX, iY) != FieldTypeBoard.BOARD_FIELD_EMPTY)
			throw new ParameterException("iX, iY - pole niepuste");
		if (aCoordenates[ iNumberFields - 1].getX() == -1 && aCoordenates[ iNumberFields - 1 ].getY() == -1)
			{
			//pierwsze ustawienie wspolrzednych
			if (iX >= 0 && iY >= 0)
				{
				aCoordenates[ iNumberFields - 1 ].setX(iX);
				aCoordenates[ iNumberFields - 1 ].setY(iY);
				oBoard.setField(iX, iY, FieldTypeBoard.SHIP_BOARD);
				}
			}
		else
			{
			//pole juz ma ustawione wspolrzedne
			//zerowanie wspolrzednych
			oBoard.setField(aCoordenates[ iNumberFields - 1 ].getX(), aCoordenates[ iNumberFields - 1 ].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			aCoordenates[ iNumberFields - 1 ].setX(-1);
			aCoordenates[ iNumberFields - 1 ].setY(-1);
			//ponowne wywolanie metody
			setField(iNumberFields, iX, iY);
			}
		}
	/**
	 * Metoda ustawia wspolrzedne wszystkich pol statku na poczatkowe Values domyslne (-1, -1).
	 */
	public void resetFields()
		{
		try
			{
			for (int i = 1; i <= iSize; ++i)
				setField(i, -1, -1);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda sprawdza czy shot na podane wspolrzedne jest celny.<br />
	 * 
	 * Jesli tak, oznacza pole statku, jako trafione i zwraca TRUE,
	 * w przeciwnym wypadku zwraca FALSE.<br />
	 * 
	 * Metoda dba takze o prawidlowe oznaczenie ostrzeliwanych pol na planszy.<br />
	 * 
	 * @param iX Wspolrzedna X shotu.
	 * @param iY Wspolrzedna Y shotu.
	 * @return Zwraca TRUE w przypadku Hits, lub FALSE, jesli shot byl niecelny.
	 * @throws ParameterException Wyrzuca wyjatek, jesli podane wspolrzedne znajduja sie poza zakresem planszy.
	 */
	public boolean shot(int iX, int iY) throws ParameterException
		{
		if (iX < 0 || iX >= oBoard.getWidth())
			throw new ParameterException("iX = " + iX);
		if (iY < 0 || iY >= oBoard.getHeight())
			throw new ParameterException("iY = " + iY);
		try
			{
			for (int i = 0; i < iSize; ++i)
				if (aCoordenates[i].getX() == iX && aCoordenates[i].getY() == iY && aHits[i] == false)
					{
					//nastapilo trafienie
					aHits[i] = true;
					++iHitsQuantity;
					oBoard.setField(iX, iY, FieldTypeBoard.CUSTOMS_SHOT_BOARD);
					if (getSunk() == true)
						{
						//oznaczenie pol sasiadujacych ze statkiem jako niedostepne
						for (int j = 1; j <= iSize; ++j)
							{
							Position oField = getField(j);
							for (int k = -1; k <= 1; ++k)
								for (int l = -1; l <= 1; ++l)
									{
									Position oAdjacentField = new Position(2);
									oAdjacentField.setX(oField.getX() + k);
									oAdjacentField.setY(oField.getY() + l);
									if (oAdjacentField.getX() < 0 || oAdjacentField.getX() >= oBoard.getWidth()
										|| oAdjacentField.getY() < 0 || oAdjacentField.getY() >= oBoard.getHeight()
										)
										continue;
									if (oBoard.getField(oAdjacentField.getX(), oAdjacentField.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY)
										oBoard.setField(oAdjacentField.getX(), oAdjacentField.getY(), FieldTypeBoard.BOARD_FIELD_UNAVAILABLE);
									}
							}
						}
					return true;
					}
			oBoard.setField(iX, iY, FieldTypeBoard.BOARD_SHOT_FALSE);
			return false;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * metoda zwraca informacje, czy statek jest zatopiony
	 * 
	 * @deprecated zastapiana przez metode {@link #getSunk()}
	 * @return zwraca TRUE, jesli statek jest zatopiony, lub FALSE w przeciwnym wypadku
	 */
	public boolean isSunken()
		{
		if (iSize > iHitsQuantity)
			return false;
		else
			return true;
		}
	}
