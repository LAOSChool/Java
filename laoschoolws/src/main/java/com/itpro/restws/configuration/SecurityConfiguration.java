package com.itpro.restws.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.itpro.restws.security.AuthenticationService;
import com.itpro.restws.security.TokenAuthenticationFilter;
import com.itpro.restws.security.UnauthorizedEntryPoint;
import com.itpro.restws.securityimpl.AuthenticationServiceDefault;
import com.itpro.restws.securityimpl.CustomAuthenticationProvider;
import com.itpro.restws.securityimpl.TokenManagerSingle;

/***
 * 
 * @author Huy
 * 	  @EnableGlobalMethodSecurity: 
 *    @EnableWebSecurity: will provide configuration via HttpSecurity providing the configuration 
 *    you could find with <http></http> tag in xml configuration, it's allow you to configure 
 *    your access based on urls patterns, the authentication end points, handlers etc...
 *
 */

@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
// securedEnabled: Determines if Spring Security's Secured annotations should be enabled.
// prePostEnabled: Determines if Spring Security's pre post annotations should be enabled
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	 
	 
	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;
	
	// Below configuration to support database authentication:
// Huy start cus Auth test
//	@Autowired
//	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService);
//	}
	@Autowired
	@Qualifier("customAuthenticationProvider")
	AuthenticationProvider customAuthenticationProvider; 	
	// Huy end test
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  
//		http.authorizeRequests()
//	    .antMatchers("/non-secure/**").permitAll()
//	    .antMatchers("/secure/**").hasAuthority("user")
//		.and().requiresChannel().antMatchers("/non-secure/**").requiresInsecure()
//		.and().requiresChannel().antMatchers("/secure/**").requiresSecure() // REQURE HTTPS access
//		.and().exceptionHandling().accessDeniedPage("/Access_Denied");
//	  http.addFilterBefore(myTokenAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
		
		http.csrf().disable();//We don’t need CSRF and typical HTTP session. 
        http.authorizeRequests().antMatchers("/*").permitAll().antMatchers("/api/**").fullyAuthenticated();
//        http.authorizeRequests().antMatchers("/*").permitAll();
//		.and().requiresChannel().antMatchers("/api/**").requiresSecure() // REQURE HTTPS access        
        http.addFilterBefore(myTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedPage("/Access_Denied");
        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        
        http.anonymous().disable();
	  
	}
	
	
	// Huy add Start
	@Bean UnauthorizedEntryPoint unauthorizedEntryPoint() throws Exception {
		UnauthorizedEntryPoint unauthorizedEntryPoint = new UnauthorizedEntryPoint();
		return unauthorizedEntryPoint;
	}
	@Bean 
	TokenManagerSingle tokenManager()  throws Exception {
		TokenManagerSingle tokenManager = new TokenManagerSingle();
		return tokenManager;
	}
	@Bean
	AuthenticationService authenticationService()throws Exception {
		AuthenticationServiceDefault authenticationServiceDefault = new AuthenticationServiceDefault(authenticationManagerBean(), tokenManager());
				return authenticationServiceDefault;
	}
	@Bean
	public TokenAuthenticationFilter myTokenAuthenticationFilter () throws Exception {
		TokenAuthenticationFilter  myTokenAuthenticationFilter = new TokenAuthenticationFilter(authenticationService(), "/logout");
		return myTokenAuthenticationFilter;
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
		// Huy test aut return this.authenticationManager();
		//return new ProviderManager(Arrays.asList((AuthenticationProvider) new CustomAuthenticationProvider()));
		return new ProviderManager(Arrays.asList(customAuthenticationProvider));
		
    }
	
	 
}
