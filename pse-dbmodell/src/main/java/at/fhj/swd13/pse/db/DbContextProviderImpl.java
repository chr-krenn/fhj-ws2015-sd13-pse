package at.fhj.swd13.pse.db;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class DbContextProviderImpl implements DbContextProvider {

	/**
	 * Default ctor, does nothing
	 */
	public DbContextProviderImpl() {		
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.DbContextProvider#getDbContext()
	 */
	@Override
	public DbContext getDbContext() {
		
		return new DbContextImpl();		
	}	
	
	@Named @Produces @CurrentDbContext @RequestScoped
	public DbContext getDbContextJta() {
		return new DbContextJtaImpl();
	}
}
