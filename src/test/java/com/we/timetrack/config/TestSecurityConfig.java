package com.we.timetrack.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.we.timetrack.model.Employee;

@Configuration
@EnableWebSecurity
@Profile("securityConfigDevelopment")
public class TestSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static Logger logger = Logger.getLogger(TestSecurityConfig.class);
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter,CsrfFilter.class)
		.csrf()
	    .csrfTokenRepository(csrfTokenRepository())
	    .and()
		.authorizeRequests()
		.antMatchers("/", "/home","/resources/**").permitAll()
        .anyRequest().authenticated()
        .and()
		.formLogin()
		.loginPage("/home")
		.defaultSuccessUrl("/timesheet")
		.and()
		.logout()
		.logoutSuccessUrl("/")
		.and()
		.rememberMe()
		.tokenValiditySeconds(2419200)
		.key("timeTrackKey");

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
			.userDetailsContextMapper(new MyUserDetailsContextMapper())
			.userDnPatterns("uid={0},ou=people")
			.groupSearchBase("ou=groups")
			.contextSource()
			.root("dc=we,dc=ru")
			.ldif("classpath:test-server.ldif");
	}
	
	private CsrfTokenRepository csrfTokenRepository() { 
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
	    repository.setSessionAttributeName("_csrf");
	    return repository; 
	}
	
	private class MyUserDetailsContextMapper implements UserDetailsContextMapper {
		
		
		
		@Override
		public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
				Collection<? extends GrantedAuthority> authorities) {
			// Read attributes from active directory
			DirContextAdapter context = (DirContextAdapter)ctx;
			Employee employee = new Employee();
			employee.setName(context.getStringAttribute("givenName"));
			employee.setSurname(context.getStringAttribute("sn"));
			employee.setUsername(context.getStringAttribute("sAMAccountName"));
			employee.setDepartment(context.getStringAttribute("department"));
			//employee.setChief(context.getStringAttribute("manager"));
			employee.setEmployeeId(UUID.fromString("12a24625-9f35-4d33-f7af-0ad444f9e5ee"));
			employee.setMail(context.getStringAttribute("mail"));
			employee.setPost(context.getStringAttribute("title"));
			employee.setAuthorities(loadUserAuthorities(context));
			return employee;
		}

		@Override
		public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
			// TODO Auto-generated method stub
			
		}
		
		private Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations ctx) {
			String[] groups = ctx.getStringAttributes("memberOf");
			
			if (groups == null) {
				logger.debug("No values for 'memberOf' attribute.");
			
				return AuthorityUtils.NO_AUTHORITIES;
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("'memberOf' attribute values: " + Arrays.asList(groups));
			}
			
			ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);
			
			for (String group : groups) {
				authorities.add(new SimpleGrantedAuthority(LdapUtils.getStringValue(LdapUtils.newLdapName(group), "CN")));
			}
			
			return authorities;
		}
	
	}
}
