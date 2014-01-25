package com.dexter.recf.i18n;

import org.jboss.seam.international.status.builder.BundleKey;

public class RecFBundleKey extends BundleKey
{
	public static final String DEFAULT_BUNDLE_NAME = "messages";

    public RecFBundleKey(String key) {
        super(DEFAULT_BUNDLE_NAME, key);
    }
}
