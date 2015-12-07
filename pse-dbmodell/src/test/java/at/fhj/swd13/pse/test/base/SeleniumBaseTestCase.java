/**
 * 
 */
package at.fhj.swd13.pse.test.base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author florian.genser
 *
 */
public abstract class SeleniumBaseTestCase {

	protected static WebDriver driver;
	protected static final String BASE_URL = getBaseUrl();

	protected StringBuffer verificationErrors = new StringBuffer();
	private boolean acceptNextAlert = true;

	@BeforeClass
	public static void setUp() throws Exception {
		driver = new FirefoxDriver();
<<<<<<< HEAD
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
=======
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
>>>>>>> 71fe058301c11efedcedbe27087d72c9b3c4afa6
	}

	private static String getBaseUrl() {
		
		String baseUrl = resolveBaseUrl();
		System.out.println("baseUrl for selenium tests is: " + baseUrl);
		return baseUrl;
	}
	
	private static String resolveBaseUrl() {

		final String pseEnv = System.getenv("PSE_ENV");

		final String pseEnvSystem = System.getProperty("PSE_ENV", pseEnv);
		if (pseEnvSystem == null || !pseEnvSystem.equals("CI")) {
			return "http://localhost:8080/pse";
		}
		return "http://localhost:8000/pse";
	}
	
	@After
	public void tearDown() throws Exception {

		if (verificationErrors.length() != 0) {
			String verificationErrorString = verificationErrors.toString();
			Assert.fail(verificationErrorString);

		}

	}

	@AfterClass
	public static void tearDownAl() throws Exception {
		driver.quit();
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	protected String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		}
		finally {
			acceptNextAlert = true;
		}
	}

	/**
	 * Verifies the text of the element found by the id.
	 * @param expected - the expected text
	 * @param id - the html id of the element
	 */
	protected void verifyTextById(String expected, String id) {
		verifyText(expected, By.id(id));
	}

	/**
	 * Verifies the text of the element found by the By.
	 * @param expected - the expected text
	 * @param by - the by of the element
	 */
	protected void verifyText(String expected, By by) {
		verifyEquals(expected, driver.findElement(by).getText());
	}

	/**
	 * Like assertTrue, but fails at the end of the test (during tearDown)
	 * 
	 * @param b
	 *            boolean to verify is true
	 */
	public void verifyTrue(boolean b) {
		try {
			Assert.assertTrue(b);
		} catch (Error e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	/**
	 * Like assertEquals, but fails at the end of the test (during tearDown)
	 * 
	 * @param actual
	 *            the actual object expected
	 * @param expected
	 *            object that you want to compare to actual
	 */
	public void verifyEquals(Object expected, Object actual) {
		try {
			Assert.assertEquals(expected, actual);
		} catch (AssertionError e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	/**
	 * Like assertEquals, but fails at the end of the test (during tearDown)
	 * 
	 * @param actual
	 *            the actual object expected
	 * @param expected
	 *            object that you want to compare to actual
	 */
	public void verifyEquals(boolean expected, boolean actual) {
		try {
			Assert.assertEquals(Boolean.valueOf(expected), Boolean.valueOf(actual));
		} catch (AssertionError e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	/**
	 * Like assertNotEquals, but fails at the end of the test (during tearDown)
	 * 
	 * @param actual
	 *            the actual object expected
	 * @param expected
	 *            object that you want to compare to actual
	 */
	public void verifyNotEquals(Object expected, Object actual) {
		try {
			Assert.assertNotEquals(expected, actual);
		} catch (AssertionError e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	/**
	 * Like assertNotEquals, but fails at the end of the test (during tearDown)
	 * 
	 * @param actual
	 *            the actual object expected
	 * @param expected
	 *            object that you want to compare to actual
	 */
	public void verifyNotEquals(boolean expected, boolean actual) {
		try {
			Assert.assertNotEquals(Boolean.valueOf(expected), Boolean.valueOf(actual));
		} catch (AssertionError e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	/**
	 * Like assertFalse, but fails at the end of the test (during tearDown)
	 * 
	 * @param b
	 *            boolean to verify is false
	 */
	public void verifyFalse(boolean b) {
		try {
			Assert.assertFalse(b);
		} catch (Error e) {
			verificationErrors.append(throwableToString(e));
		}
	}

	private static String throwableToString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
