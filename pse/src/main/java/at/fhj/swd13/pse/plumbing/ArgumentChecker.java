package at.fhj.swd13.pse.plumbing;


public abstract class ArgumentChecker {

	private ArgumentChecker() {
		
	}
	
	
	/** Assert that the given string argument is not null and not empty (or only blanks)
	 * 
 	 * @param argument the string to check
	 * @param argumentName the name of the string argument shown in the exception
	 * @throws IllegalArgumentException when argument is null, empty or only whitespaces
	 */
	public static void assertContent( final String argument, final String argumentName ) {
		
		if( argument == null || argument.trim().equals("")) {
		
			throw new IllegalArgumentException( "Argument empty: " + argumentName );			
		}
	}
	
	/** Assert that the given string argument is not null
	 * 
 	 * @param argument the object to check
	 * @param argumentName the name of the string argument shown in the exception
	 * @throws IllegalArgumentException when argument is null
	 */
	public static void assertNotNull( final Object argument, final String argumentName ) {

		if ( argument == null ) {
			
			throw new IllegalArgumentException( "Argument null: " + argumentName );						
		}
	}
}
