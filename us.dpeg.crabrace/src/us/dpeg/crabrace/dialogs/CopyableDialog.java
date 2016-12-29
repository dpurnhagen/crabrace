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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple, resizable dialog box with a single text widget, a button to
 * copy the contents of the text widget to the clipboard, and an OK button.
 *
 * @author dpurnhagen
 */
public class CopyableDialog extends Dialog {
	private static final int FLASH_MILLIS = 125;
	private static final int COPY_ID = IDialogConstants.CLIENT_ID + 1;
	private String title;
	private String message;
	private StyledText text;
	private boolean busy;
	private Display display;
	private boolean flash;
	private boolean wrap;

	/**
	 * Convenience constructor with more options.
	 *
	 * @param parentShell the parent shell
	 * @param title the title of the window
	 * @param message the text in the body of the text widget
	 * @param flash controls the flashing red of the text widget background when the text is copied
	 * @param wrap controls the wrapping of the text if the lines are too long to fit
	 */
	public CopyableDialog(Shell parentShell, String title, String message, boolean flash, boolean wrap) {
		super(parentShell);
		this.title = (title == null ? "" : title); //$NON-NLS-1$
		this.message = message;
		this.flash = flash;
		this.wrap = wrap;
		this.busy = false;
	}

	/**
	 * Create the dialog. Use setters before open() to configure.
	 *
	 * @param parentShell the parent shell
	 * @param title the title of the window
	 */
	public CopyableDialog(Shell parentShell, String title) {
		this(parentShell, title, null, true, false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == COPY_ID) {
			if (flash) {
				busy = true;
    			final Color background = text.getBackground();
    			text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
    			display.timerExec(FLASH_MILLIS, new Runnable() {
    				@Override
    				public void run() {
    					text.setBackground(background);
    					busy = false;
    				}
    			});
			}
			final Clipboard cb = new Clipboard(Display.getDefault().getActiveShell().getDisplay());
			final TextTransfer textTransfer = TextTransfer.getInstance();
			final Object[] data = new Object[] { message };
			final Transfer[] dataTypes = new Transfer[] { textTransfer };
			cb.setContents(data, dataTypes);
		}
		super.buttonPressed(buttonId);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close() {
		while (busy) {
			display.syncExec(new Runnable() {
				public void run() {
					// nothing to do here, just waiting...
				}
			});
		}
		return super.close();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		this.display = newShell.getDisplay();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, COPY_ID, "Copy All", false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.MULTI;
		if (wrap) {
			style |= SWT.WRAP;
		}
		text = new StyledText(container, style);
		text.setAlwaysShowScrollBars(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.widthHint = 440;
		text.setLayoutData(gd_text);
		if (message != null) {
			text.setText(message);
		}
		return container;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * @param flash controls the flashing red of the text widget background when the text is copied
	 */
	public void setFlash(boolean flash) {
		this.flash = flash;
	}

	/**
	 * @param message the text in the body of the text widget
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param title the title of the window
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param wrap controls the wrapping of the text if the lines are too long to fit
	 */
	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}

}
