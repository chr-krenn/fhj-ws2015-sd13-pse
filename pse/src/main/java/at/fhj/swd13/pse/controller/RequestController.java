package at.fhj.swd13.pse.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.OrderBy;

import org.jboss.logging.Logger;
import org.primefaces.event.SelectEvent;

import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.plumbing.MailService;
import at.fhj.swd13.pse.plumbing.UserSession;

/**
 * 
 * @author manuelkirsch
 *
 */
@Model
public class RequestController extends ControllerBase {

	@Produces
	@Named
	@OrderBy("createdAt ASC")
	private List<Community> requests;

	@Produces
	@Named
	@OrderBy("createdAt ASC")
	private List<CommunityMember> memberrequests;

	@Inject
	private ChatService chatService;

	@Inject
	private MailService mailService;

	@Inject
	private Logger logger;

	@Inject
	private UserSession userSession;

	@Inject
	private UserService userService;

	private transient Community selectedRequest = null;

	private transient CommunityMember selectedMemberRequest = null;

	private int communityId;
	private String communityIdString;
	private boolean isMember;
	private int communityMemberId;
	private String communityMemberIdString;

	@PostConstruct
	@SuppressWarnings("squid:S1166")
	public void postConstruct() {
		try {
			updateUnconfirmedCommunities();
			updateMemberRequests();
		} catch (ServiceException e) {
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}

		communityIdString = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get("communityId");
		communityMemberIdString = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get("communityMemberId");
	}

	public List<Community> getRequests() {
		return requests;
	}

	public List<CommunityMember> getMemberrequests() {
		return memberrequests;
	}

	public Community getSelectedRequest() {
		return selectedRequest;
	}

	public CommunityMember getSelectedMemberRequest() {
		return selectedMemberRequest;
	}

	public void setSelectedRequest(Community selectedRequest) {
		this.selectedRequest = selectedRequest;
	}

	public void setSelectedMemberRequest(CommunityMember selectedMemberRequest) {
		this.selectedMemberRequest = selectedMemberRequest;
	}

