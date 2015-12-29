package at.fhj.swd13.pse.test.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbMailServiceTest extends DbTestBase {

	
	@BeforeClass
	public static void init() throws Exception {
		
		DbTestBase.prepare();
	}
	
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void sendTextMail() {
		
	}	
}
