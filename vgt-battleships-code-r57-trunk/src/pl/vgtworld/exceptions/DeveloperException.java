package pl.vgtworld.exceptions;

/**
 * The RuntimeException type exception is used mainly when applying try-catch 
 * instances inside methods.
 * The occurrence of this error is usually independent of the user of the class
 * and will be the result of an error by the class author in creating the given
 * code block.<br />
 * 
 * <p>
 * updates:<br />
 * 1.1.1<br />
 * - fixes in variable naming<br />
 * - corrections in documentation<br />
 * 1.1<br />
 * - adding two additional constructors to pass an exception-reason for 
 * throwing an exception.<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1.1
 */
public class DeveloperException
	extends RuntimeException
	{
	/**
	 * Default constructor.
	 */
	public DeveloperException() {super();}
	/**
	 * Overload constructor that allows you to define the content of 
         * the error.
	 * 
	 * @param sMessage Message about the error that caused the exception to be thrown.
	 */
	public DeveloperException(String sMessage) {super(sMessage);}
	/**
	 * Overload constructor to define the error content and the cause-exception.
	 * 
	 * @since 1.1
	 * @param sMessage Message about the error that caused the exception to be thrown.
	 * @param oCause The exception that caused the error.
	 */
	public DeveloperException(String sMessage, Throwable oCause) {super(sMessage, oCause);}
	/**
	 * Overload constructor to define exception-cause.
	 * 
	 * @since 1.1
	 * @param oCause The exception that caused the error.
	 */
	public DeveloperException(Throwable oCause) {super(oCause);}
	}
