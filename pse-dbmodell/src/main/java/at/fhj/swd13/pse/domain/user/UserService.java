package at.fhj.swd13.pse.domain.user;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Person;

public interface UserService {

	/**
	 * Get a user and log the user in
	 * 
	 * @param username
	 *            username of the user to log in (case sensitive)
	 * @param plainPassword
	 *            plaintext password (case sensitive)
	 * 
	 * @return instance of a person or null if it could not be found or logged in or may not log in
	 */
	Person loginUser(String username, String plainPassword);

	Person getUser(final String username);

	/**
	 * Update all passwords that are null or '--' with 12345678
	 * 
	 * @return the number of passwords changed
	 */
	int updateNullPasswords();

	/**
	 * Set a new instance that checks the strength of the password
	 * 
	 * @param passwordStrengthValidator
	 *            new instance of the passwordStrengthValidator
	 * 
	 * 
	 */
	void setPasswordStrengthValidator(PasswordStrengthValidator passwordStrengthValidator);

	/**
	 * 
	 * Set a new password for the user
	 * 
	 * @param username
	 *            username of for whom to change the password
	 * @param newPlainPassword
	 *            plaintext of the new password
	 * 
	 * @throws WeakPasswordException
	 *             password does not meet strength criteria
	 * @throws EntityNotFoundException
	 *             when the username is not associated with any existing user
	 */
	void setPassword(String username, String newPlainPassword) throws WeakPasswordException, EntityNotFoundException;

}