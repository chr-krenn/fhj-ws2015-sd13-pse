package at.fhj.swd13.pse.domain.user;

/**
 * 
 * The e-mail-address given is not assigned to any person 
 *
 */
public class InvalidEmailAddressException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a InvalidEmailAddressException
	 * 
	 * @param message a message to the user 
	 */
	public InvalidEmailAddressException(String message) {
		super(message);
	}
	
	public InvalidEmailAddressException(){
		
	}
	
}
