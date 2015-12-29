package at.fhj.swd13.pse.domain.document;

import javax.inject.Inject;

import at.fhj.swd13.pse.plumbing.UserSession;

public class DocumentLibraryRightsProviderFactory {

	@Inject
	private UserSession userSession;
	
	public DocumentLibraryRightsProvider create(int communityId)
	{
		if(communityId == 1)
			return new GlobalDocumentLibraryRightsProvider(userSession);
		else
			return new DefaultDocumentLibraryRightsProvider(communityId);
	}	
}
