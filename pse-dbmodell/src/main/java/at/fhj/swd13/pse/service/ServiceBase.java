package at.fhj.swd13.pse.service;

import at.fhj.swd13.pse.db.DbContextProvider;

public abstract class ServiceBase {

	protected DbContextProvider contextProvider;

	public void setDbContext(final DbContextProvider contextProvider) {
		this.contextProvider = contextProvider;
	}
}
