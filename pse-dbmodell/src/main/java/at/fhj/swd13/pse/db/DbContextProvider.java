package at.fhj.swd13.pse.db;

public interface DbContextProvider {

	/**
	 * Get a db-context (session) for the currently configured persistence model
	 * 
	 * @return an open session to the db with an open transaction
	 */
	DbContext getDbContext();
}