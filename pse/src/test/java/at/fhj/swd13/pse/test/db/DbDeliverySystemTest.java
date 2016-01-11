package at.fhj.swd13.pse.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.dao.DeliverySystemDAO;
import at.fhj.swd13.pse.db.entity.DeliverySystem;
import at.fhj.swd13.pse.test.util.DbTestBase;

public class DbDeliverySystemTest extends DbTestBase {

	@Before
	public void setup() {
		DbTestBase.prepare();
	}

	@Test
	public void get() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DeliverySystemDAO deliverSystemDAO = dbContext.getDeliverySystemDAO();
			DeliverySystem deliverySystem = deliverSystemDAO.get(DeliverySystemDAO.SYSTEM_SMS);
			assertEquals("SMS auf Mobiltelefon", deliverySystem.getName());
		}
	}
	
	@Test
	public void getPseService() throws Exception {
		try (DbContext dbContext = contextProvider.getDbContext()) {
			DeliverySystemDAO deliverSystemDAO = dbContext.getDeliverySystemDAO();
			assertNotEquals(DeliverySystemDAO.SYSTEM_PSE, deliverSystemDAO.getPseService());
		}
	}
}
