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

/*
 * Created on 1/12/2004
 *
 */
package org.eclipse.ptp.debug;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

/**
 *
 */
public class CDTAttachDebuger extends AbstractAttachDebugger {
	public final String CDT_GDB_DEBUGGER_ID = "org.eclipse.cdt.debug.mi.core.CDebugger";

	public final String CDT_ATTACH_TYPE_ID = "org.eclipse.cdt.launch.localAttachCLaunch";

	public CDTAttachDebuger(String pidText, String configName) {
		super(pidText, configName);
	}

	protected ILaunchConfiguration createDebugConfiguration(IFile exeFile)
			throws CoreException {
		ILaunchManager manager = getLaunchManager();
		// ILaunchConfigurationType type =
		// manager.getLaunchConfigurationType(ICDTLaunchConfigurationConstants.ID_LAUNCH_C_APP);
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(CDT_ATTACH_TYPE_ID);

		ILaunchConfigurationWorkingCopy wc = type.newInstance(exeFile
				.getProject(), debugConfigName);
		wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME,
				exeFile.getProject().getName());
		wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
				exeFile.getName());

		wc.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,
				ICDTLaunchConfigurationConstants.DEBUGGER_MODE_ATTACH);
		wc.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN,
				false);
		wc.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID,
				CDT_GDB_DEBUGGER_ID);
		wc.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_ATTACH_PROCESS_ID,
				getPid());
		wc.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
				(String) null);
		// wc.setAttribute( ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID,
		// CDebugUIPlugin.getDefaultSourceLocatorID());
		return wc.doSave();
	}

	public void attachDebugger(ILaunchConfiguration ptpConfig)
			throws CoreException {
		IFile exeFile = findExeFile(ptpConfig);
		ILaunchConfiguration config = createDebugConfiguration(exeFile);
		config.launch(ILaunchManager.DEBUG_MODE, null);
	}

	protected boolean createLaunch(ILaunchConfiguration config)
			throws CoreException {
		return false;
	}

	/*
	 * IPersistableSourceLocator locator =
	 * CDebugUIPlugin.createDefaultSourceLocator(); try {
	 * locator.initializeDefaults( configuration ); if ( locator instanceof
	 * IAdaptable ) { ICSourceLocator clocator =
	 * (ICSourceLocator)((IAdaptable)locator).getAdapter( ICSourceLocator.class );
	 * if ( clocator != null && getProject() != null && getProject().equals(
	 * getProjectFromLaunchConfiguration( configuration ) ) ) {
	 * clocator.setSourceLocations( getSourceLocations() );
	 * clocator.setSearchForDuplicateFiles( searchForDuplicateFiles() ); } }
	 * configuration.setAttribute(
	 * ILaunchConfiguration.ATTR_SOURCE_LOCATOR_MEMENTO, locator.getMemento() ); }
	 * catch( CoreException e ) { } }
	 */

	/*
	 * public void attachDebugger(ILaunchConfiguration ptpConfig) throws
	 * CoreException { ICDISession dsession = null; IProcess debuggerProcess =
	 * null; try { IFile exeFile = findExeFile(ptpConfig); ILaunchConfiguration
	 * config = createDebugConfiguration(exeFile); if (createLaunch(config)) {
	 * int pid =
	 * config.getAttribute(ICDTLaunchConfigurationConstants.ATTR_ATTACH_PROCESS_ID,
	 * -1); ICDebugConfiguration debugConfig = getDebugConfig(config); dsession =
	 * debugConfig.getDebugger().createAttachSession(config, exeFile, pid);
	 * DebugPlugin.getDefault().getLaunchManager().addLaunch(debugLaunch);
	 * //Process debugger = dsession.getSessionProcess(); //if (debugger !=
	 * null) { // debuggerProcess = DebugPlugin.newProcess(debugLaunch,
	 * debugger, renderDebuggerProcessLabel()); //
	 * debugLaunch.removeProcess(debuggerProcess); //}
	 * CDebugModel.newAttachDebugTarget(debugLaunch,
	 * dsession.getCurrentTarget(), renderTargetLabel(debugConfig.getName()),
	 * debuggerProcess, exeFile); } } catch (CDIException e) { if (dsession !=
	 * null) { try { dsession.terminate(); } catch (CDIException ex) { // ignore } }
	 * Status status = new Status(IStatus.ERROR,
	 * PTPCorePlugin.getUniqueIdentifier(), IStatus.INFO, e.getMessage(), e);
	 * throw new CoreException(status); } } protected boolean
	 * createLaunch(ILaunchConfiguration config) throws CoreException { int pid =
	 * config.getAttribute(ICDTLaunchConfigurationConstants.ATTR_ATTACH_PROCESS_ID,
	 * -1); if (pid == -1) { debugLaunch =
	 * config.launch(ILaunchManager.DEBUG_MODE, null); return false; }
	 * debugLaunch = new Launch(config, ILaunchManager.DEBUG_MODE, null); return
	 * true; } protected ICDebugConfiguration
	 * getDebugConfig(ILaunchConfiguration config) throws CoreException {
	 * ICDebugConfiguration dbgCfg = null; try { dbgCfg =
	 * CDebugCorePlugin.getDefault().getDebugConfiguration(config.getAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID,
	 * "")); } catch (CoreException e) { IStatus status = new
	 * Status(IStatus.ERROR, PTPCorePlugin.getUniqueIdentifier(),
	 * ICDTLaunchConfigurationConstants.ERR_DEBUGGER_NOT_INSTALLED,
	 * e.getMessage(), e); IStatusHandler handler =
	 * DebugPlugin.getDefault().getStatusHandler(status); if (handler != null) {
	 * Object result = handler.handleStatus(status, this); if (result instanceof
	 * String) { // this could return the new debugger id to use? } } throw e; }
	 * return dbgCfg; }
	 */
}
