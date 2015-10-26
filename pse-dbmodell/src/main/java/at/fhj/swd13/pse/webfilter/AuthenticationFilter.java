package at.fhj.swd13.pse.webfilter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import at.fhj.swd13.pse.plumbing.UserSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	@Inject
	private Logger logger;

	@Inject
	private UserSession userSession;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getRequestURI().indexOf("/protected") < 0 || isLoggedIn(request)) {

			logger.info("[AUTH] allowed " + req.getRequestURI() );
			chain.doFilter(request, response);

		} else {

			logger.info("[AUTH] denied " + req.getRequestURI() );

			HttpServletResponse servletResponse = (HttpServletResponse) response;
			servletResponse.sendRedirect(req.getContextPath() + "/NotLoggedIn.jsf");
		}
	}

	private boolean isLoggedIn(ServletRequest request) {
		return userSession.isLoggedIn();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
}
