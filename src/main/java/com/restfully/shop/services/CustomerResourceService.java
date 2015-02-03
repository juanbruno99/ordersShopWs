package com.restfully.shop.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.restfully.shop.domain.Customer;
import com.restfully.shop.interfaces.CustomerResource;

@Path("/customers")
public class CustomerResourceService {

	private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
	private AtomicInteger idCounter = new AtomicInteger();

	/*
	 * The createCustomer() method returns a response code of 201, “Created,”
	 * along with a Location header pointing to the absolute URI of the customer
	 * we just created.
	 */

	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) { // Only one Java method
														// parameter can
														// represent the HTTP
														// message body.
		Customer customer = readCustomer(is);
		customer.setId(idCounter.incrementAndGet());
		customerDB.put(customer.getId(), customer);
		System.out.println("Created customer " + customer.getId());
		return Response.created(URI.create("/customers/" + customer.getId()))
				.build();
	}

	/*
	 * when you want to do streaming manually, you must implement and return an
	 * instance of the javax.ws.rs.core.StreamingOutput interface from your
	 * JAX-RS method. StreamingOutput is a callback interface with one callback
	 * method, write():
	 * 
	 * In general, you will not use the StreamingOutput interface to output
	 * responses. In Chapter 6, you will see that JAX-RS has a bunch of nice
	 * content handlers that can automatically convert Java objects straight
	 * into the data format you are sending across the wire.
	 */

	@GET
	@Path("{id}-{id2}")
	@Produces("application/xml")
	public StreamingOutput getCustomer(@PathParam("id") int id, @PathParam("id2") int id2) {
		
		Customer customer1 = new Customer();
		customer1.setId(id+id2);
		customer1.setCountry("Argentina");
		customer1.setFirstName("Juan");
		customer1.setLastName("Brunno");
		customer1.setState("Buenos Aires");
		customer1.setStreet("Paez");
		customer1.setZip("1406");
		customer1.setCity("CABA");
		
		customerDB.put(id+id2, customer1);
		
		final Customer customer = customerDB.get(id+id2);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException,
					WebApplicationException {
				outputCustomer(outputStream, customer);
			}
		};
	}
	
	//Method to retrieve all the customers
	@GET
	@Produces("application/xml")
	public StreamingOutput getAllCustomers() {
		
		Customer customer1 = new Customer();
		customer1.setId(1);
		customer1.setCountry("Argentina");
		customer1.setFirstName("Juan");
		customer1.setLastName("Brunno");
		customer1.setState("Buenos Aires");
		customer1.setStreet("Paez");
		customer1.setZip("1406");
		customer1.setCity("CABA");
		
		customerDB.put(1, customer1);
		
		final Customer customer = customerDB.get(1);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException,
					WebApplicationException {
				outputCustomer(outputStream, customer);
			}
		};
	}

	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public void updateCustomer(int id, InputStream is) {
		Customer update = readCustomer(is);
		Customer current = customerDB.get(id);

		if (current == null)
			throw new WebApplicationException(Response.Status.NOT_FOUND);

		current.setFirstName(update.getFirstName());
		current.setLastName(update.getLastName());
		current.setStreet(update.getStreet());
		current.setState(update.getState());
		current.setZip(update.getZip());
		current.setCountry(update.getCountry());
	}

	protected void outputCustomer(OutputStream os, Customer cust)
			throws IOException {
		PrintStream writer = new PrintStream(os);
		writer.println("<customer id=\"" + cust.getId() + "\">");
		writer.println(" <first-name>" + cust.getFirstName() + "</first-name>");
		writer.println(" <last-name>" + cust.getLastName() + "</last-name>");
		writer.println(" <street>" + cust.getStreet() + "</street>");
		writer.println(" <city>" + cust.getCity() + "</city>");
		writer.println(" <state>" + cust.getState() + "</state>");
		writer.println(" <zip>" + cust.getZip() + "</zip>");
		writer.println(" <country>" + cust.getCountry() + "</country>");
		writer.println("</customer>");
	}

	protected Customer readCustomer(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Customer cust = new Customer();
			if (root.getAttribute("id") != null
					&& !root.getAttribute("id").trim().equals("")) {
				cust.setId(Integer.valueOf(root.getAttribute("id")));
			}
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("first-name")) {
					cust.setFirstName(element.getTextContent());
				} else if (element.getTagName().equals("last-name")) {
					cust.setLastName(element.getTextContent());
				} else if (element.getTagName().equals("street")) {
					cust.setStreet(element.getTextContent());
				} else if (element.getTagName().equals("city")) {
					cust.setCity(element.getTextContent());
				} else if (element.getTagName().equals("state")) {
					cust.setState(element.getTextContent());
				} else if (element.getTagName().equals("zip")) {
					cust.setZip(element.getTextContent());
				} else if (element.getTagName().equals("country")) {
					cust.setCountry(element.getTextContent());
				}
			}
			return cust;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}

}
