package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.NotLoggedInPage;
import at.fhj.swd13.pse.test.gui.pageobjects.ResetPasswordPage;

public class UserForgotPasswordIT extends SeleniumBaseTestCase{

	private ResetPasswordPage resetPasswordPage = null;
	private LoginPage loginPage;
	private NotLoggedInPage notLoggedInPage;
	private static final String RESET_PASSWORD_HEADER = "Kennwort zurücksetzen";
	private static final String RESET_STATUS_EMAIL_INVALID = "An die E-Mail-Adresse konnte nicht gesendet werden.";
	
	@Before
	public void setup() {
		loginPage = new LoginPage(driver, BASE_URL);
		resetPasswordPage = loginPage.forgotPassword();
	}
	
	
	@Test
	public void testOpenResetPasswordPage() {		
		verifyEquals(RESET_PASSWORD_HEADER, resetPasswordPage.getHeaderText());
	}
	
	
	
	@Test
	public void testResetEmptyEmail() {		
		resetPasswordPage.resetPassword("");
		verifyEquals(RESET_STATUS_EMAIL_INVALID, resetPasswordPage.getStatusText());
	}
	
	
	@Test
	public void testLoginWithOldPassword() {
		final String username = "oswaldge13";
		final String passwordOld = "12345678";
		final String emailAddress = "gerald.oswald@edu.fh-joanneum.at";
		
		resetPasswordPage.resetPassword(emailAddress);
		
		//need to wait for some seconds to have the password reset and the e-mail sent
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		notLoggedInPage = loginPage.loginWithWrongCredentials(username, passwordOld);
		
		verifyTrue(notLoggedInPage.isDoorsOfDurinLabelPresent());			
	}
	
	@After
	public void teardown() {
		loginPage = null;
		resetPasswordPage = null;
	}

}
