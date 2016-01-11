package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.document.DocumentLibraryEntry;
import at.fhj.swd13.pse.domain.document.DocumentLibraryService;
import at.fhj.swd13.pse.domain.document.DocumentLibraryServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class DocumentLibraryServiceIT extends RemoteTestBase {

	private static DocumentLibraryService documentLibraryService;
	private static UserService userService;
	private List<DocumentLibraryEntry> entries;
	
	
	@BeforeClass
	public static void setupServices() throws NamingException {
		prepareDatabase();
		documentLibraryService = lookup(DocumentLibraryServiceFacade.class, DocumentLibraryService.class);
		userService = lookup(UserServiceFacade.class, UserService.class);
	}
	
	@Before
	public void setup() throws NamingException {
		prepareDatabase();
	}

	private void loginAsAdmin()
	{
		Person admin = userService.loginUser("padmin", "12345678", UserSession.createSessionId());
		
		assertNotNull(admin);	
	}
	
	/* 	PSE2015-63 Einträge laden */
	
	@Test
	public void getDocumentLibraryEntriesAsAdmin()
	{
		loginAsAdmin();
		
		entries = documentLibraryService.getEntriesForCommunity(1);
		
		assertTrue(entries.isEmpty());
	}
	
	/* 	PSE2015-63 Eintrag hinzufügen*/
	
	@Test
	public void addEntryToMainDocumentLibrary()
	{
		loginAsAdmin();
		
		byte[] binaryData = new byte[]{1,2,3,4,5,6,7,8};
		
		documentLibraryService.addEntry("test.txt", "test", binaryData, 1);
		
		List<DocumentLibraryEntry> entriesForCommunity = documentLibraryService.getEntriesForCommunity(1);
		
		assertTrue(entriesForCommunity.size() == 1);
		
		DocumentLibraryEntry entry = entriesForCommunity.get(0);
		
		assertEquals("test.txt", entry.getName());
		assertEquals("test",entry.getDescription());
	}
	
	/* 	PSE2015-63 Eintrag löschen*/
	
	@Test
	public void DeleteEntryFromMainDocumentLibrary()
	{
		loginAsAdmin();
		
		byte[] binaryData = new byte[]{1,2,3,4,5,6,7,8};
		
		documentLibraryService.addEntry("test.txt", "test", binaryData, 1);
		
		List<DocumentLibraryEntry> entriesForCommunity = documentLibraryService.getEntriesForCommunity(1);
		
		assertTrue(entriesForCommunity.size() == 1);
		
		DocumentLibraryEntry entry = entriesForCommunity.get(0);
		
		documentLibraryService.deleteEntry(entry.getId());
		
		entriesForCommunity = documentLibraryService.getEntriesForCommunity(1);
		
		assertTrue(entriesForCommunity.size() == 0);
	}
}
