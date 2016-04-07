package com.itpro.restws.securityimpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.itpro.restws.helper.Password;
import com.itpro.restws.model.User;
import com.itpro.restws.service.UserService;

@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UserService userService;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
	    String username = authentication.getPrincipal() + "";
	    String password = authentication.getCredentials() + "";

	    User user = userService.findBySso(username);
	    if (user == null) {
	    	 throw new BadCredentialsException("Username not found.");
	    }
//	    if (user.getState() != .isDisabled()) {
//	        throw new DisabledException("1001");
//	    }
	    
	    try {
			if (!Password.check(password, user.getPassword())) {
				 throw new BadCredentialsException("Wrong password.");
			}
		} catch (Exception e) {
			 throw new BadCredentialsException("Wrong password.");
		}
	    ArrayList<org.springframework.security.core.GrantedAuthority> grantedAuths = new ArrayList<>();
	    for (String role:user.getRoles().split(",")){
	    	grantedAuths.add(new SimpleGrantedAuthority(role));
	    }
	    
	     
	    //return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
	    return new UsernamePasswordAuthenticationToken(new UserContext(user), password, grantedAuths);
	}
	public boolean supports(Class<?> arg0) {
        return true;
    }

}
