package at.fhj.swd13.pse.test.db;

import at.fhj.swd13.pse.db.DbContextProvider;
import at.fhj.swd13.pse.db.DbContextProviderImpl;
import at.fhj.swd13.pse.test.util.JdbcTestHelper;

public abstract class DbTestBase {

	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();
	protected static DbContextProvider contextProvider;

	private static boolean installedDb = false;

	protected static void prepare() {

		if (!installedDb) {
			
			setupDb();
			
			contextProvider = new DbContextProviderImpl();

			
			installedDb = true;
		}
	}
	
	protected static void setupDb() {

		JDBC_HELPER.executeSqlScript("SQL/db-create.sql");
		JDBC_HELPER.executeSqlScript("SQL/users.sql");
		JDBC_HELPER.executeSqlScript("SQL/testdata.sql");		
	}
}
