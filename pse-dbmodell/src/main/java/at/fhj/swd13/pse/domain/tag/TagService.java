package at.fhj.swd13.pse.domain.tag;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.Tag;

public interface TagService {

	List<Tag> getMatchingTags(String needle);
	
	Tag getTagByToken(String token);
	
	void insert(Tag tag) throws ConstraintViolationException;
	
	List<Tag> getTagsforPerson(Person p);
}