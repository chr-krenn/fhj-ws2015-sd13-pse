/**
 * 
 */
package at.fhj.swd13.pse.domain.user;

import org.junit.Test;

/**
 * @author florian.genser
 *
 */
public class PasswordStrengthValidatorImplTest {

	@Test(expected = WeakPasswordException.class)
	public void testNullPassword() throws WeakPasswordException {
		
		final PasswordStrengthValidatorImpl validator = new PasswordStrengthValidatorImpl();
		
		final String password = null;
		validator.validate(password);
	}
	
	@Test(expected = WeakPasswordException.class)
	public void testEmptyPassword() throws WeakPasswordException {
		
		final PasswordStrengthValidatorImpl validator = new PasswordStrengthValidatorImpl();
		
		final String password = "";
		validator.validate(password);
	}
	
	@Test(expected = WeakPasswordException.class)
	public void testShortPassword() throws WeakPasswordException {
		
		final PasswordStrengthValidatorImpl validator = new PasswordStrengthValidatorImpl();
		
		final String password = "123456";
		validator.validate(password);
	}
	
	@Test
	public void testCorrectPassword() throws WeakPasswordException {
		
		final PasswordStrengthValidatorImpl validator = new PasswordStrengthValidatorImpl();
		
		final String password = "1234567";
		validator.validate(password);
	}
	
}
