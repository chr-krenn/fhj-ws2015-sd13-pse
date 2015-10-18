package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;

public class DbPersonRelation {

	private Person p1 = new Person("etester", "Tester", "Ehrenfried", "1234567");
	private Person p2 = new Person("xtester", "Tester", "Xaver", "1234567");
	private Person p3 = new Person("mtester", "Tester", "Mario", "1234567");

	private static DbContextProvider contextProvider;

	@BeforeClass
	public static void construct() {
		contextProvider = new DbContextProviderImpl();
	}

	@Before
	public void setup() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p1);
			personDAO.insert(p2);
			personDAO.insert(p3);

			dbContext.commit();
		}
	}

	@After
	public void cleanup() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person a = personDAO.getById(p1.getPersonId());
			Person b = personDAO.getById(p2.getPersonId());
			Person c = personDAO.getById(p3.getPersonId());

			personDAO.remove(a);
			personDAO.remove(b);
			personDAO.remove(c);

			dbContext.commit();
		}

	}

	@Test
	public void sanityCheck() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			assertEquals("Ehrenfried", e.getFirstName());
			assertEquals("Xaver", x.getFirstName());

			dbContext.rollback();
		}
	}

	@Test
	public void addRelation() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			PersonRelation rel = personDAO.createRelation(e, x);

			assertNotNull(rel);

			dbContext.commit();
		}
	}

	@Test
	public void testTargetRelation() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			PersonRelation rel = personDAO.createRelation(e, x);
			assertNotNull(rel);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			assertEquals(1, e.getPersonSourceRelations().size());
			assertEquals(0, e.getPersonTargetRelations().size());
			assertEquals(0, x.getPersonSourceRelations().size());
			assertEquals(1, x.getPersonTargetRelations().size());

			personDAO.removeTargetRelations(x);
			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			assertEquals(0, e.getPersonTargetRelations().size());
			assertEquals(0, x.getPersonSourceRelations().size());
			assertEquals(0, x.getPersonTargetRelations().size());
			assertEquals(0, e.getPersonSourceRelations().size());
		}
	}

	@Test
	public void testAreRelated() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			PersonRelation rel = personDAO.createRelation(e, x);
			assertNotNull(rel);

			assertTrue(e.isRelatedTo(x));
			assertTrue(x.isRelatedTo(e));
			assertFalse(e.isRelatedTo(e));

			dbContext.commit();
		}
	}
	
	
	@Test
	public void testPersonGetContacts() throws Exception {
		
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person e = personDAO.getById(p1.getPersonId());
			Person x = personDAO.getById(p2.getPersonId());

			PersonRelation rel = personDAO.createRelation(e, x);
			assertNotNull(rel);
			
			assertTrue(e.isRelatedTo(x));
			assertTrue(x.isRelatedTo(e));
			assertFalse(e.isRelatedTo(e));
			
			assertEquals(1, e.getContacts().size());
			assertEquals(1, x.getContacts().size());
			
			assertTrue(e.getContacts().contains(x));
			assertTrue(x.getContacts().contains(e));
			

			dbContext.commit();
		}
		
	}
	
}
