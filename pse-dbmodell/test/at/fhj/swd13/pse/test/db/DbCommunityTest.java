package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;

public class DbCommunityTest {

	private static DbContextProvider contextProvider;

	@BeforeClass
	public static void setup() {

		contextProvider = new DbContextProviderImpl();
	}

	@Test
	public void getPortal() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			CommunityDAO communityDAO = dbContext.getCommunityDAO();

			Community c = communityDAO.getByName("Portal-News");

			assertNotNull(c);
		}
	}

	@Test
	public void insertRemovePortal() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			CommunityDAO communityDAO = dbContext.getCommunityDAO();

			Community c = new Community();
			c.setName("Test-InsertRemove");
			c.setCreatedBy(dbContext.getPersonDAO().getById(1));

			communityDAO.insert(c);

			dbContext.commit();
		}

		int id;
		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			Community c = dbContext.getCommunityDAO().getByName("Test-InsertRemove");

			assertNotNull(c);
			assertNotNull(c.getCreatedAt());

			id = c.getCommunityId();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.getCommunityDAO().remove(id);

			dbContext.commit();
		}

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.clearCache();

			Community c = dbContext.getCommunityDAO().getByName("Test-InsertRemove");

			assertNull(c);
		}
	}
}
