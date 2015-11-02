package at.fhj.swd13.pse.domain.document;

public class DocumentNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public DocumentNotFoundException( final String message ) {
		super( message );
	}
	
	public DocumentNotFoundException( final String message, Throwable cause) {
		super( message,cause);
	}
	
	

}
