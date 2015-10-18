package at.fhj.swd13.pse.service;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@ManagedBean
@SessionScoped
public class SessionBean {

	@Inject
	private Logger logger;
	
	public String getUsername() {
		return "username";
	}
	
	@PostConstruct
	public void init() {
	
		logger.info("[SESSION] session service started");		
	}	
}
