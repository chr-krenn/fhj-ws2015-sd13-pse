package at.fhj.swd13.pse.domain.tag;

import java.util.List;

import javax.ejb.Stateless;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * provide all tag related services
 * 
 * @author Gustav Gans
 *
 */
@Stateless
public class TagServiceImpl extends ServiceBase implements TagService {

	/**
	 * parameterless ctor needed for injection to work
	 */
	public TagServiceImpl() {
		super();
	}

	/**
	 * ctor for testing purpses mainly
	 * 
	 * @param dbContext
	 */
	public TagServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.chat.TagService#getMatchingTags(java.lang.String)
	 */
	@Override
	public List<Tag> getMatchingTags(final String needle) {

		return dbContext.getTagDAO().getByTokenLike(needle);
	}

	@Override
	public Tag getTagByToken(String token) {
		return dbContext.getTagDAO().getByToken(token);
	}

	@Override
	public void insert(Tag tag) throws ConstraintViolationException{
		dbContext.getTagDAO().insert(tag);
	}

	@Override
	public List<Tag> getTagsforPerson(Person p) {
		return dbContext.getTagDAO().getByPerson(p);
	}
}
