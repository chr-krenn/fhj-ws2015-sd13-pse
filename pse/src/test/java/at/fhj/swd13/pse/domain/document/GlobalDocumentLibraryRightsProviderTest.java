package at.fhj.swd13.pse.domain.document;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.fhj.swd13.pse.plumbing.UserSession;

public class GlobalDocumentLibraryRightsProviderTest {

	@Test
	public void testCanViewLibrary() {
//		userSession != null && userSession.isLoggedIn();
		
		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(true);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertTrue(rightsProvider.canViewLibrary());
		
	}
	
	@Test
	public void testCanViewLibrary2() {

		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(null);
		
		assertFalse(rightsProvider.canViewLibrary());
		
	}
	
	@Test
	public void testCanViewLibrary3() {

		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(false);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertFalse(rightsProvider.canViewLibrary());
		
	}
	
	@Test
	public void testCanEditLibrary() {
		
		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(true);
		expect(mock.isAdmin()).andReturn(true);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertTrue(rightsProvider.canEditLibrary());
		
	}
	
	@Test
	public void testCanEditLibrary2() {

		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(null);
		
		assertFalse(rightsProvider.canEditLibrary());
		
	}
	
	@Test
	public void testCanEditLibrary3() {
		
		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(false);
		expect(mock.isAdmin()).andReturn(true);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertFalse(rightsProvider.canEditLibrary());
		
	}
	
	@Test
	public void testCanEditLibrary4() {
		
		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(true);
		expect(mock.isAdmin()).andReturn(false);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertFalse(rightsProvider.canEditLibrary());
		
	}
	
	@Test
	public void testCanEditLibrary5() {
		
		// Configure mock object
		UserSession mock = createMock(UserSession.class);
		expect(mock.isLoggedIn()).andReturn(false);
		expect(mock.isAdmin()).andReturn(false);
		replay(mock);
		
		GlobalDocumentLibraryRightsProvider rightsProvider = new GlobalDocumentLibraryRightsProvider(mock);
		
		assertFalse(rightsProvider.canEditLibrary());
		
	}
}
