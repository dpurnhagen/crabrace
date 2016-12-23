/*
 * Copyright (c) 2016 Donald Purnhagen
 *
 * Description:
 *     A dialog that displays a string, and has a button to copy
 *     the string to the clipboard.
 *
 * Contributors:
 *     Donald Purnhagen <dpurnhagen@gmail.com>
 */
package us.dpeg.crabrace.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CopyableDialog extends Dialog {
	private static final int COPY_ID = IDialogConstants.CLIENT_ID + 1;
	private String title;
	private String message;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CopyableDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = (title == null ? "" : title); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		if (message != null) {
			text.setText(message);
		}

		return container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == COPY_ID) {
			final Clipboard cb = new Clipboard(Display.getDefault().getActiveShell().getDisplay());
			final TextTransfer textTransfer = TextTransfer.getInstance();
			final Object[] data = new Object[] { message };
			final Transfer[] dataTypes = new Transfer[] { textTransfer };
			cb.setContents(data, dataTypes);
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, COPY_ID, "Copy to Clipboard", false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(450, 300);
//	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
