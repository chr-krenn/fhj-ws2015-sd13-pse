package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbPersonTest extends DbTestBase {

	@BeforeClass
	public static void setup() {

		DbTestBase.prepare();
	}

	@Test
	public void insert() throws Exception {

		Person p = new Person("etester", "Tester", "Ehrenfried", "1234567");

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			personDAO.insert(p);
			dbContext.commit();

			assertNotEquals(0, p.getPersonId());

		} catch (Exception e) {

			e.printStackTrace();
			fail("Exception " + e.getMessage());

		} finally {

			if (p.getPersonId() != 0) {
				try (DbContext dbContext = contextProvider.getDbContext()) {

					PersonDAO personDAO = dbContext.getPersonDAO();

					personDAO.remove(p.getPersonId());

					dbContext.commit();
				}
			}
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void insertRemovCycle() throws Exception {

		Person p = new Person("etester", "Tester", "Ehrenfried", "12345678");

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p);
			dbContext.commit();

			assertNotEquals(0, p.getPersonId());
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person x = personDAO.getById(p.getPersonId());

			personDAO.remove(x);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person x = personDAO.getById(p.getPersonId());
			assertEquals(x, new Person());
		}
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertDuplicate() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p1 = new Person("etester", "Tester", "Ehrenfried",
					"12345678");

			Person p2 = new Person("etester", "Tester", "Ehrenfried",
					"12345678");

			personDAO.insert(p1);
			personDAO.insert(p2);
			dbContext.commit();

		} catch (Exception e) {

			throw e;
		}
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void remove() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			
			Person p1 = personDAO.getById(996);
			personDAO.remove(p1);
			dbContext.commit();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		} 

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p1 = personDAO.getById(996);
			fail("User not deleted: " + p1.getFullName());
		} 
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void removeOnNull() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.remove(null);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeOnIdZero() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			Person p = new Person();
			p.setPersonId(0);

			personDAO.remove(p);
		}
	}
	
	@Test
	public void createRelation() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p1 = personDAO.getByUsername("angelofr13");
			Person p2 = personDAO.getByUsername("haringst13");
			
			PersonRelation relation = personDAO.createRelation(p1, p2);
			assertEquals(p1,  relation.getSourcePerson());
			assertEquals(p2,  relation.getTargetPerson());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
	
	@Test
	public void removeTargetRelations() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p1 = personDAO.getByUsername("pompenig13");
			personDAO.removeTargetRelations(p1);

			p1 = personDAO.getByUsername("pompenig13");
			assertEquals(0,  p1.getPersonTargetRelations().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void removeRelation() {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p1 = personDAO.getByUsername("oswaldge13");
			Person p2 = personDAO.getByUsername("schmidtr13");
			personDAO.removeRelation(p2, p1);
			dbContext.commit();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			Person p1 = personDAO.getByUsername("oswaldge13");
			assertEquals(0,  p1.getPersonSourceRelations().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
