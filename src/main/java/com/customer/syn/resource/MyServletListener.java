package com.customer.syn.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ViewVisitOption;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.Iterator;
import java.util.Map;


@WebListener
public class MyServletListener implements ServletContextListener {

	private final Logger log = LoggerFactory.getLogger(MyServletListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Iterator<? extends Map.Entry<String, ? extends ServletRegistration>> it = event
			.getServletContext().getServletRegistrations().entrySet()
			.iterator();
		while (it.hasNext()) {
			if (it.next().getValue().getClassName()
			      .equals(FacesServlet.class.getName())) {
				fc.getApplication().getViewHandler()
				  .getViews(fc, "/", ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME)
				  .forEach(v -> log.debug("{}", v));
			}
		}
	}


}

