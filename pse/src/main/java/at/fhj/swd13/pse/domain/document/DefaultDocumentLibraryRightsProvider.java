package at.fhj.swd13.pse.domain.document;

public class DefaultDocumentLibraryRightsProvider implements DocumentLibraryRightsProvider {

	public DefaultDocumentLibraryRightsProvider(int communityId) {
		
	}

	@Override
	public boolean canViewLibrary() {
		return true;
	}

	@Override
	public boolean canEditLibrary() {
		return true;
	}
}
