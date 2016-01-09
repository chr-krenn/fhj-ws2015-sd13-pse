package at.fhj.swd13.pse.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.domain.tag.TagService;
import at.fhj.swd13.pse.domain.tag.TagServiceFacade;
import at.fhj.swd13.pse.domain.user.UserService;
import at.fhj.swd13.pse.domain.user.UserServiceFacade;
import at.fhj.swd13.pse.test.util.RemoteTestBase;

public class TagServiceIT extends RemoteTestBase {

	private static TagService tagService;
	private static UserService userService;
	
	@BeforeClass
    public static void setupServices() throws NamingException {
        tagService = lookup(TagServiceFacade.class, TagService.class);
        userService = lookup(UserServiceFacade.class, UserService.class);
    }	

	@Before
    public void setup() throws NamingException {
    	prepareDatabase();
    }	
	
    @Test
    public void getMatchingTagsTest() {
    	assertEquals(1, tagService.getMatchingTags("So").size());
    	assertEquals(0, tagService.getMatchingTags("Son").size());
    }
    
    @Test
    public void getTagByTokenTest() {
    	Tag t = tagService.getTagByToken("Software");
    	assertTrue(t != null);
    	assertEquals("Software", t.getToken());
    }
    
    @Test
    public void getTagsforPersonTest() {
    	List<Tag> tags = tagService.getTagsforPerson(userService.getUser("pompenig13"));
    	assertEquals(2, tags.size());
    	assertEquals("Software", tags.get(0).getToken());
    }
    
    @Test
    public void insertTest() {
    	Tag t = new Tag("Sonstiges", "Another tag");
    	tagService.insert(t);
    	assertTrue(tagService.getTagByToken("Sonstiges") != null);
    	assertEquals(2, tagService.getMatchingTags("So").size());
    }
}
