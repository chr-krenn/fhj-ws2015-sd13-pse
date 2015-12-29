package at.fhj.swd13.pse.domain.tag;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.ServiceException;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * provide all tag related services
 * 
 */
@Stateless
@Local(TagService.class)
public class TagServiceImpl extends ServiceBase implements TagService {
	
	@Inject
	private Logger logger;


	/**
	 * parameterless ctor needed for injection to work
	 */
	public TagServiceImpl() {
		super();
	}

	/**
	 * ctor for testing purposes mainly
	 * 
	 * @param dbContext
	 */
	public TagServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	public List<Tag> getMatchingTags(final String needle) {
		try {
			return dbContext.getTagDAO().getByTokenLike(needle);
		} catch (Throwable ex) {
			logger.info("[TagService] getMatchingTags failed for needle " + needle + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public Tag getTagByToken(String token) {
		try {
			return dbContext.getTagDAO().getByToken(token);
		} catch (Throwable ex) {
			logger.info("[TagService] getTagByToken failed for token " + token + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public void insert(Tag tag) {
		try {
			dbContext.getTagDAO().insert(tag);
		} catch (Throwable ex) {
			logger.info("[TagService] insert failed for tag " + tag.getToken() + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<Tag> getTagsforPerson(Person p) {
		try {
			return dbContext.getTagDAO().getByPerson(p);
		} catch (Throwable ex) {
			logger.info("[TagService] getTagsforPerson failed for Person " + p.getFullName() + " : " + ex.getMessage());
			throw new ServiceException(ex);
		}
	}
}
