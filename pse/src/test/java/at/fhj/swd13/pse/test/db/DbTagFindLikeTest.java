package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.TagDAO;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbTagFindLikeTest extends DbTestBase {

	Tag ballroomTag = new Tag("Ballroom", "Alles was mit Tanzen zu tun hat.");
	Tag ballgamesTag = new Tag("Ballspiele", "Alles was mit Fu�ball, Handball usw zu tun hat.");
	Tag cyclingTag = new Tag("Radfahren", "Alles was mit Radfahren zu tun hat.");

	@Before
	public void setup() throws Exception {

		DbTestBase.prepare();

		try (DbContext dbContext = contextProvider.getDbContext()) {

			TagDAO tagDAO = dbContext.getTagDAO();

			tagDAO.insert(ballroomTag);
			tagDAO.insert(ballgamesTag);
			tagDAO.insert(cyclingTag);

			dbContext.commit();
		}
	}

	@After
	public void teardown() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			TagDAO tagDAO = dbContext.getTagDAO();

			tagDAO.remove(ballroomTag.getTagId());
			tagDAO.remove(ballgamesTag.getTagId());
			tagDAO.remove(cyclingTag.getTagId());

			dbContext.commit();
		}
	}

	@Test
	public void findLikeBall() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			TagDAO tagDAO = dbContext.getTagDAO();

			assertEquals(2, tagDAO.getByTokenLike("Ball").size());
		}
	}

	@Test
	public void findLikeball() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			TagDAO tagDAO = dbContext.getTagDAO();

			assertEquals(2, tagDAO.getByTokenLike("ball").size());
		}
	}

	@Test
	public void notFound() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			TagDAO tagDAO = dbContext.getTagDAO();
			assertNull(tagDAO.getByToken("Unknown"));
		}
	}
}
