package at.fhj.swd13.pse.db;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

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
	
	@Inject
	private Logger logger;
	
	@PersistenceContext
	private EntityManager em;
	
	@Named @Produces @CurrentDbContext @RequestScoped
	public DbContext getDbContextJta() {
		
		logger.info("[PERSISTENCE] new context");
		
		DbContext newContext = new DbContextJtaImpl( em ); 
		
		return newContext; 
	}
}
