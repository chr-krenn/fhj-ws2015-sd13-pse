package at.fhj.swd13.pse.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.dto.UserDTO;
import at.fhj.swd13.pse.dto.UserDTOBuilder;

@ManagedBean
@ViewScoped
public class UserView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userService;

	@Inject
	private UserDTOBuilder userDTOBuilder;

	public UserDTO getUserDTO() {
		String userName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("userName");
		return userDTOBuilder.createFrom(userService.getUser(userName));
	}
}
