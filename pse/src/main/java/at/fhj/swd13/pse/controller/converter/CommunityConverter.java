package at.fhj.swd13.pse.controller.converter;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.domain.chat.ChatService;

@FacesConverter("communityConverter")
public class CommunityConverter implements Converter {

	@Inject
	private Logger logger;

	@Inject
	private ChatService chatService;

	@PostConstruct
	public void postConstruct() {
		logger.info("[MSG+] converter instantiated");
	}

	@Override
	@SuppressWarnings("squid:S1166")
	public Object getAsObject(FacesContext fc, UIComponent uic, String objectId) {

		logger.info("[MSG+] converter.getAsObject for " + objectId);

		if (objectId != null) {
			final int communityId = Integer.parseInt(objectId);

			try {
				return chatService.getCommunity(communityId);
			} catch (Exception e) {
				logger.error("[MSG+] failed to add community: " +e.getMessage());
			}
		}
		
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object item) {
		logger.info("[MSG+] converter.getAsObject");

		logger.info("[MSG+] converter.getAsString");

		if (item != null) {

			return ((Community)item).getToken();

		} else {

			return null;
		}
	}
}
