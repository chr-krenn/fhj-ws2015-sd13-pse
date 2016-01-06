package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.*;

import org.junit.Test;

import at.fhj.swd13.pse.plumbing.ArgumentChecker;


public class ArgumentCheckerTest {

	@Test
	public void success() {
		ArgumentChecker.assertContent( "gustl", "test" );
	}

	@Test
	public void successNotNull() {
		ArgumentChecker.assertNotNull("gustl", "test" );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fail_null() {
		ArgumentChecker.assertContent( null, "test" );
	}

	@Test(expected=IllegalArgumentException.class)
	public void fail_empty() {
		ArgumentChecker.assertContent( "", "test" );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fail_blank() {
		ArgumentChecker.assertContent( " ", "test" );
	}

	@Test(expected=IllegalArgumentException.class)
	public void fail_whitespace() {
		ArgumentChecker.assertContent( "\t", "test" );
	}	

	@Test(expected=IllegalArgumentException.class)
	public void fail_whitespace2() {
		ArgumentChecker.assertContent( " \t ", "test" );
	}	

	@Test(expected=IllegalArgumentException.class)
	public void fail_NotNull() {
		ArgumentChecker.assertNotNull(null, "test");
	}	
}
