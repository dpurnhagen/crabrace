/*
 * Copyright (c) 2016 Donald Purnhagen
 *
 * Description:
 *     Set of composite panels controlled by a combo selection.
 *
 * Contributors:
 *     Donald Purnhagen <dpurnhagen@gmail.com>
 *
 * Origin:
 *     https://github.com/dpurnhagen/crabrace
 */
package us.dpeg.crabrace.controls;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import us.dpeg.crabrace.Messages;

/**
 * Maintain a list of composites, which overlay the same space on the page.
 * Enabling visibility by a key string in a combo box. Create the
 * ComboComposite object, with or without a custom label for the combo box.
 * Then use {@link #getComposite(String) getComposite(key)} to add or
 * retrieve a composite. Use {@link #setSelection(String) setSelection(key)}
 * to force the display of a particular composite.
 *
 * <h2>Example of usage:</h2>
 * <pre>
 * {@code
 *	ComboComposites cc = new ComboComposites(parent);
 *	cc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
 *	Composite cAgility = cc.getComposite(AGILITY_KEY);
 *	Composite cOwner = cc.getComposite(OWNER_KEY);
 *	cAgility.setLayout(new GridLayout(1, false));
 *	cOwner.setLayout(new GridLayout(2, false));
 *	// Button for the Agility form
 *	btnNimble = new Button(cAgility, SWT.CHECK);
 *	btnNimble.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
 *	btnNimble.setText(AGILITY_TITLE);
 *	// Label for the Owner form
 *	Label lblOwner = new Label(cOwner, SWT.NONE);
 *	lblOwner.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
 *	lblOwner.setText(OWNER_TITLE);
 *	// Text field Label for the Owner form
 *	txtOwner = new Text(cOwner, SWT.SINGLE | SWT.BORDER);
 *	GridData gdTxtOwner = new GridData(SWT.FILL, SWT.CENTER, true, false);
 *	txtOwner.setLayoutData(gdTxtOwner);
 *	// Show the Agility form
 *	cc.setSelection(AGILITY_KEY);
 * }
 * </pre>
 *
 * @author dpurnhagen
 */
public class ComboComposites extends Composite {
	private Map<String, Composite> map;
	private String label;
	private Combo comboCombo;
	private String selection;

	/**
	 * @param parent the parent container
	 *
	 * @wbp.parser.constructor
	 */
	public ComboComposites(Composite parent) {
		this(parent, Messages.ComboComposites_LabelDefault);
	}

	/**
	 * @param parent the parent container
	 * @param label the text label for the combo box
	 */
	public ComboComposites(Composite parent, String label) {
		super(parent, SWT.NONE);
		map = new HashMap<String, Composite>();
		this.label = label;
		this.selection = ""; //$NON-NLS-1$

		setLayout(new GridLayout(2, false));

		Label lblLabel = new Label(this, SWT.NONE);
		lblLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
		lblLabel.setText(label);

		comboCombo = new Combo(this, SWT.READ_ONLY);
		comboCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSelection(((Combo) e.widget).getText());
			}
		});

	}

	protected void hideComposite(Composite composite, boolean hide) {
		GridData gridData = (GridData) composite.getLayoutData();
		gridData.exclude = hide;
		composite.setVisible(!hide);
	}

	/**
	 * @param key the unique string identifier and text for the combo box entry
	 * @return the composite associated with the key
	 */
	public Composite getComposite(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		final Composite composite = new Composite(this, SWT.None);
		final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		composite.setLayoutData(gd);
		gd.exclude = true;
		map.put(key, composite);
		comboCombo.add(key);
		return composite;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the map
	 */
	protected Map<String, Composite> getMap() {
		return map;
	}

	/**
	 * @return the selection
	 */
	public String getSelection() {
		return selection;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param map the map to set
	 */
	protected void setMap(Map<String, Composite> map) {
		this.map = map;
	}

	/**
	 * The composite associated with the key will be displayed. All other
	 * composites will be hidden.
	 *
	 * @param selection the key to select in the combo box
	 */
	public void setSelection(String key) {
		if (!key.equals(selection)) {
			// hide currently selected composite if it exists
			if (map.containsKey(selection)) {
				Composite c = map.get(selection);
				if (c != null) {
					hideComposite(c, true);
				}
			}
			// show newly selected composite if it exists
			if (map.containsKey(key)) {
				Composite c = map.get(key);
				if (c != null) {
					hideComposite(c, false);
				}
			}
			this.layout();
			// save the newly selected key
			this.selection = key;
		}
		comboCombo.setText(selection);
	}
}
