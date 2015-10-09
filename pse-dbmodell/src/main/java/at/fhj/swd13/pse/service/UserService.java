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
