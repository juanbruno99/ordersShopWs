package com.restfully.shop.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class ShopClient {

	public static void main(String[] args) {
	    ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
	    WebResource service = client.resource(getBaseURI());
		
	    //Create domain object to persist
	    String xml = "<customer>"
	    		+ "<first-name>Bill</first-name>"
	    		+ "<last-name>Burke</last-name>"
	    		+ "<street>256 Clarendon Street</street>"
	    		+ "<city>Boston</city>"
	    		+ "<state>MA</state>"
	    		+ "<zip>02115</zip>"
	    		+ "<country>USA</country>"
	    		+ "</customer>";
	    
	    //Make request
	    ClientResponse response = service.path("shop").path("customers").accept(MediaType.APPLICATION_XML).post(ClientResponse.class, xml);
	
	 // Return code should be 201 == created resource
	    System.out.println(response.getStatus());
	    
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/OrdersRestWs").build();
	}

}
