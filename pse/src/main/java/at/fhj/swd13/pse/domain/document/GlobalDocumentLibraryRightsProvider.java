package at.fhj.swd13.pse.domain.document;

import at.fhj.swd13.pse.plumbing.UserSession;

public class GlobalDocumentLibraryRightsProvider implements DocumentLibraryRightsProvider {

	private UserSession userSession;
	
	public GlobalDocumentLibraryRightsProvider(UserSession userSession) {
		this.userSession = userSession;
		
	}
	
	@Override
	public boolean canViewLibrary() {
		return userSession != null && userSession.isLoggedIn();
	}

	@Override
	public boolean canEditLibrary() {
		return userSession != null && userSession.isLoggedIn() && userSession.isAdmin();
	}
}
