package javax.ws.rs.core;

import java.util.Collections;
import java.util.Set;

/**
 * It is easiest to deploy JAX-RS within a Java EE–certified application server (e.g., JBoss)
or standalone Servlet 3 container (e.g., Tomcat). Before we can do that, we need to write
one simple class that extends javax.ws.rs.core.Application. This class tells our application
server which JAX-RS components we want to register.
 * */

public abstract class Application {

	private static final Set<Object> emptySet = Collections.emptySet();

	public abstract Set<Class<?>> getClasses();

	/*
	 * The getSingletons() method returns a list of JAX-RS service objects (and providers,
too—again, see Chapter 6). You, as the application programmer, are responsible for
creating and initializing these objects.
	 * **/
	
	public Set<Object> getSingletons() {
		return emptySet;
	}
}
