package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * A class representing a ship in board.
 * 
 * @author VGT
 * @version 1.0
 */
public class Ship
	{
	/**
	 * Reference to the board object where the ship will be placed.
	 */
	private Board oBoard;
	/**
	 * The size of the vessel is the number of halves it occupies on the board.
	 */
	private int iSize;
	/**
	 * A table containing the co-ordinates of the various fields of the ship.
	 */
	private Position[] aCoordenates;
	/**
	 * A table storing information whether or not individual ship positions have been hit.
	 */
	private boolean[] aHits;
	/**
	 * Number of hits hit by the ship.
	 */
	private int iHitsQuantity;
	/**
	 * Default constructor - creates a new ship with the given Sizeze.
	 * 
	 * @param iSize The number of spaces the ship takes on board.
	 * @param oBoard A reference to the board object where the ship is to be placed.
	 */
	public Ship(int iSize, Board oBoard)
		{
		this.oBoard = oBoard;
		this.iSize = iSize;
		aCoordenates = new Position[ iSize ];
		//padding the coordinate table with default values ​​(-1, -1)
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
	 * Display information about the ship on the standard exit.
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
	 * Returns an object containing co-ordinates position with the given number.
	 * 
	 * @param iNumberFields 
The position number whose co-ordinates are to be returned (counted from 1).
	 * @return co-ordinates position with the given number.
	 * @throws ParameterException Throws an exception if the position number is less than 1 or greater from Ship Size. 
	 */
	public Position getField(int iNumberFields) throws ParameterException
		{
		if (iNumberFields > iSize || iNumberFields <= 0)
			throw new ParameterException("iNumberFields = " + iNumberFields);
		return aCoordenates[ iNumberFields - 1 ];
		}
	/**
	 * If there is a ship field in the given coordinates, the method returns its number,
	 * otherwise it returns 0.
	 * 
	 * @param iX Coordinate X in board.
	 * @param iY Coordinate Y in board.
	 * @return Ship position number (counted from 1).
	 * @throws ParameterException Throws an exception if the given co-ordinates is outside the range of the board.
	 */
	public int getNumberPosition(int iX, int iY) throws ParameterException
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
	 * The method returns the Ship Size.
	 * 
	 * @return Ship size.
	 */
	public int getSize()
		{
		return iSize;
		}
	/**
	 * The method returns the number of ship halves hit.
	 * 
	 * @return Number of poles hit.
	 */
	public int getNumberOfHits()
		{
		return iHitsQuantity;
		}
	/**
	 * The method returns whether the ship has not received any hits.
	 * 
	 * @return Returns TRUE if neither half of the ship has been hit, or FALSE.
	 * if there is at least one hit.
	 */
	public boolean getUntouched()
		{
		if (iHitsQuantity == 0)
			return true;
		else
			return false;
		}
	/**
	 * The method returns information whether the ship has been hit at least once.
	 * 
	 * @return Returns TRUE if the ship has at least one hit square, or FALSE,
	 * if all positions are intact.
	 */
	public boolean getHits()
		{
		if (iHitsQuantity > 0)
			return true;
		else
			return false;
		}
	/**
	 * The method returns information whether the ship has been sunk, i.e. all its positions have been hit.
	 * 
	 * @return Returns TRUE if all ship positions are hit, or FALSE,
	 * if at least one square is missed.
	 */
	public boolean getSunk()
		{
		if (iHitsQuantity > 0 && iHitsQuantity == iSize)
			return true;
		else
			return false;
		}
	/**
	 * The method returns a reference of the board object it is on.
	 * 
	 * @return The board where the ship is placed.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Allows you to set co-ordinates position with the given number. <br />
	 * 
	 * The method also ensures that the spaces occupied by the ship are properly marked on the board.
	 * 
	 * @param iNumberFields Numer position statku (counted from 1).
	 * @param iX Coordinate X na board (counted from 0).
	 * @param iY Coordinate Y na board (counted from 0).
	 * @throws ParameterException Throws an exception if the position number is outside the range of the ship half,
	 * when co-ordinates are outside the scope of the board, or when it is not possible to place a ship on the given board space
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
			//first coordinate setting
			if (iX >= 0 && iY >= 0)
				{
				aCoordenates[ iNumberFields - 1 ].setX(iX);
				aCoordenates[ iNumberFields - 1 ].setY(iY);
				oBoard.setField(iX, iY, FieldTypeBoard.SHIP_BOARD);
				}
			}
		else
			{
			//the field already has co-ordinates set
			//resetting the coordinates
			oBoard.setField(aCoordenates[ iNumberFields - 1 ].getX(), aCoordenates[ iNumberFields - 1 ].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			aCoordenates[ iNumberFields - 1 ].setX(-1);
			aCoordenates[ iNumberFields - 1 ].setY(-1);
			//resetting the coordinates
			setField(iNumberFields, iX, iY);
			}
		}
	/**
	 * The method sets the co-ordinates of all half of the ship to the initial default Values ​​(-1, -1).
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
	 * The method checks if the shot for the given co-ordinates is accurate. <br />
	 * 
	 * If so, marks the ship's space as hit and returns TRUE.
	 * otherwise it returns FALSE. <br />
	 * 
	 * The method also ensures the correct marking of the shot fields on the board. <br />
	 * 
	 * @param iX Coordinate X shot.
	 * @param iY Coordinate Y shot.
	 * @return Returns TRUE for a hit, or FALSE if the shot was missed.
	 * @throws ParameterException Throws an exception if the co-ordinates given are outside the scope of the board.
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
					//there was a hit
					aHits[i] = true;
					++iHitsQuantity;
					oBoard.setField(iX, iY, FieldTypeBoard.CUSTOMS_SHOT_BOARD);
					if (getSunk() == true)
						{
						//marking the fields adjacent to the ship as unavailable
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
	 * the method returns whether the ship is sunk
	 * 
	 * @deprecated replaced by method {@link #getSunk()}
	 * @return returns TRUE if the ship is sunk, FALSE otherwise
	 */
	public boolean isSunken()
		{
		if (iSize > iHitsQuantity)
			return false;
		else
			return true;
		}
	}
