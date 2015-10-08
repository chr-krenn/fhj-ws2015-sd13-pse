package at.fhj.swd13.pse.db;

public class EntityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException( final String message ) {
		super( message );
	}
}
