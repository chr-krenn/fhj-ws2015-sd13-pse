package at.fhj.swd13.pse.test.db;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;

public class DbContextTest {

	private static DbContextProvider contextProvider;

	@BeforeClass
	public static void setup() {
		contextProvider = new DbContextProviderImpl();
	}

	@Test(expected = IllegalStateException.class)
	public void commitOnClosedTx() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.rollback();
			dbContext.commit();
		}
	}

	@Test(expected = IllegalStateException.class)
	public void rollbackOnClosedTx() throws Exception {

		try (DbContext dbContext = contextProvider.getDbContext()) {

			dbContext.commit();
			dbContext.rollback();
		}
	}
}
