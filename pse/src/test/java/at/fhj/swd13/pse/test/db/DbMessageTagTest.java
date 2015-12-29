package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.persistence.NoResultException;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.MessageTagDAO;
import at.fhj.swd13.pse.db.entity.MessageTag;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbMessageTagTest extends DbTestBase {
	
	@BeforeClass
	public static void setup() {

		DbTestBase.prepare();
	}
	
	@Test
	public void testFindById() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			assertNotNull(dbContext.getMessageTagDAO().getById(1));
		}
	}
	
	@Test(expected = NoResultException.class)
	public void getByIdNoneFound() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {
			dbContext.getMessageTagDAO().getById(-1);
		}
	}
	
	@Test
	public void insert() throws Exception {

		MessageTag mt = new MessageTag();

		try (DbContext dbContext = contextProvider.getDbContext()) {
			mt.setTag(dbContext.getTagDAO().getById(1));
			mt.setCreatedAt(new Date());
			
			MessageTagDAO mtDAO = dbContext.getMessageTagDAO();

			mtDAO.insert(mt);
			dbContext.commit();

			assertNotEquals(0, mt.getMessageTagId());

		} catch (Exception e) {

			e.printStackTrace();
			fail("Exception " + e.getMessage());

		} finally {

			if (mt.getMessageTagId() != 0) {
				try (DbContext dbContext = contextProvider.getDbContext()) {

					MessageTagDAO mtDAO = dbContext.getMessageTagDAO();

					mtDAO.remove(mt.getMessageTagId());

					dbContext.commit();
				}
			}
		}
	}


}
