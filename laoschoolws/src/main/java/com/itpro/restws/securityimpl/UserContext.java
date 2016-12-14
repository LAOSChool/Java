package com.itpro.restws.securityimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.model.User;

public class UserContext  implements UserDetails {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(UserContext.class);
	private static final long serialVersionUID = 1L;
	private User user;

	public UserContext(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		logger.info("UserContext.getGrantedAuthorities Start, ssoId:"+user.getSso_id());
		String roles = user.getRoles();
		
		for(String role : roles.split(",")){
			logger.info("Role: "+role.toUpperCase());
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
		}
		logger.info("authorities: "+authorities);
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getSso_id();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (E_STATE.ACTIVE.value() == user.getState().intValue()){
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		return this == o
			|| o != null && o instanceof UserContext
			&& Objects.equals(user, ((UserContext) o).user);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user);
	}

	@Override
	public String toString() {
		return "UserContext{" +
			"user=" + user +
			'}';
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}


