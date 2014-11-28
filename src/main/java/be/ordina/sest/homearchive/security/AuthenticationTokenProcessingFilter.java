package be.ordina.sest.homearchive.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Custom authentication filter
 * Created by sest on 25/11/14.
 *
 * @author sest
 */
@Log4j
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private final UserDetailsService userService;


    public AuthenticationTokenProcessingFilter(final UserDetailsService userService) {
        this.userService = userService;
    }


    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = this.getAsHttpRequest(request);

        String authToken = this.extractAuthTokenFromRequest(httpRequest);
        String userName = TokenUtils.getUserNameFromToken(authToken);

        if (userName != null) {

            UserDetails userDetails = this.userService.loadUserByUsername(userName);
            log.debug(String.format("Found user %s with authorities %s", userDetails.getUsername(), userDetails.getAuthorities()));
            boolean isValidToken = TokenUtils.validateToken(authToken, userDetails);
            log.debug("Token is valid: " + isValidToken);
            if (isValidToken) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Casts {@link javax.servlet.ServletRequest} to {@link javax.servlet.http.HttpServletRequest}
     *
     * @param request input request
     * @return request output request
     */

    private HttpServletRequest getAsHttpRequest(final ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        return (HttpServletRequest) request;
    }

    /**
     * Extractsn security token from request
     *
     * @param httpRequest request
     * @return authToken security token
     */

    private String extractAuthTokenFromRequest(final HttpServletRequest httpRequest) {
        /* Get token from header */
        String authToken = httpRequest.getHeader("X-Auth-Token");

        /* If token not found get it from request parameter */
        if (authToken == null) {
            authToken = httpRequest.getParameter("token");
        }

        return authToken;
    }
}
