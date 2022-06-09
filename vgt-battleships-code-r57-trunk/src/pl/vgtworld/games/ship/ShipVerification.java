package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa sprawdzajaca, czy statek spelnia rozne warunki
 * odnosnie jego struktury, czy rozmieszczenia na board.<br />
 * 
 * <p>
 * aktualizacje:<br />
 * 1.1<br />
 * - dodanie parametru bStraightLines do metody {@link #fieldsConnected(boolean)}.<br />
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
	public void importShip(Ship oShip)
		{
		this.oShip = oShip;
		oBoard = oShip.getBoard();
		}
	/**
	 * Metoda sprawdza, czy wszystkie position statku znajduja sie na board.
	 * 
	 * @return Zwraca TRUE jesli statek w calosci jest na board, lub FALSE w przeciwnym wypadku.
	 */
	public boolean spacesOnBoard()
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oField = oShip.getField(i);
				if (oField.getX() == -1 || oField.getX() >= oBoard.getWidth()
					|| oField.getY() == -1 || oField.getY() >= oBoard.getHeight()
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
	public boolean fieldsConnected()
		{
		return fieldsConnected(false);
		}
	/**
	 * Metoda sprawdza, czy wszystkie position danego statku tworza jednolita strukture
	 * (stykaja sie krawedziami i nie tworza dwoch lub wiecej niepolaczonych obszarow na board).<br />
	 * 
	 * aktualizacje:<br />
	 * 
	 * 1.1 - dodanie pierwszego parametru
	 * 
	 * @param bStraightLines Okresla, czy position musza byc w jednej linii pionowej lub poziomej.
	 * @return Zwraca TRUE, jesli statek jest prawidlowo zbudowany, lub FALSE w przeciwnym wypadku.
	 */
	public boolean fieldsConnected(boolean bStraightLines)
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			int iQuantityValid = 0;
			boolean[] aCorrect = new boolean[ oShip.getSize() ];
			for (int i = 0; i < oShip.getSize(); ++i)
				aCorrect[i] = false;
			//pierwsze pole statku prawidlowe z automatu
			++iQuantityValid;
			aCorrect[0] = true;
			boolean bChanges = true;
			//petla wykonujaca sie dopoki nastepuja jakies zmiany w ilosci prawidlowych pol
			while (bChanges == true)
				{
				bChanges = false;
				for (int i = 0; i < oShip.getSize(); ++i)
					if (aCorrect[i] == true)
						{
						Position oValidField = oShip.getField(i+1);
						for (int j = -1; j <= 1; ++j)
							for (int k = -1; k <= 1; ++k)
								{
								if (oValidField.getX() + j < 0 || oValidField.getX() + j >= oBoard.getWidth()
									|| oValidField.getY() + k < 0 || oValidField.getY() + k >= oBoard.getHeight()
									)
									continue;
								int iNumberFields = oShip.getNumerPola(oValidField.getX() + j, oValidField.getY() + k);
								if (iNumberFields > 0 && aCorrect[iNumberFields - 1] == false)
									{
									bChanges = true;
									++iQuantityValid;
									aCorrect[iNumberFields - 1] = true;
									}
								}
						}
				}
			
			if (iQuantityValid == oShip.getSize())
				{
				//dodatkowe sprawdzenie, czy position tworza linie, jesli wymagane
				if (bStraightLines == true)
					{
					int iX = -1;
					int iY = -1;
					boolean bLevel = true;
					boolean bVertical = true;
					for (int i = 1; i <= iQuantityValid; ++i)
						{
						if (iX == -1)
							iX = oShip.getField(i).getX();
						else if (iX != oShip.getField(i).getX())
							bLevel = false;
						if (iY == -1)
							iY = oShip.getField(i).getY();
						else if (iY != oShip.getField(i).getY())
							bVertical = false;
						}
					if (bLevel == true || bVertical == true)
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
	public boolean NoNeighbors()
		{
		if (oShip == null)
			throw new DeveloperException("Ship item not imported");
		try
			{
			for (int i = 1; i <= oShip.getSize(); ++i)
				{
				Position oField = oShip.getField(i);
				for (int j = -1; j <= 1; ++j)
					for (int k = -1; k <=1; ++k)
						{
						Position oAdjacentField = new Position(2);
						oAdjacentField.setX(oField.getX() + j);
						oAdjacentField.setY(oField.getY() + k);
						//odrzucenie sprawdzania pol, ktore laduja poza zakresem board
						if (oAdjacentField.getX() < 0 || oAdjacentField.getX() >= oBoard.getWidth()
							|| oAdjacentField.getY() < 0 || oAdjacentField.getY() >= oBoard.getHeight()
							)
							continue;
						if (oBoard.getField(oAdjacentField.getX(), oAdjacentField.getY()) == FieldTypeBoard.SHIP_BOARD
							&& oShip.getNumerPola(oAdjacentField.getX(), oAdjacentField.getY()) == 0
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
