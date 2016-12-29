/*
 * Copyright (c) 2016 Innovative Routines International (IRI), Inc.
 *
 * Description:
 *     TODO
 *
 * Contributors:
 *     donp
 *
 * $Id$
 */
package us.dpeg.crabrace;

import org.eclipse.osgi.util.NLS;

/**
 * @author donp
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "us.dpeg.crabrace.messages"; //$NON-NLS-1$
	public static String ComboComposites_LabelDefault;
	public static String CopyableDialog_labelCopyAll;
	public static String CrabraceProperties_info1;
	public static String CrabraceProperties_info2;
	public static String CrabraceProperties_info3;
	public static String CrabraceProperties_info4;
	public static String CrabraceProperties_info5;
	public static String CrabraceProperties_labelExtraNimble;
	public static String CrabraceProperties_labelInformation;
	public static String CrabraceProperties_labelLooksFactor;
	public static String CrabraceProperties_labelOwner;
	public static String CrabraceProperties_labelPath;
	public static String CrabraceProperties_labelSexy;
	public static String CrabraceProperties_labelUgly;
	public static String CrabraceProperties_titleOwnerInformation;
	public static String CrabraceProperties_valueAgilityDefault;
	public static String CrabraceProperties_valueLooksDefault;
	public static String CrabraceProperties_valueOwnerDefault;

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
