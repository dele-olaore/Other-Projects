/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dexter.navlg.security;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.solder.logging.Logger;

import com.dexter.navlg.bean.PermissionManager;
import com.dexter.navlg.model.User;

/**
 * Exposes the currently logged in user
 *
 * @author Oladele Olaore
 */
@Stateful
@SessionScoped
public class CurrentUserManager
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    PermissionManager permManager;
    
    private User currentUser;

    @Produces
    @Authenticated
    @Named("currentUser")
    public User getCurrentAccount()
    {
        return currentUser;
    }
    
    @Produces
    @Authenticated
    @Named("dashboard")
    public String dashboard()
    {
    	String ret = "dashboard";
    	
    	return ret;
    }

    // Injecting HttpServletRequest instead of HttpSession as the latter conflicts with a Weld bean on GlassFish 3.0.1
    public void onLogin(@Observes @Authenticated User user, HttpServletRequest request)
    {
        currentUser = user;
        request.getSession().setAttribute("curUsername", user.getUsername());
        // reward authenticated users with a longer session
        // default is kept short to prevent search engines from driving up # of sessions
        request.getSession().setMaxInactiveInterval(3600);
        //permManager.setUser(user);
        //permManager.loadFunctions();
    }
}
