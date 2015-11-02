package at.fhj.swd13.pse.domain.document;

public class DefaultDocumentLibraryRightsProvider implements DocumentLibraryRightsProvider {

	public DefaultDocumentLibraryRightsProvider(int communityId) {
		
	}

	@Override
	public boolean canViewLibrary() {
		//TODO Check if the logged in person is part of this community
		return true;
	}

	@Override
	public boolean canEditLibrary() {
		// TODO Auto-generated method stub
		return true;
	}
}
