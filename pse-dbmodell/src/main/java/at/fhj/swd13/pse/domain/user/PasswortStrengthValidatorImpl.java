package at.fhj.swd13.pse.domain.user;

public class PasswortStrengthValidatorImpl implements PasswordStrengthValidator
{
	@Override
	public void validate( String password ) throws WeakPasswordException
	{
		if(password == null || password.length() <= 6)
			throw new WeakPasswordException();
	}
}
