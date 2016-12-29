/*
 * Copyright (c) 2016 Donald Purnhagen
 *
 * Description:
 *     Example of a property page using composite panels controlled by
 *     a combo selection.
 *
 * Contributors:
 *     Donald Purnhagen <dpurnhagen@gmail.com>
 *
 * Origin:
 *     https://github.com/dpurnhagen/crabrace
 */
package us.dpeg.crabrace.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import us.dpeg.crabrace.
Messages;
import us.dpeg.crabrace.controls.ComboComposites;
import us.dpeg.crabrace.dialogs.CopyableDialog;

public class CrabraceProperties extends PropertyPage {

	private static final String LOOKS_DEFAULT = Messages.CrabraceProperties_valueLooksDefault;
	private static final String AGILITY_DEFAULT = Messages.CrabraceProperties_valueAgilityDefault;
	private static final String PATH_TITLE = Messages.CrabraceProperties_labelPath;
	private static final String AGILITY_TITLE = Messages.CrabraceProperties_labelExtraNimble;
	private static final String AGILITY_PROPERTY = "AGILITY"; //$NON-NLS-1$
	private static final String AGILITY_KEY = "Agility"; //$NON-NLS-1$
	private static final String LOOKS_TITLE = Messages.CrabraceProperties_labelLooksFactor;
	private static final String LOOKS_PROPERTY = "LOOKS"; //$NON-NLS-1$
	private static final String LOOKS_KEY = "Looks"; //$NON-NLS-1$
	private static final String OWNER_TITLE = Messages.CrabraceProperties_labelOwner;
	private static final String OWNER_PROPERTY = "OWNER"; //$NON-NLS-1$
	private static final String OWNER_KEY = "Owner"; //$NON-NLS-1$
	private static final String OWNER_DEFAULT = Messages.CrabraceProperties_valueOwnerDefault;
	private static final String OWNER_BUTTON = Messages.CrabraceProperties_labelInformation;

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
		lblUgly.setText(Messages.CrabraceProperties_labelUgly);
		scaleLooks = new Scale(cLooks, SWT.HORIZONTAL);
		scaleLooks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		scaleLooks.setMinimum(1);
		scaleLooks.setMaximum(10);
		scaleLooks.setIncrement(1);
		scaleLooks.setSelection(5);
		Label lblSexy = new Label(cLooks, SWT.None);
		lblSexy.setText(Messages.CrabraceProperties_labelSexy);

		// Label for owner field
		Label lblOwner = new Label(cOwner, SWT.NONE);
		lblOwner.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		lblOwner.setText(OWNER_TITLE);

		// Owner text field
		txtOwner = new Text(cOwner, SWT.SINGLE | SWT.BORDER);
		//gdTxtOwner.widthHint = convertWidthInCharsToPixels(50);
		txtOwner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Blank label to skip a cell
		new Label(cOwner, SWT.NONE);

		// Button to test CopyableDialog
		Button btnInfo = new Button(cOwner, SWT.PUSH);
		btnInfo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		btnInfo.setText(OWNER_BUTTON);
		btnInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String owner = txtOwner.getText();
				String message;
				if (OWNER_DEFAULT.equalsIgnoreCase(owner)) {
					StringBuilder sb = new StringBuilder();
					sb.append(Messages.CrabraceProperties_info1);
					sb.append(Messages.CrabraceProperties_info2);
					sb.append(Messages.CrabraceProperties_info3);
					sb.append(Messages.CrabraceProperties_info4);
					message = sb.toString();
				} else {
					message = Messages.CrabraceProperties_info5 + owner + "\n"; //$NON-NLS-2$
				}
				new CopyableDialog(
						getShell(),
						Messages.CrabraceProperties_titleOwnerInformation,
						message,
						true,
						true).open();
			}
		});

		// Populate fields
		IResource element = (IResource) getElement();
		try {
			String agility = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", AGILITY_PROPERTY)); //$NON-NLS-1$
			btnNimble.setSelection(Boolean.parseBoolean((agility != null) ? agility : AGILITY_DEFAULT));
		} catch (CoreException e) {
			btnNimble.setSelection(Boolean.parseBoolean(AGILITY_DEFAULT));
		}
		try {
			String looks = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", LOOKS_PROPERTY)); //$NON-NLS-1$
			scaleLooks.setSelection(Integer.parseInt((looks != null) ? looks : LOOKS_DEFAULT));
		} catch (CoreException e) {
			scaleLooks.setSelection(Integer.parseInt(LOOKS_DEFAULT));
		}
		try {
			String owner = element.getPersistentProperty(new QualifiedName("", OWNER_PROPERTY)); //$NON-NLS-1$
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
			element.setPersistentProperty(new QualifiedName("", AGILITY_PROPERTY), String.valueOf(btnNimble.getSelection())); //$NON-NLS-1$
			element.setPersistentProperty(new QualifiedName("", LOOKS_PROPERTY), String.valueOf(scaleLooks.getSelection())); //$NON-NLS-1$
			element.setPersistentProperty(new QualifiedName("", OWNER_PROPERTY), txtOwner.getText()); //$NON-NLS-1$
		} catch (CoreException e) {
			return false;
		}
		return true;
	}

}
