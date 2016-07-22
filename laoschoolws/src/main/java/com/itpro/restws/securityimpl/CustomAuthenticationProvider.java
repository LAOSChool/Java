package com.itpro.restws.securityimpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.itpro.restws.dao.SysStsDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.Password;
import com.itpro.restws.model.SysSts;
import com.itpro.restws.model.User;
import com.itpro.restws.service.UserService;

@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UserService userService;
	
	@Autowired
	private SysStsDao sysStsgDao;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
	    String username = authentication.getPrincipal() + "";
	    String password = authentication.getCredentials() + "";
	    
	    User user  = null;
//	    if (username.equals("itpro") && password.equals("xxxxxxxx")){
//	    	user = userService.findBySso("itpro");
//	    	if (user == null ){
//		    	user = new User();
//		    	user.setSso_id("itpro");
//		    	user.setPassword("***");
//		    	user.setActflg("A");
//		    	user.setPhone("1234567890");
//		    	user.setState(1);
//		    	user.setRoles("SYS_ADMIN");
//		    	userService.insertUser(user);
//	    	}
//	    }else{
	    	user = userService.findBySso(username);
	    	if (user == null) {
		    	 throw new BadCredentialsException("Username not found.");
		    }
	    	 
//		    if (user.getState() != com.itpro.restws.helper.E_STATE.ACTIVE.value()) {
//		        throw new DisabledException("User state is not active");
//		    }
		    
	    	ArrayList<SysSts> list= (ArrayList<SysSts>) sysStsgDao.findByFval(user.getState());
	    	if (list == null || list.size() <= 0){
	    		throw new BadCredentialsException("User state is not Found");
	    	}
	    	for (SysSts sysSts : list){
	    		if (	sysSts.getSval() != null && 
	    				(!sysSts.getSval().equalsIgnoreCase(E_STATE.ACTIVE.name()))){
	    			
	    			throw new BadCredentialsException("User state is not Active");
	    		}
	    	}
		    try {
				if (!Password.check(password, user.getPassword())) {
					 throw new BadCredentialsException("Wrong password.");
				}
			} catch (Exception e) {
				 throw new BadCredentialsException("Wrong password.");
			}
	    //}
	    
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
