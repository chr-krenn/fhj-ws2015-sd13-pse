package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.DocumentDAO;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.plumbing.JpaHelper;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbDocumentTest extends DbTestBase {

	@BeforeClass
	public static void setup() {

		DbTestBase.prepare();
	}

	@Test
	public void testGetColumnLength() throws NoSuchFieldException, SecurityException {
		Document d = new Document();

		assertEquals(64, JpaHelper.getColumneLength(d, "name"));
	}

	@Test
	public void testGetColumnLengthClass() throws NoSuchFieldException, SecurityException {

		assertEquals(64, JpaHelper.getColumneLength(Document.class, "name"));
	}

	@Test
	public void testSetName_1() {
		Document d = new Document();
		d.setName("1234567890");

		assertEquals("1234567890", d.getName());
	}

	@Test
	public void testSetName_2() throws NoSuchFieldException, SecurityException {

		Document d = new Document();

		final String testName = StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name"));

		d.setName(testName);

		assertEquals(testName, d.getName());
	}

	@Test
	public void testSetName_3() throws NoSuchFieldException, SecurityException {

		Document d = new Document();
		
		final String testName = StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") + 4);
		
		d.setName(testName);
		
		assertEquals( StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") ).length(), d.getName().length() );
		assertEquals( StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") ), d.getName() );
	}

	@Test
	public void testSetName_4() throws NoSuchFieldException, SecurityException {

		Document d = new Document();
		
		final String testName = StringUtils.leftPad("x", JpaHelper.getColumneLength(d, "name") + 4);
		
		d.setName(testName);
		
		assertEquals( StringUtils.leftPad(" ", JpaHelper.getColumneLength(d, "name") ).length(), d.getName().length() );
		assertEquals( StringUtils.leftPad(" ", JpaHelper.getColumneLength(d, "name") ), d.getName() );
	}
	
	@Test
	public void getById() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			Document document = documentDAO.getById(998);
			assertEquals("dbtestreaddocument", document.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
	
	@Test
	public void insert() throws Exception {
		Document document = new Document("inserttest desc", "application/pdf", "inserttest", 100, "1\1");
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			documentDAO.insert(document);
			dbContext.commit();

			assertNotEquals(0, document.getDocumentId());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		} finally {
			if (document.getDocumentId() != 0) {
				try (DbContext dbContext = contextProvider.getDbContext()) {
					DocumentDAO documentDAO = dbContext.getDocumentDAO();
					documentDAO.remove(document.getDocumentId());
					dbContext.commit();
				}
			}
		}
	}	
	
	@Test
	public void remove() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			Document document = documentDAO.getById(999);
			documentDAO.remove(document);
			dbContext.commit();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		} 

		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			assertNull(documentDAO.getById(999));
		} 
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeOnNull() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			documentDAO.remove(null);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeOnIdZero() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DocumentDAO documentDAO = dbContext.getDocumentDAO();
			Document document = new Document();
			document.setDocumentId(0);
			documentDAO.remove(document);
		}
	}
}
