package at.fhj.swd13.pse.service;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;

/**
 * User Service, object that provides all higher level logic for managing users
 * 
 */
public class UserService extends ServiceBase {

	/**
	 * Create an instance of the user service
	 */
	public UserService() {
		super();
	}
	
	
	/**
	 * Get a user and log the user in
	 * 
	 * @param username username of the user to log in (case sensitive)
	 * @param plainPassword plaintext password (case sensitive)
	 * 
	 * @return instance of a person or null if it could not be found or logged in or may not log in 
	 */
	public Person loginUser( final String username, final String plainPassword, DbContext dbContext ) {
		
		Person p = dbContext.getPersonDAO().getByUsername(username);
		
		
		if ( p != null 
				&& p.isLoginAllowed()
				&& p.isMatchingPassword(plainPassword)) {
			return p;
		}
		
		return null;
	}
	
	public int updateNullPasswords( ) throws Exception {
		try ( DbContext context = contextProvider.getDbContext() ) {
			
			int userCount = updateNullPasswords( context );
			
			context.commit();
			
			return userCount;
		}
	}
	
	public int updateNullPasswords( DbContext dbContext ) {
		
		int userCount = 0;
		
		for( Person person : dbContext.getPersonDAO().getAllWithNullPasswords() ) {

			person.setPassword( "12345678" );
			++userCount;
		}		
		
		return userCount;
	}	
}
