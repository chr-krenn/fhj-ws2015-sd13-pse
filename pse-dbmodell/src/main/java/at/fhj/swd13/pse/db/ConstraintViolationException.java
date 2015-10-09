package at.fhj.swd13.pse.db;

/**
 * A constraint is not fullfilled in the persistent storage.
 * This could either be a non-existing foreign key
 * , a duplicate value in a unique column ... 
 */
public class ConstraintViolationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create an instance
	 * 
	 * @param message explanation for the exception
	 * @param reason root cause of the exception (from persistent storage modul)
	 */
	public ConstraintViolationException( final String message, final Throwable reason ) {
		
		super(message, reason);
	
	}
}
