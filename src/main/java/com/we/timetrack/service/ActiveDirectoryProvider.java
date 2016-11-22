package com.we.timetrack.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.jboss.logging.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.util.StringUtils;

public class ActiveDirectoryProvider {
		
	private static Logger logger = Logger.getLogger("service");
	
	private final String domain;
	private final String url;
	private final String rootDn;
	private String searchFilter = "(&(objectClass=user)(distinguishedName={0}))";
	
	ContextFactory contextFactory = new ContextFactory();
	
	public ActiveDirectoryProvider(String domain, String url){
		this.domain = StringUtils.hasText(url) ? domain.toLowerCase() : null;
		this.url = url;
		this.rootDn = this.domain == null ? null : rootDnFromDomain(this.domain);
	}
	
	
	public DirContextOperations get(String username, String password, String dnToFind) throws NamingException{
				
		DirContext ctx = null;
		try {
			ctx = bindAsUser(username, password);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		try {
			return searchForUser(ctx, dnToFind);
		}
		catch(NamingException e){
			logger.error("Failed to locate directory entry for authenticated user: "
					+ username, e);
			throw e;
		}
		finally {
			LdapUtils.closeContext(ctx);
		}
	}
	
	private DirContext bindAsUser(String username, String password) throws NamingException{
		final String bindUrl = url;
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String bindPrincipal = createBindPrincipal(username);
		env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
		env.put(Context.PROVIDER_URL, bindUrl);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());
		
		return contextFactory.createContext(env);
	}
	
	private DirContextOperations searchForUser(DirContext context, String dn) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		String searchRoot = rootDn;
		
		try {
			return SpringSecurityLdapTemplate.searchForSingleEntryInternal(context,
					searchControls, searchRoot, searchFilter,
					new Object[] {dn });
		}
		catch (IncorrectResultSizeDataAccessException incorrectResults) {
			if (incorrectResults.getActualSize() != 0) {
				throw incorrectResults;
			}
			
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
					"User " +dn + " not found in directory.", incorrectResults);
			throw userNameNotFoundException;
		}
	}
	
	private String rootDnFromDomain(String domain){
		String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
		StringBuilder root = new StringBuilder();
		
		for (String token : tokens){
			if (root.length() > 0) {
				root.append(',');
			}
			root.append("dc=").append(token);
		}
		
		return root.toString();
	}
	
	String createBindPrincipal(String username){
		if (domain == null || username.toLowerCase().endsWith(domain)){
			return username;
		}
		
		return username + "@" + domain;
	}
	
	static class ContextFactory {
		DirContext createContext(Hashtable<?,?> env) throws NamingException {
			return new InitialLdapContext(env, null);
		}
	}
}
