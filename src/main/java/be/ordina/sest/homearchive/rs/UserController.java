package be.ordina.sest.homearchive.rs;

import be.ordina.sest.homearchive.security.TokenUtils;
import be.ordina.sest.homearchive.security.transfer.TokenTransfer;
import be.ordina.sest.homearchive.security.transfer.UserTransfer;
import be.ordina.sest.homearchive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User REST API
 * Created by sest on 25/11/14.
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Gets current user from the security context
     * @return response entity with user transfer object
     */

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<UserTransfer> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
            return new ResponseEntity<UserTransfer>(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) principal;
        return new ResponseEntity<UserTransfer>(new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails)), HttpStatus.OK);
    }

    /**
     * authenticates user
     * @param username username
     * @param password password
     * @return token transfer object with security token
     */
    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public TokenTransfer authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
         * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
        UserDetails userDetails = this.userService.loadUserByUsername(username);

        return new TokenTransfer(TokenUtils.createToken(userDetails));
    }

    private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<String, Boolean>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }

        return roles;
    }


}
