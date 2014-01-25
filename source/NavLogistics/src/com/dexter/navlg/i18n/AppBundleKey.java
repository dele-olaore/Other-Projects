package com.dexter.navlg.i18n;

import org.jboss.seam.international.status.builder.BundleKey;

public class AppBundleKey extends BundleKey
{
	private static final long serialVersionUID = 7140734369906395219L;
	
	public static final String DEFAULT_BUNDLE_NAME = "messages";

    public AppBundleKey(String key)
    {
        super(DEFAULT_BUNDLE_NAME, key);
    }
}
