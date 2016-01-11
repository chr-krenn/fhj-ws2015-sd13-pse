package at.fhj.swd13.pse.controller;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Manuel Zametter
 *
 */
public abstract class ControllerBase {
	
	protected void addFacesMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	protected String getStringResource(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "Resources");

		return bundle.getString(key);
	}
}
