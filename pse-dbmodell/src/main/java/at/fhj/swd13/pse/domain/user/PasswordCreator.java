package at.fhj.swd13.pse.domain.user;

import java.util.Random;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;

@ManagedBean
@Stateless

public class PasswordCreator {
	
	
	public String createRandomPassword() {
		
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder();
		   for( int i = 0; i < 8; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
	}

}
