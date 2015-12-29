package at.fhj.swd13.pse.db;

public class EntityNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException( final String message ) {
		super( message );
	}
	
	public EntityNotFoundException( final String message, Throwable cause) {
		super( message,cause);
	}
}
