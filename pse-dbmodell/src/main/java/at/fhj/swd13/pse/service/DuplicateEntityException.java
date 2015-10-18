package at.fhj.swd13.pse.service;

public class DuplicateEntityException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEntityException( final String message ) {
		super( message );
	}
}
