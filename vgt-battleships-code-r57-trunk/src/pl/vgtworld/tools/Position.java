package pl.vgtworld.tools;

import java.util.Arrays;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;

/**
 * A class to handle positions in the n-dimensional coordinate range. <br />
 *
 * <p>
 * updates: <br />
 * 1.2.1 <br />
 * - documentation fixes <br />
 * 1.2 <br />
 * - adding methods {@link #getZ ()}, {@link #setZ (int)}, {@link #przesunZ (int)}, {@link #clone ()} <br />
 * 1.1 <br />
 * - added support for exceptions when referencing dimensions outside the range <br />
 * </p>
 * 
 * @author VGT
 * @version 1.2.1
 */
public class Position
	{
	/**
	 * It stores information about the number of dimensions of a given object instance.
	 */
	int iNumberOfDimensions;
	/**
	 * It stores the co-ordinates of the various dimensions.
	 */
	int[] aDimensions;
	/**
	 * The default constructor. Creates an object with two dimensions.
	 */
	public Position()
		{
		this(2);
		}
	/**
	 * Constructor overloaded allowing to define the number of dimensions of the object.
	 * 
	 * @param iNumberOfDimensions The number of dimensions of the object.
	 */
	public Position(int iNumberOfDimensions)
		{
		this.iNumberOfDimensions = iNumberOfDimensions;
		aDimensions = new int[ iNumberOfDimensions ];
		for (int i = 0; i < iNumberOfDimensions; ++i)
			aDimensions[ i ] = 0;
		}
	/**
	 * Obscured version of the toString () method.
	 */
	@Override public String toString()
		{
		return Arrays.toString(aDimensions);
		}
	/**
	 * The method returns the position written on the dimension specified in the parameter.
	 * 
	 * @param iDimensionNumber Number of the dimension on which to return Position (counted from 1).
	 * @return Returns the Position of the object on the given dimension.
	 * @throws ParameterException Throws an exception if the passed dimension number is out of range.
	 */
	public int getDimension(int iDimensionNumber) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		return aDimensions[ iDimensionNumber - 1 ];
		}
	/**
	 * A simplified version of the {@link #getDimension (int)} method that returns the positions of the first dimension.
	 * 
	 * @return Returns the positions of the first dimension.
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
	 * A simplified version of the {@link #getDimension (int)} method that returns the positions of the second dimension.
	 *
	 * @return Returns the positions of the second dimension.
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
	 * A simplified version of the {@link #getDimension (int)} method that returns the items of the third dimension.
	 * 
	 * @since 1.2
	 * @return Returns the positions of the third dimension.
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
	 * The method saves the items in the given dimension.
	 * 
	 * @param iDimensionNumber Dimension number for which Position is to be written (counted from 1).
	 * @param The iPosition Position of the object in the given dimension to which it should be positioned.
	 * @throws ParameterException Throws an exception if the passed dimension number is out of range.
	 */
	public void setDimension(int iDimensionNumber, int iPosition) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		aDimensions[ iDimensionNumber - 1 ] = iPosition;
		}
	/**
	 * A simplified version of the {@link #setDimension (int, int)} method that sets positions for the first dimension.	 * 
	 * @param iPosition Position of the object in the first dimension.	 */
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
	* A simplified version of the {@link #setDimension (int, int)} method that sets positions for the second dimension.
	*
	* @param iPosition Position of the object in the second dimension.
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
	 * A simplified version of the {@link #setDimension (int, int)} method that sets positions for the third dimension.
	 *
	 * @since 1.2
	 * @param iPosition Position of an object in the third dimension.
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
	 * The method moves positions on a given dimension by a given value.
	 *
	 * @param iDimensionNumber Number of the dimension whose Position is to be shifted (counted from 1).
	 * @param iOffsetValue Value of the dimension's offset.
	 * @throws ParameterException Throws an exception if the dimension number is out of range.
	 */
	public void shiftDimension(int iDimensionNumber, int iOffsetValue) throws ParameterException
		{
		if (iDimensionNumber > iNumberOfDimensions || iDimensionNumber <= 0)
			throw new ParameterException("iDimensionNumber = " + iDimensionNumber);
		aDimensions[ iDimensionNumber - 1 ]+= iOffsetValue;
		}
	/**
	 * A simplified version of the {@link #shiftDimension (int, int)} method that allows you to shift the position of the object on the first dimension.
	 *
	 * @param iOffsetValue Value to offset the position on the first dimension.
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
	 * A simplified version of the {@link #shiftDimension (int, int)} method that allows you to shift the position of the object on the second dimension.
 	 *
	 * @param iOffsetValue Value to offset the position on the second dimension.
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
	 * A simplified version of the {@link #shiftDimension (int, int)} method that allows you to shift the position of an object on the third dimension.
 	 *
	 * @since 1.2
	 * @param iOffsetValue Value to offset the position on the third dimension.
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
	 * Obscured version of the Object class method that creates a copy of the object.
	 *
	 * @since 1.2
	 * @return Returns a reference to the created copy of the object.
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
	 * Obscured version of the Object class method that compares two Position objects.
	 *
	 * @param oObj Object to compare.
	 * @return Returns TRUE if both objects are of the same type and have the same number of dimensions
	 * and the same positions in all dimensions, otherwise returns FALSE. 
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
