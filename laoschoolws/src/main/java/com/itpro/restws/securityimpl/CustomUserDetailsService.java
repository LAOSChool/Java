package com.itpro.restws.securityimpl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.UserDao;
import com.itpro.restws.model.User;

/***
 * This source code base on Spring Security 4 Hibernate Integration Annotation+XML Example
 * http://websystique.com/spring-security/spring-security-4-hibernate-annotation-example/
 * @author Huy
 *
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{
	private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserDao userDao;

	
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String ssoId)
			throws UsernameNotFoundException {
		User user = userDao.findBySSO(ssoId);
		logger.info("loadUserByUsername Start, ssoId:"+ssoId);
		if(user==null){
			logger.info("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
//		return new org.springframework.security.core.userdetails.User(user.getSso_id(), user.getPassword(), 
//			 user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
		return new UserContext(user);
	}

	
//	private List<GrantedAuthority> getGrantedAuthorities(User user){
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		logger.info("getGrantedAuthorities Start, ssoId:"+user.getSso_id());
//		String roles = user.getRoles();
//		
//		for(String role : roles.split(",")){
//			logger.info("Role: "+role);
//			authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
//		}
//		logger.info("authorities: "+authorities);
//		return authorities;
//	}

}
