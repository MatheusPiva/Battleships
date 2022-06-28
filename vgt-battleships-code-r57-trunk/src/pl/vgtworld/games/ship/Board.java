package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;

/**
 * A class representing the game board.
 * 
 * @author VGT
 * @version 1.0
 */
public class Board
	{
	/**
	 * The width of the board.
	 */
	private int iWidth;
	/**
	 * The height of the board.
	 */
	private int iHeight;
	/**
	 * The number of fields on the board unknown to the opponent, i.e. available for shooting.<br />
	 * 
	 * These are empty fields, and fields containing a ship.
	 */
	private int iUnknownFields;
	/**
	 * A board storing the state of individual fields of the board.
	 */
	FieldTypeBoard[][] aBoard;
	/**
	 * Default constructor - creates boards with a default size of 10x10.
	 */
	public Board()
		{
		this(10, 10);
		}
	/**
	 * Overload constructor - allows you to create a square-shaped board with a given side length.
	 * 
	 * @param iSize A value specifying the width and height of the board creation.
	 */
	public Board(int iSize)
		{
		this(iSize, iSize);
		}
	/**
	 * Overload constructor - allows you to create boards of any width and height.
	 * 
	 * @param iWidth A value specifying the width of the board creation.
	 * @param iHeight A value specifying the height of the board creation.
	 */
	public Board(int iWidth, int iHeight)
		{
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		iUnknownFields = iWidth * iHeight;
		aBoard = new FieldTypeBoard[ iWidth ][ iHeight ];
		clean();
		}
	/**
	 * Displaying the board on the standard output.
	 */
	@Override public String toString()
		{
		String sReturn = "Board\n";
		sReturn+= "Size: [ " + iWidth + ", " + iHeight + " ]\n";
		for (int j = 0; j < iHeight; ++j)
			{
			sReturn+= "|";
			for (int i = 0; i < iWidth; ++i)
				{
				FieldTypeBoard eField = aBoard[i][j];
				switch (eField)
					{
					case BOARD_FIELD_EMPTY:
						sReturn+= "_";
						break;
					case SHIP_BOARD:
						sReturn+= "S";
						break;
					case CUSTOMS_SHOT_BOARD:
						sReturn+= "X";
						break;
					case BOARD_SHOT_FALSE:
						sReturn+= "-";
						break;
					case BOARD_FIELD_UNAVAILABLE:
						sReturn+= "!";
						break;
					default:
						sReturn+= "?";
						break;
					}
				sReturn+= "|";
				}
			if (j + 1 < iHeight)
				sReturn+="\n";
			}
		return sReturn;
		}
	/**
	 * Returns the width of the board.
	 * 
	 * @return The size of the board horizontally.
	 */
	public int getWidth()
		{
		return iWidth;
		}
	/**
	 * Returns the height of the board.
	 * 
	 * @return The size of the board vertically.
	 */
	public int getHeight()
		{
		return iHeight;
		}
	/**
	 * Returns the number of unknown fields on the board.
	 * 
	 * @return Number of unknown poles.
	 */
	public int getUnknownQuantity()
		{
		return iUnknownFields;
		}
	/**
	 * The method returns the field type for the given coordinates.
	 * 
	 * @param iX The X coordinate on the board (counted from 0).
	 * @param iY The Y coordinate on the board (counted from 0).
	 * @return Returns the field type for the given coordinates.
	 * @throws ParameterException Throws an exception when the coordinates 
         * are out of range of the board.
	 */
	public FieldTypeBoard getField(int iX, int iY) throws ParameterException
		{
		if (iX >= iWidth || iX < 0)
			throw new ParameterException("iX = " + iX);
		if (iY >= iHeight || iY < 0)
			throw new ParameterException("iY = " + iY);
		return aBoard[iX][iY];
		}
	/**
	 * Lets you set the field type to the given coordinates. <br />
 
 The method also ensures that the value of the Unknown Fields variable is updated when this value changes.
	 * 
	 * @param iX X coordinate on the board (counted from 0).
	 * @param iY Y coordinate on the board (counted from 0).
	 * @param eType Type of the field, on which the field with the given coordinates is to be set.
	 * @throws ParameterException Throws an exception when the coordinates are out of range of the board.
	 */
	public void setField(int iX, int iY, FieldTypeBoard eType) throws ParameterException
		{
		if (iX + 1 > iWidth)
			throw new ParameterException("iX = " + iX);
		if (iY + 1 > iHeight)
			throw new ParameterException("iY = " + iY);
		FieldTypeBoard eOldType = aBoard[iX][iY];
		aBoard[iX][iY] = eType;
		//determining whether the number of fields unknown has changed and possible correction of this value
		if (
			(eOldType == FieldTypeBoard.BOARD_FIELD_EMPTY || eOldType == FieldTypeBoard.SHIP_BOARD)
			&& (eType == FieldTypeBoard.BOARD_SHOT_FALSE || eType == FieldTypeBoard.CUSTOMS_SHOT_BOARD || eType == FieldTypeBoard.BOARD_FIELD_UNAVAILABLE)
			)
			{
			--iUnknownFields;
			}
		else if (
			(eOldType == FieldTypeBoard.BOARD_SHOT_FALSE || eOldType == FieldTypeBoard.CUSTOMS_SHOT_BOARD || eOldType == FieldTypeBoard.BOARD_FIELD_UNAVAILABLE)
			&& (eType == FieldTypeBoard.BOARD_FIELD_EMPTY || eType == FieldTypeBoard.SHIP_BOARD)
			)
			{
			++iUnknownFields;
			}
		}
	/**
	 * The method allows you to resize an already created board.<br />
	 * 
	 * To avoid the potential loss of information, do not use on a board filled with ships.
	 * For this reason, additionally after resizing, the {@link #clear ()} method is called.
	 * 
	 * @param iWidth The new width of the board.
	 * @param iHeight The new height of the board.
	 * @throws ParameterException Throws an exception if the given width and / or height is less than 1.
	 */
	 //TODO zmienSize
	public void zmienSize(int iWidth, int iHeight) throws ParameterException
		{
		if (iWidth < 1)
			throw new ParameterException("iWidth = " + iWidth);
		if (iHeight < 1)
			throw new ParameterException("iHeight = " + iHeight);
		this.iWidth = iWidth;
		this.iHeight = iHeight;
		iUnknownFields = iWidth * iHeight;
		aBoard = new FieldTypeBoard[ iWidth ][ iHeight ];
		clean();
		}
	/**
	 * The method clears the whole board by setting the type of all fields to "empty".
	 */
	public void clean()
		{
		for (int i = 0; i < iWidth; ++i)
			for (int j = 0; j < iHeight; ++j)
				aBoard[i][j] = FieldTypeBoard.BOARD_FIELD_EMPTY;
		iUnknownFields = iWidth * iHeight;
		}
	}
