package com.natura.web.server.security;

import jakarta.servlet.http.HttpServletRequest;

import lombok.EqualsAndHashCode;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@EqualsAndHashCode(callSuper = true)
public class WebTokenAuthenticationDetails extends WebAuthenticationDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6647315010418156945L;
	
	private String token;
	
	public WebTokenAuthenticationDetails(HttpServletRequest request) {
		super(request);
		
		// Get token detail
		final String requestTokenHeader = request.getHeader("Authorization");
    	// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
    	if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
    		this.token = requestTokenHeader.substring(7);
	}
	
	public String getTokenValue() {
		return this.token;
	}

}
