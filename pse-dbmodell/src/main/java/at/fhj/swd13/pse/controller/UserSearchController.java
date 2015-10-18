package at.fhj.swd13.pse.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.UserDTO;
import at.fhj.swd13.pse.dto.UserDTOBuilder;

@ManagedBean
@SessionScoped
public class UserSearchController {

	private String search = "";

	@Inject
	private UserService userService;

	@Inject
	private UserDTOBuilder userDTOBuilder;

	public List<UserDTO> getResult() {
		List<UserDTO> result = new ArrayList<UserDTO>();
		for (Person person : userService.findUsers(getSearch())) {
			result.add(userDTOBuilder.createFrom(person));
		}
		return result;
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