	@SuppressWarnings({"squid:S1166", "squid:S1172"})
	public void onCommunitySelected(SelectEvent object) {
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"Community.jsf?id="
									+ selectedRequest.getCommunityId());
		} catch (IOException e) {
			logger.error("Error on redirecting to Community.jsf?id="
					+ selectedRequest.getCommunityId() + ": " + e.getMessage());
			
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
	}

	@SuppressWarnings("squid:S1166")
	public void onMemberRequestSelected(SelectEvent object) {

		onCommunitySelected(object);
	}

	@SuppressWarnings("squid:S1166")
	public void approveCommunity() {

		Community com = null;
		Person currentUser = null;
		communityId = Integer.parseInt(communityIdString);

		try {
			com = chatService.getCommunity(communityId);
			logger.info("  community: " + com.getCommunityId() + " - "
					+ com.getName());

			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - "
					+ currentUser.getFirstName() + " "
					+ currentUser.getLastName());

			chatService.confirmCommunity(currentUser, com);
			logger.info(" Community Confirmed: " + com.getCommunityId()
					+ " by: " + currentUser.getPersonId());

		} catch (ServiceException e) {
			logger.error("Error while approving community");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}

		updateUnconfirmedCommunities();

		try {
			mailService.sendMail("Community Antrag " + com.getName()
					+ " Freigegeben", "Die Community " + com.getName()
					+ " ist jetzt freigegeben!", com.getCreatedBy()
					.getEmailAddress(), null);
			logger.info("Email sent");

		} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}

		info("Community Antrag angenommen!");
	}
	@SuppressWarnings("squid:S1166")
	public void declineCommunity() {

		Community com = null;
		Person currentUser = null;
		communityId = Integer.parseInt(communityIdString);

		try {
			com = chatService.getCommunity(communityId);
			logger.info("  community: " + com.getCommunityId() + " - "
					+ com.getName());

			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - "
					+ currentUser.getFirstName() + " "
					+ currentUser.getLastName());

			chatService.declineCommunity(currentUser, com);
			logger.info(" Community Declined: " + com.getCommunityId()
					+ " by: " + currentUser.getPersonId());

		} catch (ServiceException e) {
			logger.error("Error while declining community");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}

		updateUnconfirmedCommunities();

		try {
			mailService.sendMail("Community Antrag " + com.getName()
					+ " Abgelehnt", "Die Community " + com.getName()
					+ " wurde nicht freigegeben!", com.getCreatedBy()
					.getEmailAddress(), null);
			logger.info("Email sent");

		} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}
		info("Community Antrag abgelehnt!");
	}

	@SuppressWarnings("squid:S1166")
	public void approveMemberRequest() {

		CommunityMember com = null;
		Person currentUser = null;
		communityMemberId = Integer.parseInt(communityMemberIdString);

		try {
			com = chatService.getUnconfirmedCommunityMember(communityMemberId);
			logger.info("  communitymember: " + com.getCommunityMemberId()
					+ " - member: " + com.getMember().getPersonId());

			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - "
					+ currentUser.getFirstName() + " "
					+ currentUser.getLastName());

			chatService.confirmCommunityMember(currentUser, com);
			logger.info(" CommunityMember Confirmed: "
					+ com.getCommunityMemberId() + " by: "
					+ currentUser.getPersonId());

		} catch (ServiceException e) {
			logger.error("Error while approving member request");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}

		updateMemberRequests();

		try {
			mailService.sendMail("Community Mitgliedschafts Antrag "
					+ com.getCommunity().getName() + " "
					+ com.getMember().getUserName() + " Freigegeben",
					"Der Community Mitgliedschafts Antrag "
							+ com.getCommunity().getName() + " "
							+ com.getMember().getUserName()
							+ " ist jetzt freigegeben!", com.getMember()
							.getEmailAddress(), null);
			logger.info("Email sent");

		} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}

		info("Community Mitgliedschafts Antrag angenommen!");
	}
	@SuppressWarnings("squid:S1166")
	public void declineMemberRequest() {

		CommunityMember com = null;
		Person currentUser = null;
		communityMemberId = Integer.parseInt(communityMemberIdString);

		try {
			com = chatService.getUnconfirmedCommunityMember(communityMemberId);
			logger.info("  communitymember: " + com.getCommunityMemberId()
					+ " - " + com.getMember().getPersonId());

			currentUser = userService.getUser(userSession.getUsername());
			logger.info("  currentUser: " + currentUser.getPersonId() + " - "
					+ currentUser.getFirstName() + " "
					+ currentUser.getLastName());

			chatService.declineCommunityMember(currentUser, com);
			logger.info(" CommunityMember Declined: "
					+ com.getCommunityMemberId() + " by: "
					+ currentUser.getPersonId());

		} catch (ServiceException e) {
			logger.error("Error while declining member Request: "
					+ e.getMessage());

			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
		updateMemberRequests();

		try {
			mailService.sendMail("Community Mitgliedschafts Antrag "
					+ com.getCommunity().getName() + " "
					+ com.getMember().getUserName() + " Abgelehnt",
					"Der Community Mitgliedschafts Antrag "
							+ com.getCommunity().getName() + " "
							+ com.getMember().getUserName()
							+ " wurde nicht freigegeben!", com.getMember()
							.getEmailAddress(), null);
			logger.info("Email sent");

		} catch (MessagingException e) {
			logger.error("ERROR: " + e.getMessage());
		}
		info("Community Mitgliedschafts Antrag abgelehnt!");
	}
	
	@SuppressWarnings("squid:S1166")
	private void updateMemberRequests() {
		try {
			memberrequests = chatService.getAllUnconfirmedCommunityMembers();
		} catch (ServiceException e) {
			logger.info("[Request-Controller] Error while updating member requests");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
	}
	
	@SuppressWarnings("squid:S1166")
	private void updateUnconfirmedCommunities() {
		try {
			requests = chatService.getUnconfirmedCommunities();
		} catch (ServiceException e){
			logger.info("[Request-Controller] Error while updating unconfirmed communities");
			addFacesMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Fehler", getStringResource("UnknownErrorMessage")));
		}
		
	}

	public void error() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!",
						"You are not a member of this community!"));
	}

	public void info(String text) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", text));
	}

	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}
}
