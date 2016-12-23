/*
 * Copyright (c) 2016 Donald Purnhagen
 *
 * Description:
 *     Example of a property page using composite panels controlled by 
 *     a combo selection.
 *
 * Contributors:
 *     Donald Purnhagen <dpurnhagen@gmail.com>
 */
package us.dpeg.crabrace.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import us.dpeg.crabrace.controls.ComboComposites;

public class CrabraceProperties extends PropertyPage {

	private static final String LOOKS_DEFAULT = "5";
	private static final String AGILITY_DEFAULT = "false";
	private static final String PATH_TITLE = "Path:";
	private static final String AGILITY_TITLE = "&Extra nimble";
	private static final String AGILITY_PROPERTY = "AGILITY";
	private static final String AGILITY_KEY = "Agility";
	private static final String LOOKS_TITLE = "&Looks Factor:";
	private static final String LOOKS_PROPERTY = "LOOKS";
	private static final String LOOKS_KEY = "Looks";
	private static final String OWNER_TITLE = "&Owner:";
	private static final String OWNER_PROPERTY = "OWNER";
	private static final String OWNER_KEY = "Owner";
	private static final String OWNER_DEFAULT = "John Doe";

	private Text txtOwner;
	private Button btnNimble;
	private Scale scaleLooks;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public CrabraceProperties() {
		super();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		//Label for path field
		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(PATH_TITLE);

		// Path text field
		Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		pathValueText.setText(((IResource) getElement()).getFullPath().toString());
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addSecondSection(Composite parent) {
		ComboComposites cc = new ComboComposites(parent);
		cc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite cAgility = cc.getComposite(AGILITY_KEY);
		Composite cLooks = cc.getComposite(LOOKS_KEY);
		Composite cOwner = cc.getComposite(OWNER_KEY);

		cAgility.setLayout(new GridLayout(1, false));
		cLooks.setLayout(new GridLayout(3, false));
		cOwner.setLayout(new GridLayout(2, false));

		// Button for agility
		btnNimble = new Button(cAgility, SWT.CHECK);
		btnNimble.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnNimble.setText(AGILITY_TITLE);

		// Label for looks field
		Label lblLooks = new Label(cLooks, SWT.NONE);
		lblLooks.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 1));
		lblLooks.setText(LOOKS_TITLE);

		// Slider for looks field
		Label lblUgly = new Label(cLooks, SWT.None);
		lblUgly.setText("Ugly");
		scaleLooks = new Scale(cLooks, SWT.HORIZONTAL);
		scaleLooks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		scaleLooks.setMinimum(1);
		scaleLooks.setMaximum(10);
		scaleLooks.setIncrement(1);
		scaleLooks.setSelection(5);
		Label lblSexy = new Label(cLooks, SWT.None);
		lblSexy.setText("Sexy");

		// Label for owner field
		Label lblOwner = new Label(cOwner, SWT.NONE);
		lblOwner.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		lblOwner.setText(OWNER_TITLE);

		// Owner text field
		txtOwner = new Text(cOwner, SWT.SINGLE | SWT.BORDER);
		GridData gdTxtOwner = new GridData(SWT.FILL, SWT.CENTER, true, false);
		//gdTxtOwner.widthHint = convertWidthInCharsToPixels(50);
		txtOwner.setLayoutData(gdTxtOwner);

		// Populate fields
		IResource element = (IResource) getElement();
		try {
			String agility = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", AGILITY_PROPERTY));
			btnNimble.setSelection(Boolean.parseBoolean((agility != null) ? agility : AGILITY_DEFAULT));
		} catch (CoreException e) {
			btnNimble.setSelection(Boolean.parseBoolean(AGILITY_DEFAULT));
		}
		try {
			String looks = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", LOOKS_PROPERTY));
			scaleLooks.setSelection(Integer.parseInt((looks != null) ? looks : LOOKS_DEFAULT));
		} catch (CoreException e) {
			scaleLooks.setSelection(Integer.parseInt(LOOKS_DEFAULT));
		}
		try {
			String owner = element.getPersistentProperty(new QualifiedName("", OWNER_PROPERTY));
			txtOwner.setText((owner != null) ? owner : OWNER_DEFAULT);
		} catch (CoreException e) {
			txtOwner.setText(OWNER_DEFAULT);
		}

		cc.setSelection(AGILITY_KEY);
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSeparator(composite);
		addSecondSection(composite);
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		super.performDefaults();
		// Populate the controls with the default values
		btnNimble.setSelection(Boolean.parseBoolean(AGILITY_DEFAULT));
		scaleLooks.setSelection(Integer.parseInt(LOOKS_DEFAULT));
		txtOwner.setText(OWNER_DEFAULT);
	}

	public boolean performOk() {
		// store the value in the owner text field
		IResource element = (IResource) getElement();
		try {
			element.setPersistentProperty(new QualifiedName("", AGILITY_PROPERTY), String.valueOf(btnNimble.getSelection()));
			element.setPersistentProperty(new QualifiedName("", LOOKS_PROPERTY), String.valueOf(scaleLooks.getSelection()));
			element.setPersistentProperty(new QualifiedName("", OWNER_PROPERTY), txtOwner.getText());
		} catch (CoreException e) {
			return false;
		}
		return true;
	}

}