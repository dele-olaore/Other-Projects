package com.dexter.navlg.security;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.dexter.navlg.i18n.AppBundleKey;
import com.dexter.navlg.model.User;

@Stateful
@Named("appAuthenticator")
public class AppAuthenticator extends BaseAuthenticator implements Authenticator
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private Messages messages;

    @Inject
    private Credentials credentials;

    @Inject
    @Authenticated
    private Event<User> loginEventSrc;

    public void authenticate()
    {
        log.info("Logging in " + credentials.getUsername());
        if((credentials.getUsername() == null) || (credentials.getCredential() == null))
        {
        	messages.error(new AppBundleKey("error")).detail(new AppBundleKey("identity_loginFailed"));
            setStatus(AuthenticationStatus.FAILURE);
        }
        User user = em.find(User.class, credentials.getUsername());
        if(user != null && credentials.getCredential() instanceof PasswordCredential && 
            user.getPassword().equals(((PasswordCredential) credentials.getCredential()).getValue()))
        {
        	try
        	{
        		loginEventSrc.fire(user);
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}
        	
            messages.info(new AppBundleKey("success")).detail(new AppBundleKey("identity_loggedIn")).detailParams(user.getUsername()).defaults("You're signed in as {0}")
                    .params(user.getUsername());
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(new SimpleUser(user.getUsername())); //TODO confirm the need for this set method
            return;
        }

        messages.error(new AppBundleKey("error")).detail(new AppBundleKey("identity_loginFailed")).defaults("Invalid username or password");
        setStatus(AuthenticationStatus.FAILURE);
    }
}
