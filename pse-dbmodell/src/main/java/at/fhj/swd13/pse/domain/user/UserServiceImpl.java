package at.fhj.swd13.pse.domain.user;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * User Service, object that provides all higher level logic for managing users
 * 
 */
@RequestScoped
public class UserServiceImpl extends ServiceBase implements UserService {

	@Inject
	private PasswordStrengthValidator passwordStrengthValidator;

	/**
	 * Create an instance of the user service
	 * 
	 * @param dbContext
	 *            the connection to the persistent storage with which to work
	 */
	public UserServiceImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#loginUser(java.lang.String, java.lang.String)
	 */
	@Override
	public Person loginUser(final String username, final String plainPassword) {

		Person p = dbContext.getPersonDAO().getByUsername(username);

		if (p != null && p.isLoginAllowed() && p.isMatchingPassword(plainPassword)) {
			return p;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#updateNullPasswords()
	 */
	@Override
	public int updateNullPasswords() {

		int userCount = 0;

		for (Person person : dbContext.getPersonDAO().getAllWithNullPasswords()) {

			person.setPassword("12345678");
			++userCount;
		}

		return userCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#setPasswordStrengthValidator(at.fhj.swd13.pse.domain.user.PasswordStrengthValidator)
	 */
	@Override
	public void setPasswordStrengthValidator(PasswordStrengthValidator passwordStrengthValidator) {

		this.passwordStrengthValidator = passwordStrengthValidator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#setPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPassword(final String username, final String newPlainPassword) throws WeakPasswordException, EntityNotFoundException {

		Person p = dbContext.getPersonDAO().getByUsername(username, true);

		passwordStrengthValidator.validate(newPlainPassword);

		p.setPassword(newPlainPassword);
	}
}
