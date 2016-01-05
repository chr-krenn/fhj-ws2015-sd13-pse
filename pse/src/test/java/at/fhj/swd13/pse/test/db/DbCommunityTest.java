package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.CommunityDAO;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.CommunityMember;
import at.fhj.swd13.pse.db.entity.ParameterException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbCommunityTest extends DbTestBase {

	private static Community inv = new Community("public_not_so");
	private static Community invited = new Community("public_not_so_dochwohl");
	private static Community privMe = new Community("@loeflerm13");
	private static Community privOther = new Community("@salzingera13");

	@BeforeClass
	public static void setup() throws Exception {

		DbTestBase.prepare();

		try (DbContext dbContext = contextProvider.getDbContext()) {

			final Person p = dbContext.getPersonDAO().getByUsername("loeflerm13", true);
			final Person baertl = dbContext.getPersonDAO().getByUsername("salzingera13", true);

			inv.setInvitationOnly(true);
			inv.setCreatedBy(p);

			invited.setInvitationOnly(true);
			invited.setCreatedBy( baertl );
			
			privMe.setCreatedBy(p);
			privMe.setPrivateUser(p);

			privOther.setCreatedBy(baertl);
			privOther.setPrivateUser(baertl);
			
			
			dbContext.getCommunityDAO().insert(inv);
			dbContext.getCommunityDAO().insert(invited);
			dbContext.getCommunityDAO().insert(privMe);
			dbContext.getCommunityDAO().insert(privOther);

			dbContext.commit();
		}
	}

	@AfterClass
	public static void teardown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			if (inv != null && inv.getCommunityId() != 0) {
				dbContext.getCommunityDAO().remove(inv.getCommunityId());
			}

			if (invited != null && invited.getCommunityId() != 0) {
				dbContext.getCommunityDAO().remove(invited.getCommunityId());
			}
			
			if (privMe != null && privMe.getCommunityId() != 0) {
				dbContext.getCommunityDAO().remove(privMe.getCommunityId());
			}
			
			if (privOther != null && privOther.getCommunityId() != 0) {
				dbContext.getCommunityDAO().remove(privOther.getCommunityId());
			}

			dbContext.commit();
		}
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

	@Test(expected = ParameterException.class)
	public void removeIdUnpersisted() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			context.getCommunityDAO().remove(0);
		}
	}

	@Test(expected = ParameterException.class)
	public void removeObjectUnpersisted() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {

			context.getCommunityDAO().remove(null);
		}
	}

	@Test
	public void findMatchingUsers() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getMatchingCommunities("loeflerm13", "Pub");

			assertEquals(1, communities.size());
		}
	}

	@Test
	public void findMatchingUsers2() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getMatchingCommunities("loeflerm13", "@sal");

			assertEquals(1, communities.size());
		}
	}

	@Test
	public void findMatchingUsers3() throws Exception {

		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getMatchingCommunities("loeflerm13", "@loef");

			assertEquals(0, communities.size());
		}
	}
	
	@Test
	public void findMatchingUsers4() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {

			final Community c = context.getCommunityDAO().get( invited.getCommunityId() ) ;
			final Person p = context.getPersonDAO().getByUsername("loeflerm13", true);
			final Person baertl = context.getPersonDAO().getByUsername("salzingera13", true);

			CommunityMember membership = c.addMember( p , false );
			membership.setConfirmer( baertl );
			
			context.persist(  membership );

			context.commit();
		}
		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getMatchingCommunities("loeflerm13", "Pub");

			assertEquals(2, communities.size());
		}	
	}

	@Test
	public void findMatchingUsers5() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getMatchingCommunities("@sal");

			assertEquals(1, communities.size());
		}
	}
	
	@Test
	public void getUnconfirmedCommunites() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			List<Community> communities = context.getCommunityDAO().getUnconfirmedCommunites();
			assertEquals(4, communities.size());
			assertTrue(communities.contains(inv));
			assertTrue(communities.contains(invited));
			assertTrue(communities.contains(privMe));
			assertTrue(communities.contains(privOther));
		}
	}
	
	@Test
	public void remove1() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			Person angelofr13 = context.getPersonDAO().getByUsername("angelofr13", true);
			Person padmin = context.getPersonDAO().getByUsername("padmin", true);
			Community todelete = new Community("@todelete");
			todelete.setInvitationOnly(true);
			todelete.setCreatedBy(angelofr13);
			todelete.setConfirmedBy(padmin);
			context.getCommunityDAO().insert(todelete);

			CommunityDAO communityDAO = context.getCommunityDAO();
			Community c = communityDAO.get(todelete.getCommunityId());
			communityDAO.remove(c);

			List<Community> communities = communityDAO.getAllCommunities();
			assertFalse(communities.contains(todelete));
		}
	}

	@Test(expected = ParameterException.class)
	public void remove2() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			communityDAO.remove(null);
		}
	}

	@Test(expected = ParameterException.class)
	public void remove3() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			communityDAO.remove(new Community());
		}
	}
	
	@Test
	public void getAllCommunities1() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			List<Community> communities = communityDAO.getAllCommunities();
			assertEquals(8, communities.size());
		}
	}
	
	@Test
	public void getAllCommunities2() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			List<Community> communities = communityDAO.getAllCommunities("public_not_so");
			assertEquals(2, communities.size());
		}
	}
	
	@Test
	public void getPrivateCommunity() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			final Person person = context.getPersonDAO().getByUsername("loeflerm13", true);

			Community community = communityDAO.getPrivateCommunity(person);
			assertEquals(privMe, community);
		}
	}
	
	@Test
	public void getAllAccessibleCommunities() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			List<Community> communities = communityDAO.getAllAccessibleCommunities();
			assertEquals(3, communities.size());
		}
	}
	
	@Test
	public void getCommunityMembers() throws Exception {
		try (DbContext context = contextProvider.getDbContext()) {
			CommunityDAO communityDAO = context.getCommunityDAO();
			Community community = communityDAO.getAllCommunities("SWD").get(0);
			Person member = context.getPersonDAO().getByUsername("pompenig13", true);
			
			List<CommunityMember> members = communityDAO.getCommunityMembers(community);
			assertEquals(member, members.get(0).getMember());
		}
	}
}
