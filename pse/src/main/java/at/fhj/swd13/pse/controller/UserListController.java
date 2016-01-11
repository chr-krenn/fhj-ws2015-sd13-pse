package at.fhj.swd13.pse.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@ViewScoped
public class UserListController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userService;

	public List<Person> getUsers() {
		return userService.getUsers();
	}
	
	public String getImageRef (Person p)
	{
		return userService.getImageRef(p);
	}
	
	public String getFullName(Person p)
	{
		return userService.getFullName(p);
	}
}
