package at.fhj.swd13.pse.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DbContextProviderImpl implements DbContextProvider {

	/**
	 * Default ctor, does nothing
	 */
	public DbContextProviderImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.DbContextProvider#getDbContext()
	 */
	@Override
	public DbContext getDbContext() {

		return new DbContextImpl();
	}

	@PersistenceContext
	private EntityManager em;
}
