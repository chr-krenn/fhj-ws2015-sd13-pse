package at.fhj.swd13.pse.test.util;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

public abstract class RemoteTestBase {

	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();

	@SuppressWarnings("unchecked")
	protected static <T> T lookup(Class<? extends T> bean, Class<T> viewClass) throws NamingException {
		
	  	final Properties clientProp = new Properties();
    	clientProp.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
    	clientProp.put("remote.connections", "default");
    	final String port = EnvironmentUtil.reolvePort();
    	clientProp.put("remote.connection.default.port", port);
    	clientProp.put("remote.connection.default.host", "localhost");
    	clientProp.put("remote.connection.default.username", "student");
    	clientProp.put("remote.connection.default.password", "student");
    	clientProp.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
    	 
    	final EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(clientProp);
    	final ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
    	EJBClientContext.setSelector(selector);
    	
    	final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        
        final String jndiName = "ejb:" + "" 
        		+ "/" + "pse" 
        		+ "/" 
        		+ "/" + bean.getSimpleName()
        		+ "!" + viewClass.getName();

        return (T) context.lookup(jndiName);
	}
	
	protected static void prepareDatabase() {
		JDBC_HELPER.executeSqlScript("SQL/db-create.sql");
		JDBC_HELPER.executeSqlScript("SQL/users.sql");
		JDBC_HELPER.executeSqlScript("SQL/testdata.sql");
	}
}
