package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.DocumentLibraryEntry;

public class DocumentLibraryEntryDAOImpl extends DAOBase implements DocumentLibraryEntryDAO {

	@Inject
	public DocumentLibraryEntryDAOImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public void insert(DocumentLibraryEntry entry) throws ConstraintViolationException{
		dbContext.persist(entry);
	}

	private DocumentLibraryEntry GetById(int id) throws EntityNotFoundException {
		try {
			
			Query query = dbContext.createNamedQuery("DocumentLibraryEntry.findById");
			query.setParameter("id", id);
			return (DocumentLibraryEntry) query.getSingleResult();
		
		} catch (NoResultException e) {
			throw new EntityNotFoundException("No document library entry found for the given id.", e);
		}
	}

	@Override
	public void remove(int documentLibraryEntryId) throws EntityNotFoundException {

		DocumentLibraryEntry entry = GetById(documentLibraryEntryId);
		dbContext.remove(entry);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentLibraryEntry> getAllForCommunity(int communityId) {
		
		Community community = new Community();
		community.setCommunityId(communityId);
		Query query = dbContext.createNamedQuery("DocumentLibraryEntry.getAllForCommunity");
		query.setParameter("community", community);
		return (List<DocumentLibraryEntry>) query.getResultList();
	}

	@Override
	public DocumentLibraryEntry getEntryById(int documentEntryId) {
		Query query = dbContext.createNamedQuery("DocumentLibraryEntry.findById");
		query.setParameter("id", documentEntryId);
		
		return (DocumentLibraryEntry)query.getSingleResult();
		
	}
}
