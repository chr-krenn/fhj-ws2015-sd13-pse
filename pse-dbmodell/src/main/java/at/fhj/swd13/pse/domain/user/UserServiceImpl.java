package at.fhj.swd13.pse.domain.user;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;
import at.fhj.swd13.pse.dto.UserDTO;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * User Service, object that provides all higher level logic for managing users
 * 
 */
@Stateless
public class UserServiceImpl extends ServiceBase implements UserService {

	@Inject
	private PasswordStrengthValidator passwordStrengthValidator;

	@Inject
	private UserSession userSession;


	/**
	 * Create an instance of the user service
	 * 
	 * @param dbContext
	 *            the connection to the persistent storage with which to work
	 */
	public UserServiceImpl() {
		super();
	}

	public UserServiceImpl(DbContext dbContext, UserSession userSession) {
		super(dbContext);
		this.userSession = userSession;
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
			p.setIsOnline(true);
			p.setCurrentSessionId(userSession.login(username));
			userSession.setAdmin(p.isAdmin());
			return p;
		}

		return null;
	}

	@Override
	public void logoutCurrentUser() {

		if (userSession.isLoggedIn()) {
			Person p = dbContext.getPersonDAO().getByUsername(userSession.getUsername());
			p.setIsOnline(false);
			p.setCurrentSessionId(null);

			userSession.logout();
		}
	}
	
	
	
	@Override
	public Person getLoggedInUser() {
		if(userSession.isLoggedIn()) {
			Person p = dbContext.getPersonDAO().getByUsername(userSession.getUsername());
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
	 * @see at.fhj.swd13.pse.domain.user.UserService#isMatchingPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isMatchingPassword(final String username, final String plainPassword) {
		Person p = dbContext.getPersonDAO().getByUsername(username);

		return p != null && p.isMatchingPassword(plainPassword);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#getUser(java.lang.String)
	 */
	@Override
	public Person getUser(final String username) throws EntityNotFoundException {
		return dbContext.getPersonDAO().getByUsername(username, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#getUsers()
	 */
	@Override
	public List<Person> getUsers() {

		// TODO use better method
		return dbContext.getPersonDAO().getAllPersons(0, 1000);

	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.user.UserService#findUsers(java.lang.String)
	 */
	@Override
	public List<Person> findUsers(String search) {
		return dbContext.getPersonDAO().getPersonLike(search);
	}
	
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.user.UserService#update(at.fhj.swd13.pse.dto.UserDTO)
	 */
	@Override
	public void update(final UserDTO userDTO) throws EntityNotFoundException {

		Person p = dbContext.getPersonDAO().getByUsername(userDTO.getUserName(), true);

		p.setLastName(userDTO.getLastName());
		p.setFirstName(userDTO.getFirstName());

		p.setEmailAddress(userDTO.getEmailAddress());
		p.setPhoneNumberMobile(userDTO.getPhoneNumberMobile());

		p.setDateOfBirth(userDTO.getDateOfBirth());
		p.setDateOfEntry(userDTO.getDateOfEntry());

		p.setDepartment(userDTO.getDepartment());
		p.setJobPosition(userDTO.getJob());
		p.setLocationBuilding(userDTO.getLocationBuilding());
		p.setLocationFloor(userDTO.getLocationFloor());
		p.setLocationRoomNumber(userDTO.getLocationRoomNumber());
		p.setIsActive(userDTO.getActive());
		p.setIsLoginAllowed(userDTO.getLoginAllowed());
	}

	@Override
	public void setUserImage(final String username, final Integer documentId) throws EntityNotFoundException {

		Person p = dbContext.getPersonDAO().getByUsername(username, true);

		if (documentId == 0) {
			p.setDocument(null);
		} else {
			Document userImage = dbContext.getDocumentDAO().getById(documentId);

			if (userImage != null) {
				p.setDocument(userImage);
			} else {
				throw new EntityNotFoundException("Document not found : " + documentId);
			}
		}
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.user.UserService#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changePassword(String loggedInUsername, String passwordOldPlain, String passwordNewPlain) {
		Person p;
		try {
			p = dbContext.getPersonDAO().getByUsername(loggedInUsername, true);
		} catch (EntityNotFoundException e) {
			return false;
		}

		if(!p.isMatchingPassword(passwordOldPlain))
			return false;
			
		try {
			passwordStrengthValidator.validate(passwordNewPlain);
		} catch (WeakPasswordException e) {
			return false;
		}
		
		p.setPassword(passwordNewPlain);
		
		return true;
	}
	
	
	@Override
	public PersonRelation createRelation(Person sourcePerson, Person targetPerson) {
		PersonRelation relation;
		
		relation = dbContext.getPersonDAO().createRelation(sourcePerson, targetPerson);
		return relation;
	}

	@Override
	public void removeRelation(Person sourcePerson, Person targetPerson) {
		dbContext.getPersonDAO().removeRelation(sourcePerson, targetPerson);
		
	}
	
}
