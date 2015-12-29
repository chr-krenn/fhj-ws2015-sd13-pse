package at.fhj.swd13.pse.domain.tag;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;

@Stateless
@Remote(TagService.class)
public class TagServiceFacade implements TagService {
	
	@EJB(beanName="TagServiceImpl")
	private TagService tagService;


	@Override
	public List<Tag> getMatchingTags(String needle) {
		return tagService.getMatchingTags(needle);
	}

	@Override
	public Tag getTagByToken(String token) {
		return tagService.getTagByToken(token);
	}

	@Override
	public void insert(Tag tag) throws ConstraintViolationException {
		insert(tag);
	}

	@Override
	public List<Tag> getTagsforPerson(Person p) {
		return tagService.getTagsforPerson(p);
	}
}
