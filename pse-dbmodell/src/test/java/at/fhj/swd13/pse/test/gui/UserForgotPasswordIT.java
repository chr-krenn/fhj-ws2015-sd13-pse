package at.fhj.swd13.pse.test.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;
import at.fhj.swd13.pse.test.gui.pageobjects.LoginPage;
import at.fhj.swd13.pse.test.gui.pageobjects.ResetPasswordPage;

public class UserForgotPasswordIT extends SeleniumBaseTestCase{

	private ResetPasswordPage resetPasswordPage = null;
	private LoginPage loginPage;
	private static final String RESET_PASSWORD_HEADER = "Kennwort zurücksetzen";
	private static final String RESET_STATUS_EMAIL_INVALID = "E-Mail-Adresse nicht gefunden.";
	
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
	public void testResetEmptyPassword() {		
		resetPasswordPage.resetPassword("");
		verifyEquals(RESET_STATUS_EMAIL_INVALID, resetPasswordPage.getStatusText());
	}
	
	@After
	public void teardown() {
		loginPage = null;
		resetPasswordPage = null;
	}

}
