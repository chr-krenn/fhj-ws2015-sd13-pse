package at.fhj.swd13.pse.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;
import at.fhj.swd13.pse.db.entity.PersonTag;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.document.DocumentService;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.plumbing.MailService;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * User Service, object that provides all higher level logic for managing users
 * 
 */
@Stateless
@Local(UserService.class)
public class UserServiceImpl extends ServiceBase implements UserService {

	@Inject
	private PasswordStrengthValidator passwordStrengthValidator;

	@Inject
	private TagService tagService;

	@Inject
	private Logger logger;

	@Inject
	private PasswordCreator passwordCreator;

	@Inject
	private MailService mailService;
	
	@Inject
	private DocumentService documentService;

	/**
	 * Create an instance of the user service
	 * 
	 * @param dbContext
	 *            the connection to the persistent storage with which to work
	 */
	public UserServiceImpl() {
		super();
	}

	public UserServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#loginUser(java.lang.String, java.lang.String)
	 */
	@Override
	public Person loginUser(final String username, final String plainPassword, String sessionId) {
		try {
			Person p = dbContext.getPersonDAO().getByUsername(username);
	
			if (p != null && p.getIsLoginAllowed() && p.getIsActive() && p.isMatchingPassword(plainPassword)) {
				p.setIsOnline(true);
				p.setCurrentSessionId(sessionId);
				return p;
			}
		} catch (Throwable ex) {
			throw new ServiceException(ex);
		}
		return null;
	}

