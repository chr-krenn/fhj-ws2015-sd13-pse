package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.dao.PersonDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.service.ChatService;
import at.fhj.swd13.pse.service.DuplicateEntityException;

public class DbChatServiceCommunity {

	private ChatService chatService;

	private DbContextProvider contextProvider;

	private Person plainPerson = new Person("plainPerson", "Person", "Plain", "12345678");
	private Person adminPerson = new Person("adminPerson", "Person", "Plain", "12345678");
	private Person inActivePerson = new Person("inActivePerson", "Person", "Plain", "12345678");

	private List<Community> toDelete = new ArrayList<Community>();

	@Before
	public void setup() throws Exception {

		contextProvider = new DbContextProviderImpl();

		chatService = new ChatService();
		chatService.setDbContext(contextProvider);

		try (DbContext context = contextProvider.getDbContext()) {

			context.persist(plainPerson);
			
			adminPerson.setIsAdmin( true );
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

		toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "unconfirmed", false));

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Community c = context.getCommunityDAO().getByName("unconfirmed");
			assertFalse(c.isConfirmed());
		}
	}

	@Test
	public void createConfirmed() throws Exception {

		toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Community c = context.getCommunityDAO().getByName("confirmed");
			assertTrue(c.isConfirmed());
		}
	}
	
	@Test(expected=DuplicateEntityException.class)
	public void duplicate() throws Exception {

		toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));		
		toDelete.add(chatService.createChatCommunity(adminPerson.getUserName(), "confirmed", false));		
	}

	@Test(expected=IllegalStateException.class)
	public void inActivePerson() throws Exception {

		toDelete.add(chatService.createChatCommunity( inActivePerson.getUserName(), "confirmed", false));		
	}

	@Test(expected=EntityNotFoundException.class)
	public void unknownPerson() throws Exception {

		toDelete.add(chatService.createChatCommunity( "gustl", "confirmed", false));		
	}
	
	@Test
	public void sanityPersonCommunity() throws Exception {

		toDelete.add(chatService.createChatCommunity(plainPerson.getUserName(), "sanityR", false));

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();
			
			Person person = context.getPersonDAO().getById( plainPerson.getPersonId() );
			
			assertEquals( 1, person.getCreatedCommunities().size() );
			assertEquals("sanityR", person.getCreatedCommunities().get(0).getName() );
		}
	}	
}
