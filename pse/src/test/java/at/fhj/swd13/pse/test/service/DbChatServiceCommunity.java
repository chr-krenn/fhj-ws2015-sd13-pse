package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.chat.ChatService;
import at.fhj.swd13.pse.domain.chat.ChatServiceImpl;
import at.fhj.swd13.pse.service.DuplicateEntityException;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbChatServiceCommunity extends DbTestBase {

	private Person plainPerson = new Person("plainPerson", "Person", "Plain", "12345678");
	private Person adminPerson = new Person("adminPerson", "Person", "Plain", "12345678");
	private Person inActivePerson = new Person("inActivePerson", "Person", "Plain", "12345678");

	private List<Community> toDelete = new ArrayList<Community>();

	@BeforeClass
	public static void init() throws Exception {
		
		DbTestBase.prepare();
		
		//Setting up private communities per user
		try (DbContext dbContext = contextProvider.getDbContext()) {
			ChatService chatService = new ChatServiceImpl(dbContext);
			chatService.createAllPrivateCommunities();
			dbContext.commit();
		}
	}
	
	@Before
	public void setup() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			context.persist(plainPerson);

			adminPerson.setIsAdmin(true);
			context.persist(adminPerson);

			inActivePerson.setIsActive(false);
			context.persist(inActivePerson);

			context.commit();
		}
	}

	@After
	public void teardown() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			CommunityDAO communityDAO = context.getCommunityDAO();

			for (Community c : toDelete) {
				communityDAO.remove(c.getCommunityId());
			}

			PersonDAO personDao = context.getPersonDAO();

			personDao.remove(plainPerson.getPersonId());
			personDao.remove(adminPerson.getPersonId());
			personDao.remove(inActivePerson.getPersonId());

			context.commit();
		}
	}

	@Test
	public void createUnconfirmed() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "unconfirmed", false));

			dbContext.commit();
		}

		/*
		 * one would expect this to work, albeit it does not... curse jpa, curse
		 * curse curse I guess it is ok, since the entities are detached
		 * anyway...
		 * 
		 * assertEquals(1, plainPerson.getMemberships().size() );
		 */
		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Community c = context.getCommunityDAO().getByName("unconfirmed");
			assertFalse(c.isConfirmed());

			Person p = context.getPersonDAO().getById(plainPerson.getPersonId());
			assertEquals(1, p.getMemberships().size());
			assertEquals("unconfirmed", p.getMemberships().get(0).getCommunity().getName());
		}
	}

	@Test
	public void createConfirmed() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));

			dbContext.commit();
		}

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Community c = context.getCommunityDAO().getByName("confirmed");
			assertTrue(c.isConfirmed());
		}
	}

	@Test(expected = DuplicateEntityException.class)
	public void duplicate() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);

			toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));
			toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));
		}
	}

	@Test(expected = IllegalStateException.class)
	public void inActivePerson() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);

			toDelete.add(chatService.createChatCommunity(inActivePerson.getUserName(), "confirmed", false));
		}
	}

	@Test(expected = EntityNotFoundException.class)
	public void unknownPerson() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);

			toDelete.add(chatService.createChatCommunity("gustl", "confirmed", false));
		}
	}

	@Test
	public void sanityPersonCommunity() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "sanityR", false));

			dbContext.commit();
		}

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Person person = context.getPersonDAO().getById(plainPerson.getPersonId());

			assertEquals(1, person.getCreatedCommunities().size());
			assertEquals("sanityR", person.getCreatedCommunities().get(0).getName());
		}
	}

	@Test
	public void getUnconfirmed() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "unconfirmed", false));

			dbContext.commit();
		}
		/*
		 * one would expect this to work, albeit it does not... curse jpa, curse
		 * curse curse I guess it is ok, since the entities are detached
		 * anyway...
		 * 
		 * assertEquals(1, plainPerson.getMemberships().size() );
		 */
		try (DbContext dbContext = contextProvider.getDbContext()) {
			dbContext.clearCache();

			Community c = dbContext.getCommunityDAO().getByName("unconfirmed");
			assertFalse(c.isConfirmed());

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());
			assertEquals(1, p.getMemberships().size());
			assertEquals("unconfirmed", p.getMemberships().get(0).getCommunity().getName());

			final ChatService chatService = new ChatServiceImpl(dbContext);
			assertEquals(1, chatService.getUnconfirmedCommunities().size());
		}
	}

	@Test
	public void confirmGetUnconfirmed() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "unconfirmed", false));

			dbContext.commit();
		}

		/*
		 * one would expect this to work, albeit it does not... curse jpa, curse
		 * curse curse I guess it is ok, since the entities are detached
		 * anyway...
		 * 
		 * assertEquals(1, plainPerson.getMemberships().size() );
		 */
		try (DbContext dbContext = contextProvider.getDbContext()) {
			dbContext.clearCache();

			Community c = dbContext.getCommunityDAO().getByName("unconfirmed");
			assertFalse(c.isConfirmed());

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());
			assertEquals(1, p.getMemberships().size());
			assertEquals("unconfirmed", p.getMemberships().get(0).getCommunity().getName());

			final ChatService chatService = new ChatServiceImpl(dbContext);

			assertEquals(1, chatService.getUnconfirmedCommunities().size());

			Community unconfirmed = chatService.getUnconfirmedCommunities().get(0);
			chatService.confirmCommunity(adminPerson, unconfirmed);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			Community c = dbContext.getCommunityDAO().getByName("unconfirmed");
			assertTrue(c.isConfirmed());
			assertEquals(adminPerson, c.getConfirmedBy());

			final ChatService chatService = new ChatServiceImpl(dbContext);
			assertEquals(0, chatService.getUnconfirmedCommunities().size());
		}
	}

	@Test(expected = IllegalStateException.class)
	public void confirmGetUnconfirmedIllegal() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final ChatService chatService = new ChatServiceImpl(dbContext);
			toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "unconfirmed", false));

			dbContext.commit();
		}
		/*
		 * one would expect this to work, albeit it does not... curse jpa, curse
		 * curse curse I guess it is ok, since the entities are detached
		 * anyway...
		 * 
		 * assertEquals(1, plainPerson.getMemberships().size() );
		 */
		try (DbContext dbContext = contextProvider.getDbContext()) {
			dbContext.clearCache();

			Community c = dbContext.getCommunityDAO().getByName("unconfirmed");
			assertFalse(c.isConfirmed());

			Person p = dbContext.getPersonDAO().getById(plainPerson.getPersonId());
			assertEquals(1, p.getMemberships().size());
			assertEquals("unconfirmed", p.getMemberships().get(0).getCommunity().getName());

			final ChatService chatService = new ChatServiceImpl(dbContext);
			assertEquals(1, chatService.getUnconfirmedCommunities().size());

			Community unconfirmed = chatService.getUnconfirmedCommunities().get(0);
			chatService.confirmCommunity(p, unconfirmed);

			dbContext.commit();
		}
	}
	
	@Test
	public void testCreateAllPrivateCommunities() throws Exception {

		try(DbContext dbContext = contextProvider.getDbContext()) {
			ChatService chatService = new ChatServiceImpl(dbContext);
			assertNotNull(chatService.getCommunity("@pompenig13"));
		}
	}
}
