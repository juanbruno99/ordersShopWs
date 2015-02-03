package com.restfully.shop.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * The @ApplicationPath defines the relative base URL path for all our JAX-RS services
in the deployment. So, in this example, all of our JAX-RS RESTful services will be prefixed
with the /services path when we execute on them.
 * **/

@ApplicationPath("/services")
public class ShoppingApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public ShoppingApplication() {
		singletons.add(new CustomerResourceService());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
