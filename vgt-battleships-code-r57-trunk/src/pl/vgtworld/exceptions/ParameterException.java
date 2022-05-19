package pl.vgtworld.exceptions;

/**
 * Throwed exception when parameter passed to method is invalid <br />
 * 
 * <p>
 * updates:<br />
 * 1.0.1<br />
 * - fixes in variable naming<br />
 * - corrections in documentation.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.0.1
 */
public class ParameterException
	extends Exception
	{
	/**
	 * Default constructor.
	 */
	public ParameterException() {super();}
	/**
	 * Overload constructor that allows you to define the content of the error.
	 * 
	 * @param sMessage Message about the error that caused the exception to be thrown.
	 */
	public ParameterException(String sMessage) {super(sMessage);}
	}
