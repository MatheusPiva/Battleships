package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa sprawdzajaca, czy statek spelnia rozne warunki
 * odnosnie jego struktury, czy rozmieszczenia na planszy.<br />
 * 
 * <p>
 * aktualizacje:<br />
 * 1.1<br />
 * - dodanie parametru bStraightLines do metody {@link #polaPolaczone(boolean)}.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public class ShipVerification
	{
	/**
	 * Obiekt statku, ktory bedzie weryfikowany.
	 */
	private Ship oShip;
	/**
	 * Board, na ktorej znajduje sie weryfikowany statek.
	 */
	private Board oBoard;
	/**
	 * Konstruktor domyslny.
	 */
	public ShipVerification()
		{
		oShip = null;
		oBoard = null;
		}
	/**
	 * Import statku, dla ktorego maja byc wykonywane testy.
	 * 
	 * @param oShip Obiekt statku do testow.
	 */
	public void importujStatek(Ship oShip)
		{
		this.oShip = oShip;
		oBoard = oShip.getBoard();
		}
	/**
	 * Metoda sprawdza, czy wszystkie pola statku znajduja sie na planszy.
	 * 
	 * @return Zwraca TRUE jesli statek w calosci jest na planszy, lub FALSE w przeciwnym wypadku.
	 */
	public boolean polaNaPlanszy()
		{
		if (oShip == null)
			throw new DeveloperException("obiekt statku niezaimportowany");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oPole = oShip.getField(i);
				if (oPole.getX() == -1 || oPole.getX() >= oBoard.getWidth()
					|| oPole.getY() == -1 || oPole.getY() >= oBoard.getHeight()
					)
					return false;
				}
			return true;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * @deprecated
	 */
	public boolean polaPolaczone()
		{
		return polaPolaczone(false);
		}
	/**
	 * Metoda sprawdza, czy wszystkie pola danego statku tworza jednolita strukture
	 * (stykaja sie krawedziami i nie tworza dwoch lub wiecej niepolaczonych obszarow na planszy).<br />
	 * 
	 * aktualizacje:<br />
	 * 
	 * 1.1 - dodanie pierwszego parametru
	 * 
	 * @param bStraightLines Okresla, czy pola musza byc w jednej linii pionowej lub poziomej.
	 * @return Zwraca TRUE, jesli statek jest prawidlowo zbudowany, lub FALSE w przeciwnym wypadku.
	 */
	public boolean polaPolaczone(boolean bStraightLines)
		{
		if (oShip == null)
			throw new DeveloperException("obiekt statku niezaimportowany");
		try
			{
			int iIloscPrawidlowych = 0;
			boolean[] aPrawidlowe = new boolean[ oShip.getSize() ];
			for (int i = 0; i < oShip.getSize(); ++i)
				aPrawidlowe[i] = false;
			//pierwsze pole statku prawidlowe z automatu
			++iIloscPrawidlowych;
			aPrawidlowe[0] = true;
			boolean bZmiany = true;
			//petla wykonujaca sie dopoki nastepuja jakies zmiany w ilosci prawidlowych pol
			while (bZmiany == true)
				{
				bZmiany = false;
				for (int i = 0; i < oShip.getSize(); ++i)
					if (aPrawidlowe[i] == true)
						{
						Position oPolePrawidlowe = oShip.getField(i+1);
						for (int j = -1; j <= 1; ++j)
							for (int k = -1; k <= 1; ++k)
								{
								if (oPolePrawidlowe.getX() + j < 0 || oPolePrawidlowe.getX() + j >= oBoard.getWidth()
									|| oPolePrawidlowe.getY() + k < 0 || oPolePrawidlowe.getY() + k >= oBoard.getHeight()
									)
									continue;
								int iNumberPola = oShip.getNumerPola(oPolePrawidlowe.getX() + j, oPolePrawidlowe.getY() + k);
								if (iNumberPola > 0 && aPrawidlowe[iNumberPola - 1] == false)
									{
									bZmiany = true;
									++iIloscPrawidlowych;
									aPrawidlowe[iNumberPola - 1] = true;
									}
								}
						}
				}
			
			if (iIloscPrawidlowych == oShip.getSize())
				{
				//dodatkowe sprawdzenie, czy pola tworza linie, jesli wymagane
				if (bStraightLines == true)
					{
					int iX = -1;
					int iY = -1;
					boolean bPoziom = true;
					boolean bPion = true;
					for (int i = 1; i <= iIloscPrawidlowych; ++i)
						{
						if (iX == -1)
							iX = oShip.getField(i).getX();
						else if (iX != oShip.getField(i).getX())
							bPoziom = false;
						if (iY == -1)
							iY = oShip.getField(i).getY();
						else if (iY != oShip.getField(i).getY())
							bPion = false;
						}
					if (bPoziom == true || bPion == true)
						return true;
					else
						return false;
					}
				else
					return true;
				}
			else
				return false;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda sprawdza, czy ktorekolwiek pole statku styka sie z innym statkiem.
	 * 
	 * @return Zwraca FALSE, jesli wystapilo zetkniecie z innym statkiem, lub TRUE w przeciwnym wypadku. 
	 */
	public boolean brakSasiadow()
		{
		if (oShip == null)
			throw new DeveloperException("obiekt statku niezaimportowany");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oPole = oShip.getField(i);
				for (int j = -1; j <= 1; ++j)
					for (int k = -1; k <=1; ++k)
						{
						Position oSasiedniePole = new Position(2);
						oSasiedniePole.setX(oPole.getX() + j);
						oSasiedniePole.setY(oPole.getY() + k);
						//odrzucenie sprawdzania pol, ktore laduja poza zakresem planszy
						if (oSasiedniePole.getX() < 0 || oSasiedniePole.getX() >= oBoard.getWidth()
							|| oSasiedniePole.getY() < 0 || oSasiedniePole.getY() >= oBoard.getHeight()
							)
							continue;
						if (oBoard.getField(oSasiedniePole.getX(), oSasiedniePole.getY()) == FieldTypeBoard.SHIP_BOARD
							&& oShip.getNumerPola(oSasiedniePole.getX(), oSasiedniePole.getY()) == 0
							)
							return false;
						}
				}
			return true;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