	@Override
	public void logoutUser(String username) {
		try {
			Person p = dbContext.getPersonDAO().getByUsername(username);
			p.setIsOnline(false);
			p.setCurrentSessionId(null);
		} catch (Throwable ex) {
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#updateNullPasswords()
	 */
	@Override
	public int updateNullPasswords() {
		try {
			int userCount = 0;
			for (Person person : dbContext.getPersonDAO().getAllWithNullPasswords()) {
				person.setPassword("12345678");
				++userCount;
			}
			return userCount;
		} catch (Throwable ex) {
			throw new ServiceException(ex);
		}
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
		try {
			Person p = dbContext.getPersonDAO().getByUsername(username);
			return p != null && p.isMatchingPassword(plainPassword);
		} catch (Throwable ex) {
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#setPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPassword(final String username, final String newPlainPassword) {
		try {
			Person p = dbContext.getPersonDAO().getByUsername(username, true);
			passwordStrengthValidator.validate(newPlainPassword);
			p.setPassword(newPlainPassword);
		} catch (Throwable ex) {
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#getUser(java.lang.String)
	 */
	@Override
	public Person getUser(final String username) {
		try {
			return dbContext.getPersonDAO().getByUsername(username, true);
		} catch (Throwable ex) {
			logger.info("[UserService] getUser failed for " + username + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#getUsers()
	 */
	@Override
	public List<Person> getUsers() {
		try {
			// TODO use better method
			return dbContext.getPersonDAO().getAllPersons(0, 1000);
		} catch (Throwable ex) {
			logger.info("[UserService] getUsers failed : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#getUsersWithDepartment()
	 */
	@Override
	public List<Person> getUsersWithDepartment(String department) {
		try {
			return dbContext.getPersonDAO().getAllPersonsWithDepartment(department);
		} catch (Throwable ex) {
			logger.info("[UserService] getUsersWithDepartment failed for " + department + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#findUsers(java.lang.String)
	 */
	@Override
	public List<Person> findUsers(String search) {
		try {
			return dbContext.getPersonDAO().getPersonLike(search);
		} catch (Throwable ex) {
			logger.info("[UserService] findUsers failed for " + search + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#update
	 */
	@Override
	public void update(final Person person,final List<String> tags) {
		try {
			Person p = dbContext.getPersonDAO().getByUsername(person.getUserName(), true);

			p.setLastName(person.getLastName());
			p.setFirstName(person.getFirstName());

			p.setEmailAddress(person.getEmailAddress());
			p.setPhoneNumberMobile(person.getPhoneNumberMobile());

			p.setDateOfBirth(person.getDateOfBirth());
			p.setDateOfEntry(person.getDateOfEntry());

			p.setDepartment(person.getDepartment());
			p.setJobPosition(person.getJobPosition());
			p.setLocationBuilding(person.getLocationBuilding());
			p.setLocationFloor(person.getLocationFloor());
			p.setLocationRoomNumber(person.getLocationRoomNumber());
			p.setIsActive(person.getIsActive());
			p.setIsExtern(person.getIsExtern());
			p.setIsLoginAllowed(person.getIsLoginAllowed());

			// remove deleted tags
			List<PersonTag> deletedTags = new ArrayList<PersonTag>();
			if (tags != null) {
				for (PersonTag personTag : p.getPersonTags()) {
					if (!tags.contains(personTag.getTag().getToken())) {
						deletedTags.add(personTag);
					}
				}

				// add new tags
				for (String token : tags) {
					boolean bExists = false;
					for (PersonTag personTag : p.getPersonTags()) {
						if (personTag.getTag().getToken().equals(token)) {
							bExists = true;
							break;
						}
					}

					if (!bExists) {
						Tag tag = tagService.getTagByToken(token);
						PersonTag personTag = new PersonTag();
						personTag.setTag(tag);
						p.addPersonTag(personTag);
					}
				}
			} else {
				for (PersonTag personTag : p.getPersonTags()) {
					deletedTags.add(personTag);
				}
			}

			for (PersonTag personTag : deletedTags) {
				p.removePersonTag(personTag);
			}
		} catch (Throwable ex) {
			logger.info("[UserService] update failed for user " + person.getFullName() + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public void setUserImage(final String username, final Integer documentId) {
		Document userImage = null;
		try {
			Person p = dbContext.getPersonDAO().getByUsername(username, true);

			if (documentId == 0) {
				p.setDocument(null);
			} else {
				userImage = dbContext.getDocumentDAO().getById(documentId);

				if (userImage != null)
					p.setDocument(userImage);
			}
		} catch (Throwable ex) {
			logger.info("[UserService] setUserImage failed for user " + username + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}

		if (userImage == null) {
			logger.info("[UserService] setUserImage failed for user " + username + " : Document not found : " + documentId);
			throw new ServiceException("Document not found : " + documentId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.domain.user.UserService#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changePassword(String loggedInUsername, String passwordOldPlain, String passwordNewPlain) {
		try {
			Person p = dbContext.getPersonDAO().getByUsername(loggedInUsername, true);
	
			if (!p.isMatchingPassword(passwordOldPlain))
				return false;
	
			passwordStrengthValidator.validate(passwordNewPlain);
			p.setPassword(passwordNewPlain);
			return true;
		} catch (Throwable ex) {
			logger.info("[UserService] changePassword failed for user " + loggedInUsername + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public PersonRelation createRelation(Person sourcePerson, Person targetPerson) {
		try {
			return dbContext.getPersonDAO().createRelation(sourcePerson, targetPerson);
		} catch (Throwable ex) {
			logger.info("[UserService] createRelation failed for users " + sourcePerson.getFullName() + " and " +  targetPerson.getFullName() + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public void removeRelation(Person sourcePerson, Person targetPerson) {
		try {
			dbContext.getPersonDAO().removeRelation(sourcePerson, targetPerson);
		} catch (Throwable ex) {
			logger.info("[UserService] removeRelation failed for users " + sourcePerson.getFullName() + " and " +  targetPerson.getFullName() + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public String resetPassword(final String emailAddress, String serverName, int port) {
		String randomPassword = passwordCreator.createRandomPassword();

		try {
			Person person = dbContext.getPersonDAO().getByEmailAddress(emailAddress);

			logger.info("[USER] found person, now sending email...");

			person.setPassword(randomPassword);

			// emailController.sendNewPassword(emailAddress, randomPassword);
			mailService.sendMail("Ihr neues Passwort",
					"Das ist ihr neues Passwort: <em>" + randomPassword + "</em><br/><div>Viel Spass mit <a href=\"" + serverName + ":"+port+"/pse\">pse</a>.</div>", emailAddress);
			logger.info("[USER] email sent");
		} catch (Throwable ex) {
			logger.info("[UserService] resetPassword failed for user " + emailAddress + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
		return randomPassword;
	}
	
	@Override
	public String getImageRef(Person p)
	{
		if ( p.getDocument() != null ) 
			return documentService.buildServiceUrl( p.getDocument().getDocumentId() ) ;
		else {
			return documentService.getDefaultDocumentRef( DocumentService.DocumentCategory.USER_IMAGE ) ;
		} 

	}
	
	@Override
	public String getFullName(Person p)
	{
		return p.getFirstName() + " " + p.getLastName();
	}
}
