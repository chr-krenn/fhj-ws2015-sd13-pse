package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.service.UserService;

public class DbUserServiceTest {

	private DbContextProvider contextProvider;

	private UserService userService;

	
	private List<Object> toDelete = new ArrayList<Object>();

	@Before
	public void setup() throws Exception {

		contextProvider = new DbContextProviderImpl();
		
		userService = new UserService();
		userService.setDbContext(contextProvider);
		
		try (DbContext context = contextProvider.getDbContext()) {

			context.getPersonDAO().getById(1).setHashedPassword("--");
			
			context.commit();
		}
	}

	@After
	public void teardown() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			for (Object o : toDelete) {
				context.remove(o);
			}

			context.commit();
		}
	}

	@Test
	public void setDefaultPassword() throws Exception {

		
		assertEquals(1, userService.updateNullPasswords());

		try (DbContext context = contextProvider.getDbContext()) {
			context.clearCache();

			Person p = context.getPersonDAO().getById(1);
			assertNotNull(p);
			assertNotNull(p.getHashedPassword());
		}
	}
}
