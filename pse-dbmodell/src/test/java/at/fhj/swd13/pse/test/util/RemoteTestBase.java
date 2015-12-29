package at.fhj.swd13.pse.test.util;

public abstract class RemoteTestBase {

	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();

	protected static void prepareDatabase() {
		JDBC_HELPER.executeSqlScript("SQL/db-create.sql");
		JDBC_HELPER.executeSqlScript("SQL/users.sql");
		JDBC_HELPER.executeSqlScript("SQL/testdata.sql");
	}
}
