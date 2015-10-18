package at.fhj.swd13.pse.service;

import javax.inject.Inject;

import at.fhj.swd13.pse.db.CurrentDbContext;
import at.fhj.swd13.pse.db.DbContext;

public abstract class ServiceBase {

	@Inject
	@CurrentDbContext
	protected DbContext dbContext;

	protected ServiceBase() {}

	protected ServiceBase(DbContext dbContext) {
		this.dbContext = dbContext;
	}
}
