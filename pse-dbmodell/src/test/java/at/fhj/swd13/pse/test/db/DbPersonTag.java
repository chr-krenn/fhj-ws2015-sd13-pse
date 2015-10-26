package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.dao.TagDAO;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbPersonTag extends DbTestBase {

	private Person p1 = new Person("etester", "Tester", "Ehrenfried", "1234567");
	private Person p2 = new Person("xtester", "Tester", "Xaver", "1234567");

	private Tag t = new Tag("ballroom", "Gesellschftstanz in allen Varianten");

	@BeforeClass
	public static void construct() {
		DbTestBase.prepare();
	}

	@Before
	public void setup() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			personDAO.insert(p1);
			personDAO.insert(p2);

			dbContext.persist(t);

			dbContext.commit();
		}
	}

	@After
	public void cleanup() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person a = personDAO.getById(p1.getPersonId());
			Person b = personDAO.getById(p2.getPersonId());

			personDAO.remove(a);
			personDAO.remove(b);

			tagDAO.remove(t.getTagId());

			dbContext.commit();
		}
	}

	@Test
	public void setTag() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());

			assertNotNull( x );
			
			Tag l = tagDAO.getById(t.getTagId());

			assertNotNull( l );

			x.addTag(l);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();

			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			assertTrue(x.hasTag(l));
		}
	}

	@Test
	public void setRemoveCycleTag() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			x.addTag(l);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			assertTrue(x.hasTag(l));
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			x.removeTag(l);

			assertFalse(x.hasTag(l));
			assertEquals(0, l.getPersonTags().size());

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			assertFalse(x.hasTag(l));
			assertEquals(0, l.getPersonTags().size());
		}
	}

	@Test
	public void getPersonsByTag1() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();

			Person x = personDAO.getById(p1.getPersonId());
			Tag l = tagDAO.getById(t.getTagId());

			x.addTag(l);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			TagDAO tagDAO = dbContext.getTagDAO();

			Tag l = tagDAO.getById(t.getTagId());

			assertNotNull(l);
			assertEquals(1, l.getPersonTags().size());
		}
	}
	
	@Test
	public void getTagsForPerson() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			PersonDAO personDAO = dbContext.getPersonDAO();
			TagDAO tagDAO = dbContext.getTagDAO();
			List<Tag> tags = tagDAO.getByPerson(personDAO.getById(108));
			
			assertNotNull(tags);
			assertEquals(2, tags.size());
		}
	}
}
