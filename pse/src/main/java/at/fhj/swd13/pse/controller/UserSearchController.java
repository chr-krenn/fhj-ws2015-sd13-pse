package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;

@ManagedBean
@SessionScoped
public class UserSearchController {

	private String search = "";

	@Inject
	private UserService userService;

	public List<Person> getResult() {
		return userService.findUsers(getSearch());
	}

	public String doSearch() {
		return "/protected/UserSearchResult.jsf?faces-redirect=true";
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
