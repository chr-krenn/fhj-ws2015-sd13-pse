package at.fhj.swd13.pse.service;

import at.fhj.swd13.pse.db.DbContext;

public abstract class ServiceBase {

	protected final DbContext dbContext;

	protected ServiceBase ( DbContext dbContext ) {
		
		
		this.dbContext = dbContext;
	}	
}
