package at.fhj.swd13.pse.domain.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;

@Stateless
@Remote(UserService.class)
public class UserServiceFacade implements UserService {
	
	@EJB(beanName="UserServiceImpl")
	private UserService userService;


	@Override
	public Person loginUser(String username, String plainPassword, String sessionId) {
		return userService.loginUser(username, plainPassword, sessionId);
	}

	@Override
	public void logoutUser(String username) {
		userService.logoutUser(username);
	}

	@Override
	public Person getUser(String username) {
		return userService.getUser(username);
	}

	@Override
	public List<Person> getUsers() {
		return userService.getUsers();
	}

	@Override
	public List<Person> getUsersWithDepartment(String department) {
		return userService.getUsersWithDepartment(department);
	}

	@Override
	public List<Person> findUsers(String search) {
		return userService.findUsers(search);
	}

	@Override
	public int updateNullPasswords() {
		return userService.updateNullPasswords();
	}

	@Override
	public void setPasswordStrengthValidator(PasswordStrengthValidator passwordStrengthValidator) {
		userService.setPasswordStrengthValidator(passwordStrengthValidator);
	}

	@Override
	public boolean isMatchingPassword(String username, String plainPassword) {
		return userService.isMatchingPassword(username, plainPassword);
	}

	@Override
	public void setPassword(String username, String newPlainPassword) {
		userService.setPassword(username, newPlainPassword);
	}

	@Override
	public boolean changePassword(String loggedInUsername, String password, String passwordNew) {
		return userService.changePassword(loggedInUsername, password, passwordNew);
	}

	@Override
	public void update(Person person, List<String> tags) {
		userService.update(person, tags);
	}

	@Override
	public void setUserImage(String username, Integer documentId) {
		userService.setUserImage(username, documentId);
	}

	@Override
	public PersonRelation createRelation(Person sourcePerson, Person targetPerson) {
		return userService.createRelation(sourcePerson, targetPerson);
	}

	@Override
	public void removeRelation(Person sourcePerson, Person targetPerson) {
		userService.removeRelation(sourcePerson, targetPerson);
	}

	@Override
	public String resetPassword(String emailAddress, String serverName, int port) {
		return userService.resetPassword(emailAddress, serverName, port);
	}

	@Override
	public String getImageRef(Person p) {
		return userService.getImageRef(p);
	}

	@Override
	public String getFullName(Person p) {
		return userService.getFullName(p);
	}
}
