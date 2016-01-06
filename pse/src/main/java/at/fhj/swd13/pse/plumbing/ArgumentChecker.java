package at.fhj.swd13.pse.plumbing;


public abstract class ArgumentChecker {

	private ArgumentChecker() {
		
	}
	
	
	/** Asser that the given string argument is not null and not empty (or only blanks)
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
}
