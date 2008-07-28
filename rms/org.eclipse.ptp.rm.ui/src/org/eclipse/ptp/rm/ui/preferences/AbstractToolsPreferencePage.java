/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.ui.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ptp.rm.core.AbstractToolsPreferenceManager;
import org.eclipse.ptp.rm.core.rmsystem.AbstractToolRMConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/*
 * TODO: make this class expansible for preference pages for tools.
 * One might want to extend this class and add more fields.
 * - Make listener extensible
 * - Make data source extensible
 * - Make createContents extensible
 */
public abstract class AbstractToolsPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage {

	class WidgetListener extends PreferenceWidgetListener implements ModifyListener {


		public void modifyText(ModifyEvent evt) {
			if (! isEnabled()) return;

			Object source = evt.getSource();
			if(source == launchCmdText ||
					source == discoverCmdText ||
					source == periodicMonitorCmdText ||
					source == continuousMonitorCmdText ||
					source == periodicMonitorTimeSpinner ||
					source == remoteInstallPathText) {
				resetErrorMessages();
				dataSource.validate();
			} else {
				assert false;
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// Empty.
		}
	}

	class DataSource extends PreferenceDataSource {
		DataSource(PreferencePage page) {
			super(page);
		}

		String launchCmd = null;
		String discoverCmd = null;
		String periodicMonitorCmd = null;
		int periodicMonitorTime = 0;
		String continuousMonitorCmd = null;
		String remoteInstallPath = null;

		protected void copyFromFields() throws ValidationException {
			if (launchCmdText != null)
				launchCmd = extractText(launchCmdText);
			if (discoverCmdText != null)
				discoverCmd = extractText(discoverCmdText);
			if (periodicMonitorCmdText != null)
				periodicMonitorCmd = extractText(periodicMonitorCmdText);
			if (periodicMonitorTimeSpinner != null)
				periodicMonitorTime = periodicMonitorTimeSpinner.getSelection();
			if (continuousMonitorCmdText != null)
				continuousMonitorCmd = extractText(continuousMonitorCmdText);
			if (remoteInstallPathText != null)
				remoteInstallPath = extractText(remoteInstallPathText);
		}

		protected void validateLocal() throws ValidationException {
			if (launchCmdText != null && launchCmd == null) {
				throw new ValidationException("Launch command is missing");
			}
			if (discoverCmdText != null) {
				if (discoverCmd == null) {
					throw new ValidationException("Discover command is missing");
				}
				if (periodicMonitorTimeSpinner != null && periodicMonitorTime < 1) {
					throw new ValidationException("Time period must be an integer greater than 0");
				}
			}
		}

		protected void storeConfig() {
			Preferences config = getPreferences();
			if (launchCmdText != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_LAUNCH_CMD, toPreference(launchCmd));
			if (discoverCmdText != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_DISCOVER_CMD, toPreference(discoverCmd));
			if (periodicMonitorCmdText != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_CMD, toPreference(periodicMonitorCmd));
			if (periodicMonitorTimeSpinner != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_TIME, periodicMonitorTime);
			if (continuousMonitorCmdText != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_CONTINUOUS_MONITOR_CMD, toPreference(continuousMonitorCmd));
			if (remoteInstallPathText != null)
				config.setValue(prefix + AbstractToolsPreferenceManager.PREFS_REMOTE_INSTALL_PATH, toPreference(remoteInstallPath));
			savePreferences();
		}

		protected void loadConfig() {
			Preferences config = getPreferences();
			if (launchCmdText != null)
				launchCmd = fromPreference(config.getString(prefix + AbstractToolsPreferenceManager.PREFS_LAUNCH_CMD));
			if (discoverCmdText != null)
				discoverCmd = fromPreference(config.getString(prefix + AbstractToolsPreferenceManager.PREFS_DISCOVER_CMD));
			if (periodicMonitorCmdText != null)
				periodicMonitorCmd = fromPreference(config.getString(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_CMD));
			if (periodicMonitorTimeSpinner != null)
				periodicMonitorTime = config.getInt(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_TIME);
			if (continuousMonitorCmdText != null)
				continuousMonitorCmd = fromPreference(config.getString(prefix + AbstractToolsPreferenceManager.PREFS_CONTINUOUS_MONITOR_CMD));
			if (remoteInstallPathText != null)
				remoteInstallPath = fromPreference(config.getString(prefix + AbstractToolsPreferenceManager.PREFS_REMOTE_INSTALL_PATH));
		}

		protected void loadDefaultConfig() {
			Preferences config = getPreferences();
			if (launchCmdText != null)
				launchCmd = fromPreference(config.getDefaultString(prefix + AbstractToolsPreferenceManager.PREFS_LAUNCH_CMD));
			if (discoverCmdText != null)
				discoverCmd = fromPreference(config.getDefaultString(prefix + AbstractToolsPreferenceManager.PREFS_DISCOVER_CMD));
			if (periodicMonitorCmdText != null)
				periodicMonitorCmd = fromPreference(config.getDefaultString(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_CMD));
			if (periodicMonitorTimeSpinner != null)
				periodicMonitorTime = config.getDefaultInt(prefix + AbstractToolsPreferenceManager.PREFS_PERIODIC_MONITOR_TIME);
			if (continuousMonitorCmdText != null)
				continuousMonitorCmd = fromPreference(config.getDefaultString(prefix + AbstractToolsPreferenceManager.PREFS_CONTINUOUS_MONITOR_CMD));
			if (remoteInstallPathText != null)
				remoteInstallPath = fromPreference(config.getDefaultString(prefix + AbstractToolsPreferenceManager.PREFS_REMOTE_INSTALL_PATH));
		}

