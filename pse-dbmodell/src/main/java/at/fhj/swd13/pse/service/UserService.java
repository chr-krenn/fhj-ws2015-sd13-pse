package at.fhj.swd13.pse.service;

import javax.inject.Inject;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.PasswordStrengthValidator;
import at.fhj.swd13.pse.domain.user.WeakPasswordException;

/**
 * User Service, object that provides all higher level logic for managing users
 * 
 */
public class UserService extends ServiceBase {

	@Inject
	private PasswordStrengthValidator passwordStrengthValidator;
	
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
	
	/**
	 * Set a new instance that checks the strength of the password
	 * 
	 * @param passwordStrengthValidator new instance of the passwordStrengthValidator
	 * 
	 * 
	 */
	public void setPasswordStrengthValidator( PasswordStrengthValidator passwordStrengthValidator ) {
		
		this.passwordStrengthValidator = passwordStrengthValidator;
	}
	
	/**
	 * 
	 * Set a new password for the user
	 * 
	 * @param username username of for whom to change the password
	 * @param newPlainPassword plaintext of the new password
	 * @param dbContext connection to the persistent storage
	 * 
	 * @throws WeakPasswordException password does not meet strength criteria
	 * @throws EntityNotFoundException when the username is not associated with any existing user
	 */
	public void setPassword( final String username, final String newPlainPassword, DbContext dbContext ) throws WeakPasswordException, EntityNotFoundException {
	
		Person p = dbContext.getPersonDAO().getByUsername(username, true);
		
		passwordStrengthValidator.validate( newPlainPassword );

		p.setPassword(newPlainPassword);
	}
}
