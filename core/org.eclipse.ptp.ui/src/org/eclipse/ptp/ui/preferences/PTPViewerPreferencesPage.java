/*******************************************************************************
 * Copyright (c) 2005 The Regents of the University of California. 
 * This material was produced under U.S. Government contract W-7405-ENG-36 
 * for Los Alamos National Laboratory, which is operated by the University 
 * of California for the U.S. Department of Energy. The U.S. Government has 
 * rights to use, reproduce, and distribute this software. NEITHER THE 
 * GOVERNMENT NOR THE UNIVERSITY MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR 
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified 
 * to produce derivative works, such modified software should be clearly marked, 
 * so as not to confuse it with the version available from LANL.
 * 
 * Additionally, this program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * LA-CC 04-115
 *******************************************************************************/
package org.eclipse.ptp.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ptp.ui.IPTPUIConstants;
import org.eclipse.ptp.ui.PTPUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Clement chu
 * 
 */
public class PTPViewerPreferencesPage extends AbstractPerferencePage {
	private IconIntFieldEditor iconSpacingXField = null;
	private IconIntFieldEditor iconSpacingYField = null;
	private IconIntFieldEditor iconWidthField = null;
	private IconIntFieldEditor iconHeightField = null;
	private IconIntFieldEditor toolTipField = null;
	
	private class IconIntFieldEditor {
		private int textLimit = 5;
		private String labelText = null;
		private int min = 0;
		private int max = 10;
		private Text textField = null;		
		private String msg = "";
		
		IconIntFieldEditor(String labelText, int min, int max, Composite parent) {
			this.labelText = labelText;
			this.min = min;
			this.max = max;
			createControl(parent);
		}
		protected void createControl(Composite parent) {
			doFillIntoGrid(parent);
		}
		protected void doFillIntoGrid(Composite parent) {
			new Label(parent, SWT.LEFT).setText(labelText);
            
			textField = new Text(parent, SWT.SINGLE | SWT.BORDER);
            textField.setFont(parent.getFont());
            textField.setTextLimit(textLimit);
            textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        	textField.addModifyListener(new ModifyListener() {
        		public void modifyText(ModifyEvent e) {
        			PTPViewerPreferencesPage.this.isValid();
        		}	        		
        	});
			
			new Label(parent, SWT.RIGHT).setText("(" + min + "-" + max + ")");
		}
	    public void setValue(int value) {
	        if (textField != null) {
	        	textField.setText(String.valueOf(value));
	        }
	    }
	    public int getValue() {
	    	try {
	    		return Integer.parseInt(textField.getText());
	    	} catch (NumberFormatException e) {
	    		return 0;
	    	}
	    }
	    public void setErrorMessage(String msg) {
	    	this.msg = msg;
	    }
	    public String getErrorMessage() {
	    	return msg;
	    }
	    public boolean isValid() {
	    	setErrorMessage("");
	    	try {
	    		int value = Integer.parseInt(textField.getText());
	    		if (value < min || value > max) {
	    			setErrorMessage("Value must be in the range of (" + min + "-" + max + ")");
	    			return false;
	    		}
	    	} catch (NumberFormatException e) {
	    		setErrorMessage("Value must be integer: " + e.getMessage());
	    		return false;
	    	}
	    	return true;
	    }
	}
	
	public PTPViewerPreferencesPage() {
		super();
		setPreferenceStore(PTPUIPlugin.getDefault().getPreferenceStore());
		setDescription(PreferenceMessages.getString("PTPViewerPreferencePage.desc"));
	}
	
