package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.db.entity.Document;
import at.fhj.swd13.pse.plumbing.JpaHelper;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbDocumentTest extends DbTestBase {

	@BeforeClass
	public static void setup() {

		DbTestBase.prepare();
	}

	@Test
	public void testGetColumnLength() throws NoSuchFieldException, SecurityException {
		Document d = new Document();

		assertEquals(64, JpaHelper.getColumneLength(d, "name"));
	}

	@Test
	public void testGetColumnLengthClass() throws NoSuchFieldException, SecurityException {

		assertEquals(64, JpaHelper.getColumneLength(Document.class, "name"));
	}

	@Test
	public void testSetName_1() {
		Document d = new Document();
		d.setName("1234567890");

		assertEquals("1234567890", d.getName());
	}

	@Test
	public void testSetName_2() throws NoSuchFieldException, SecurityException {

		Document d = new Document();

		final String testName = StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name"));

		d.setName(testName);

		assertEquals(testName, d.getName());
	}

	@Test
	public void testSetName_3() throws NoSuchFieldException, SecurityException {

		Document d = new Document();
		
		final String testName = StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") + 4);
		
		d.setName(testName);
		
		assertEquals( StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") ).length(), d.getName().length() );
		assertEquals( StringUtils.leftPad(".txt", JpaHelper.getColumneLength(d, "name") ), d.getName() );
	}

	@Test
	public void testSetName_4() throws NoSuchFieldException, SecurityException {

		Document d = new Document();
		
		final String testName = StringUtils.leftPad("x", JpaHelper.getColumneLength(d, "name") + 4);
		
		d.setName(testName);
		
		assertEquals( StringUtils.leftPad(" ", JpaHelper.getColumneLength(d, "name") ).length(), d.getName().length() );
		assertEquals( StringUtils.leftPad(" ", JpaHelper.getColumneLength(d, "name") ), d.getName() );
	}
}
