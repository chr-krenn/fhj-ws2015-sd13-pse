package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.DocumentDAO;
import at.fhj.swd13.pse.db.dao.DocumentLibraryEntryDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.DocumentLibraryEntry;
import at.fhj.swd13.pse.test.util.DbTestBase;
import at.fhj.swd13.pse.db.EntityNotFoundException;

public class DbDocumentLibraryEntryTest extends DbTestBase {

	@BeforeClass
	public static void setup() {
		DbTestBase.prepare();
	}

	@Test
	public void getById() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			DocumentLibraryEntry entry = documentLibraryDAO.getById(998);
			assertEquals("dbtestdocument", entry.getDocument().getName());
			assertEquals("SWD", entry.getCommunity().getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}	
	
	@Test
	public void getAllForCommunity() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			List<DocumentLibraryEntry> entries = documentLibraryDAO.getAllForCommunity(100);
			assertEquals(1, entries.size());
			assertEquals("dbtestdocument", entries.get(0).getDocument().getName());
			assertEquals("SWD", entries.get(0).getCommunity().getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}	

	@Test
	public void insert() throws Exception {
		DocumentLibraryEntry entry = null;
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			CommunityDAO communityDAO = dbContext.getCommunityDAO();
			DocumentDAO documentDAO = dbContext.getDocumentDAO();

			Community community = communityDAO.getByName("Portal-News");
			Document document = documentDAO.getById(998);
			entry = new DocumentLibraryEntry(community, document);

			documentLibraryDAO.insert(entry);
			dbContext.commit();

			assertNotEquals(0, entry.getDocumentLibraryEntryId());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		} finally {
			if (entry != null && entry.getDocumentLibraryEntryId() != 0) {
				try (DbContext dbContext = contextProvider.getDbContext()) {
					DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
					documentLibraryDAO.remove(entry.getDocumentLibraryEntryId());
					dbContext.commit();
				}
			}
		}
	}	
	
	@Test(expected = EntityNotFoundException.class)
	public void remove() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			documentLibraryDAO.remove(999);
			dbContext.commit();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		} 

		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			documentLibraryDAO.getById(999);
			fail("DocumentLibraryEntry not deleted: " + 999);
		} 
	}

	@Test(expected = EntityNotFoundException.class)
	public void removeOnIdZero() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentLibraryEntryDAO documentLibraryDAO = dbContext.getDocumentLibraryDAO();
			documentLibraryDAO.remove(0);
		}
	}
}	