		protected void copyToFields() {
			if (launchCmdText != null)
				applyText(launchCmdText, launchCmd);
			if (discoverCmdText != null)
				applyText(discoverCmdText, discoverCmd);
			if (periodicMonitorCmdText != null)
				applyText(periodicMonitorCmdText, periodicMonitorCmd);
			if (periodicMonitorTimeSpinner != null)
				periodicMonitorTimeSpinner.setSelection(periodicMonitorTime);
			if (continuousMonitorCmdText != null)
				applyText(continuousMonitorCmdText, continuousMonitorCmd);
			if (remoteInstallPathText != null)
				applyText(remoteInstallPathText, remoteInstallPath);
		}
	}

	public static final String EMPTY_STRING = "";  //$NON-NLS-1$
	private Text launchCmdText = null;
	private Text discoverCmdText = null;
	private Text continuousMonitorCmdText = null;
	private Text periodicMonitorCmdText = null;
	private Spinner periodicMonitorTimeSpinner = null;
	private Text remoteInstallPathText = null;

	private int capabilities = AbstractToolRMConfiguration.NO_CAP_SET;
	private String prefix; // to get preferences

   public AbstractToolsPreferencePage(String prefix, int capabilities) {
		super();
    	this.capabilities = capabilities;
    	this.prefix = prefix;
	}

	public AbstractToolsPreferencePage(String prefix, int capabilities, String title, ImageDescriptor image) {
		super(title, image);
    	this.capabilities = capabilities;
    	this.prefix = prefix;
	}

	public AbstractToolsPreferencePage(String prefix, int capabilities, String title) {
		super(title);
    	this.capabilities = capabilities;
    	this.prefix = prefix;
	}

	@Override
	protected PreferenceDataSource createDataSource() {
		return new DataSource(this);
	}
	
	@Override
	protected PreferenceWidgetListener createListener() {
		return new WidgetListener();
	}
	
	private DataSource getDataSource() {
		return (DataSource) this.dataSource;
	}

	private WidgetListener getListener() {
		return (WidgetListener) this.listener;
	}

	public void init(IWorkbench workbench) {
		// Nothing to do.
	}

	protected Composite doCreateContents(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		contents.setLayout(layout);

		/*
		 * launch cmd
		 */
		if ((capabilities & AbstractToolRMConfiguration.CAP_LAUNCH) != 0) {
			Label label = new Label(contents, SWT.NONE);
			label.setText("Launch command:");

			launchCmdText = new Text(contents, SWT.SINGLE | SWT.BORDER);
			launchCmdText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
			launchCmdText.addModifyListener(getListener() );
		}

		/*
		 * discover cmd
		 */
		if ((capabilities & AbstractToolRMConfiguration.CAP_DISCOVER) != 0) {
			Label label = new Label(contents, SWT.NONE);
			label.setText("Discover command:");

			discoverCmdText = new Text(contents, SWT.SINGLE | SWT.BORDER);
			discoverCmdText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
			discoverCmdText.addModifyListener(getListener());
		}

		/*
		 * Periodic monitor cmd and time
		 */
		if ((capabilities & AbstractToolRMConfiguration.CAP_PERIODIC_MONITOR) != 0) {
			Label label = new Label(contents, SWT.NONE);
			label.setText("Monitor command:");

			periodicMonitorCmdText = new Text(contents, SWT.SINGLE | SWT.BORDER);
			periodicMonitorCmdText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));
			periodicMonitorCmdText.addModifyListener(getListener() );

			label = new Label(contents, SWT.NONE);
			label.setText("Period:");
			periodicMonitorTimeSpinner = new Spinner(contents, SWT.SINGLE | SWT.BORDER);
			periodicMonitorCmdText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false, 1, 1));
			periodicMonitorTimeSpinner.addModifyListener(getListener());
		}

		/*
		 * Continuous monitor cmd
		 */
		if ((capabilities & AbstractToolRMConfiguration.CAP_CONTINUOUS_MONITOR) != 0) {
			Label label = new Label(contents, SWT.NONE);
			label.setText("Continuous monitor command:");

			continuousMonitorCmdText = new Text(contents, SWT.SINGLE | SWT.BORDER);
			continuousMonitorCmdText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
			continuousMonitorCmdText.addModifyListener(getListener());
		}

		/*
		 * Installation path
		 */
		if ((capabilities & AbstractToolRMConfiguration.CAP_REMOTE_INSTALL_PATH) != 0) {
			Label label = new Label(contents, SWT.NONE);
			label.setText("Path to Open MPI Installation:");

			remoteInstallPathText = new Text(contents, SWT.SINGLE | SWT.BORDER);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			gd.widthHint = 60;
			remoteInstallPathText.setLayoutData(gd);
			remoteInstallPathText.addModifyListener(getListener());
		}
		return contents;
	}
}
