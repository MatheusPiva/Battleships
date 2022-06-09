package pl.vgtworld.tools;

import java.util.Arrays;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;

/**
 * Klasa sluzaca do obslugi pozycji w n-wymiarowym zakresie wspolrzednych.<br />
 * 
 * <p>
 * aktualizacje:<br />
 * 1.2.1<br />
 * - poprawki w dokumentacji<br />
 * 1.2<br />
 * - dodanie metod {@link #getZ()}, {@link #setZ(int)}, {@link #przesunZ(int)}, {@link #clone()}<br />
 * 1.1<br />
 * - dodanie obslugi wyjatkow w przypadku odwolywania sie do wymiarow poza zakresem<br />
 * </p>
 * 
 * @author VGT
 * @version 1.2.1
 */
public class Position
	{
	/**
	 * Przechowuje informacje o ilosci wymiarow danej instancji obiektu.
	 */
	int iNumberOfDimensions;
	/**
	 * Przechowuje co-ordinates poszczegolnych wymiarow.
	 */
	int[] aDimensions;
	/**
	 * Konstruktor domyslny. Tworzy obiekt o dwoch wymiarach.
	 */
	public Position()
		{
		this(2);
		}
	/**
	 * Konstruktor przeciazony pozwalajacy okreslic ilosc wymiarow obiektu.
	 * 
	 * @param iNumberOfDimensions Ilosc wymiarow obiektu.
	 */
	public Position(int iNumberOfDimensions)
		{
		this.iNumberOfDimensions = iNumberOfDimensions;
		aDimensions = new int[ iNumberOfDimensions ];
		for (int i = 0; i < iNumberOfDimensions; ++i)
			aDimensions[ i ] = 0;
		}
	/**
	 * Przeslonieta wersja metody toString().
	 */
	@Override public String toString()
		{
		return Arrays.toString(aDimensions);
		}
	/**
	 * Metoda zwraca pozycje zapisana na podanym w parametrze wymiarze.
	 * 
	 * @param iDimensionNumber Numer wymiaru, dla ktorego ma byc zwrocona Position (counted from 1).
	 * @return Zwraca Position obiektu na danym wymiarze.
	 * @throws ParameterException Wyrzuca wyjatek, jesli przekazany numer wymiaru jest poza zakresem.
	 */
	public int getDimension(int iDimensionNumber) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		return aDimensions[ iDimensionNumber - 1 ];
		}
	/**
	 * Uproszczona wersja metody {@link #getDimension(int)} zwracajaca pozycje pierwszego wymiaru.
	 * 
	 * @return Zwraca pozycje pierwszego wymiaru.
	 */
	public int getX()
		{
		try
			{
			return getDimension(1);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #getDimension(int)} zwracajaca pozycje drugiego wymiaru.
	 * 
	 * @return Zwraca pozycje drugiego wymiaru.
	 */
	public int getY()
		{
		try
			{
			return getDimension(2);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #getDimension(int)} zwracajaca pozycje trzeciego wymiaru.
	 * 
	 * @since 1.2
	 * @return Zwraca pozycje trzeciego wymiaru.
	 */
	public int getZ()
		{
		try
			{
			return getDimension(3);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda zapisuje pozycje w podanym wymiarze.
	 * 
	 * @param iDimensionNumber Numer wymiaru, dla ktorego ma byc zapisana Position (counted from 1).
	 * @param iPosition Position obiektu w danym wymiarze, na ktora ma byc ustawiony.
	 * @throws ParameterException Wyrzuca wyjatek, jesli przekazany numer wymiaru jest poza zakresem.
	 */
	public void setDimension(int iDimensionNumber, int iPosition) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		aDimensions[ iDimensionNumber - 1 ] = iPosition;
		}
	/**
	 * Uproszczona wersja metody {@link #setDimension(int, int)} ustawiajaca pozycje dla pierwszego wymiaru.
	 * 
	 * @param iPosition Position obiektu w pierwszym wymiarze.
	 */
	public void setX(int iPosition)
		{
		try
			{
			setDimension(1, iPosition);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #setDimension(int, int)} ustawiajaca pozycje dla drugiego wymiaru.
	 * 
	 * @param iPosition Position obiektu w drugim wymiarze.
	 */
	public void setY(int iPosition)
		{
		try
			{
			setDimension(2, iPosition);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #setDimension(int, int)} ustawiajaca pozycje dla trzeciego wymiaru.
	 * 
	 * @since 1.2
	 * @param iPosition Position obiektu w trzecim wymiarze.
	 */
	public void setZ(int iPosition)
		{
		try
			{
			setDimension(3, iPosition);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda przesuwa pozycje na danym wymiarze o dana wartosc.
	 * 
	 * @param iDimensionNumber Numer wymiaru, ktorego Position ma byc przesunieta (counted from 1).
	 * @param iOffsetValue Wartosc przesuniecia danego wymiaru.
	 * @throws ParameterException Wyrzuca wyjatek, jesli numer wymiaru jest poza zakresem.
	 */
	public void shiftDimension(int iDimensionNumber, int iOffsetValue) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		aDimensions[ iDimensionNumber - 1 ]+= iOffsetValue;
		}
	/**
	 * Uproszczona wersja metody {@link #shiftDimension(int, int)} pozwalajaca przesunac pozycje obiektu na pierwszym wymiarze.
	 * 
	 * @param iOffsetValue Wartosc przesuniecia pozycji na pierwszym wymiarze.
	 */
	public void shiftX(int iOffsetValue)
		{
		try
			{
			shiftDimension(1, iOffsetValue);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #shiftDimension(int, int)} pozwalajaca przesunac pozycje obiektu na drugim wymiarze.
	 * 
	 * @param iOffsetValue Wartosc przesuniecia pozycji na drugim wymiarze.
	 */
	public void shiftY(int iOffsetValue)
		{
		try
			{
			shiftDimension(2, iOffsetValue);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Uproszczona wersja metody {@link #shiftDimension(int, int)} pozwalajaca przesunac pozycje obiektu na trzecim wymiarze.
	 * 
	 * @since 1.2
	 * @param iOffsetValue Wartosc przesuniecia pozycji na trzecim wymiarze.
	 */
	public void przesunZ(int iOffsetValue)
		{
		try
			{
			shiftDimension(3, iOffsetValue);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Przeslonieta wersja metody klasy Object tworzaca kopie obiektu.
	 * 
	 * @since 1.2
	 * @return Zwraca referencje do utworzonej kopii obiektu.
	 */
	@Override public Object clone()
		{
		Position oRef = new Position(iNumberOfDimensions);
		try
			{
			for (int i = 1; i <= iNumberOfDimensions; ++i)
				oRef.setDimension(i, getDimension(i));
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		return oRef;
		}
	/**
	 * Przeslonieta wersja metody klasy Object porownujaca dwa obiekty Position.
	 * 
	 * @param oObj Obiekt do porownania.
	 * @return Zwraca TRUE, jesli obydwa obiekty sa tego samego typu, maja taka sama ilosc wymiarow
	 * i takie same pozycje na wszystkich wymiarach, w przeciwnym wypadku zwraca FALSE. 
	 */
	@Override public boolean equals(Object oObj)
		{
		if (oObj == null)
			return false;
		
		if (getClass() != oObj.getClass())
			return false;
		
		Position oPosition = (Position)oObj;
		
		if (oPosition.iNumberOfDimensions != iNumberOfDimensions)
			return false;
		for (int i = 0; i < iNumberOfDimensions; ++i)
			if (aDimensions[i] != oPosition.aDimensions[i])
				return false;
		return true;
		}
	}