	protected Control createContents(Composite parent) {
		//getWorkbench().getHelpSystem().setHelp(getControl(), IPDebugHelpContextIds.P_DEBUG_PREFERENCE_PAGE);
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);
		createSpacer(composite, 1);
		createViewSettingPreferences(composite);
		setValues();
		return composite;
	}
	protected void createViewSettingPreferences(Composite parent) {
		Composite group = createGroupComposite(parent, 1, false, PreferenceMessages.getString("PTPViewerPreferencesPage.iconName"));
		Composite compIcon = createComposite(group, 3);

		iconSpacingXField = new IconIntFieldEditor(PreferenceMessages.getString("PTPViewerPreferencesPage.icon_spacing_x"), 1, 10, compIcon);
		iconSpacingYField = new IconIntFieldEditor(PreferenceMessages.getString("PTPViewerPreferencesPage.icon_spacing_y"), 1, 10, compIcon);

		iconWidthField = new IconIntFieldEditor(PreferenceMessages.getString("PTPViewerPreferencesPage.icon_width"), 12, 100, compIcon);
		iconHeightField = new IconIntFieldEditor(PreferenceMessages.getString("PTPViewerPreferencesPage.icon_height"), 12, 100, compIcon);
		
		Composite compToolTip = createComposite(parent, 3);
		toolTipField = new IconIntFieldEditor(PreferenceMessages.getString("PTPViewerPreferencesPage.tooltip"), 1000, 10000, compToolTip);
	}
	public void performDefaults() { 
		IPreferenceStore store = getPreferenceStore();
		iconSpacingXField.setValue(store.getDefaultInt(IPTPUIConstants.VIEW_ICON_SPACING_X));
		iconSpacingYField.setValue(store.getDefaultInt(IPTPUIConstants.VIEW_ICON_SPACING_Y));
		iconWidthField.setValue(store.getDefaultInt(IPTPUIConstants.VIEW_ICON_WIDTH));
		iconHeightField.setValue(store.getDefaultInt(IPTPUIConstants.VIEW_ICON_HEIGHT));
		toolTipField.setValue((int)store.getDefaultLong(IPTPUIConstants.VIEW_TOOLTIP));
		super.performDefaults();
	}
	public boolean performOk() {
		storeValues();
		PTPUIPlugin.getDefault().savePluginPreferences();
		PTPUIPlugin.getDefault().firePreferencesListeners();
		return true;
	}
	
	protected void setValues() {
		IPreferenceStore store = getPreferenceStore();
		iconSpacingXField.setValue(store.getInt(IPTPUIConstants.VIEW_ICON_SPACING_X));
		iconSpacingYField.setValue(store.getInt(IPTPUIConstants.VIEW_ICON_SPACING_Y));
		iconWidthField.setValue(store.getInt(IPTPUIConstants.VIEW_ICON_WIDTH));
		iconHeightField.setValue(store.getInt(IPTPUIConstants.VIEW_ICON_HEIGHT));
		toolTipField.setValue((int)store.getLong(IPTPUIConstants.VIEW_TOOLTIP));
	}
	protected void storeValues() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(IPTPUIConstants.VIEW_ICON_SPACING_X, iconSpacingXField.getValue());
		store.setValue(IPTPUIConstants.VIEW_ICON_SPACING_Y, iconSpacingYField.getValue());
		store.setValue(IPTPUIConstants.VIEW_ICON_WIDTH, iconWidthField.getValue());
		store.setValue(IPTPUIConstants.VIEW_ICON_HEIGHT, iconHeightField.getValue());
		store.setValue(IPTPUIConstants.VIEW_TOOLTIP, (long)toolTipField.getValue());
	}
	
	public boolean isValid() {
		setErrorMessage(null);
		setMessage(null);
		if (!iconSpacingXField.isValid()) {
			setErrorMessage(iconSpacingXField.getErrorMessage());
			return false;
		}
		if (!iconSpacingYField.isValid()) {
			setErrorMessage(iconSpacingYField.getErrorMessage());
			return false;
		}
		if (!iconWidthField.isValid()) {
			setErrorMessage(iconWidthField.getErrorMessage());
			return false;
		}
		if (!iconHeightField.isValid()) {
			setErrorMessage(iconHeightField.getErrorMessage());
			return false;
		}
		if (!toolTipField.isValid()) {
			setErrorMessage(toolTipField.getErrorMessage());
			return false;
		}
		return true;
	}	
}
