package at.fhj.swd13.pse.plumbing;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 * Session Bean implementation class LoggerProducer
 */
@Stateless
public class LoggerProducer {

	private final Logger logger = Logger.getLogger(LoggerProducer.class);

	@Produces
	public Logger getLogger(InjectionPoint injectionPoint) {

		logger.info("[LOG] giving away a logger to " + injectionPoint.getMember().getDeclaringClass().getName());

		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
}
