package com.natura.web.server.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class WebTokenAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

  /**
   * @param context the {@code HttpServletRequest} object.
   * @return the {@code WebAuthenticationDetails} containing information about the current request
   */
  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new WebTokenAuthenticationDetails(context);
  }

}
