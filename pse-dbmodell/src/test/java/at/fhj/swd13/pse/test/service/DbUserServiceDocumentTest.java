package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceImpl;
import at.fhj.swd13.pse.plumbing.UserSession;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbUserServiceDocumentTest extends DbTestBase {

	private UserSession userSession;

	private Person plainPerson = new Person("plainPerson", "Person", "Plain", "12345678");
	private Person pwPerson = new Person("pwPerson", "Person", "PW", "12345678");

	private Document document = new Document();

	@Before
	public void setup() throws Exception {

		DbTestBase.prepare();

		try (DbContext context = contextProvider.getDbContext()) {

			context.getPersonDAO().getById(1).setHashedPassword("--");

			context.getPersonDAO().insert(plainPerson);
			context.getPersonDAO().insert(pwPerson);

			document.setDescription("This is the description");
			document.setMimeType("text/plain");
			document.setCreatedAt(new Date());
			document.setName("test.txt");
			document.setSize(4711);
			document.setStorageLocation("daham");

			context.getDocumentDAO().insert(document);

			context.commit();
		}

		userSession = new UserSession();
	}

	@After
	public void teardown() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			context.getPersonDAO().remove(plainPerson.getPersonId());
			context.getPersonDAO().remove(pwPerson.getPersonId());

			context.getDocumentDAO().remove(document.getDocumentId());

			context.commit();
		}
	}

	@Test
	public void sanity() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());

			assertNull(p.getDocument());
		}
	}

	@Test
	public void setDocument() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final UserService userService = new UserServiceImpl(dbContext, userSession);

			userService.setUserImage(plainPerson.getUserName(), document.getDocumentId());

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());

			assertEquals(document.getDocumentId(), p.getDocument().getDocumentId());
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());

			assertEquals(document.getDocumentId(), p.getDocument().getDocumentId());
		}
	}
}
