package com.dexter.recf.security;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.dexter.recf.i18n.RecFBundleKey;
import com.dexter.recf.model.User;
import com.dexter.recf.model.UserProfile;

@Stateful
@Named("recfAuthenticator")
public class RecFAuthenticator extends BaseAuthenticator implements Authenticator
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Credentials credentials;

    @Inject
    private Messages messages;

    @Inject
    @Authenticated
    private Event<User> loginEventSrc;

    public void authenticate()
    {
        log.info("Logging in " + credentials.getUsername());
        if((credentials.getUsername() == null) || (credentials.getCredential() == null))
        {
            messages.error(new RecFBundleKey("identity_loginFailed")).defaults("Invalid username or password");
            setStatus(AuthenticationStatus.FAILURE);
        }
        User user = em.find(User.class, credentials.getUsername());
        if(user != null && credentials.getCredential() instanceof PasswordCredential && 
            user.getPassword().equals(((PasswordCredential) credentials.getCredential()).getValue()))
        {
        	if(user.getRole().getName().equalsIgnoreCase("Candidate"))
        	{
	        	try
	        	{
		        	Query q = em.createQuery("SELECT e FROM UserProfile e WHERE user = :nm");
			    	q.setParameter("nm", user);
		        	UserProfile up = (UserProfile)q.getSingleResult();
		        	
		        	if(up != null && up.getStatus() == 1)
		        	{
		        		messages.error(new RecFBundleKey("identity_loginFailed")).defaults("Invalid username or password");
		        		setStatus(AuthenticationStatus.FAILURE);
		        		return;
		        	}
	        	}
	        	catch(Exception ex)
	        	{
	        		ex.printStackTrace();
	        	}
        	}
        	
        	try
        	{
            loginEventSrc.fire(user);
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}
            messages.info(new RecFBundleKey("identity_loggedIn"), user.getUsername()).defaults("You're signed in as {0}")
                    .params(user.getUsername() + " : " + user.getRole());
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(new SimpleUser(user.getUsername())); //TODO confirm the need for this set method
            return;
        }

        messages.error(new RecFBundleKey("identity_loginFailed")).defaults("Invalid username or password");
        setStatus(AuthenticationStatus.FAILURE);
    }
}
