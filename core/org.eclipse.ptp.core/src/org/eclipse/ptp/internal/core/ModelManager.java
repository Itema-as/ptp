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
package org.eclipse.ptp.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.ptp.core.AttributeConstants;
import org.eclipse.ptp.core.ControlSystemChoices;
import org.eclipse.ptp.core.IModelListener;
import org.eclipse.ptp.core.IModelManager;
import org.eclipse.ptp.core.INodeListener;
import org.eclipse.ptp.core.IPJob;
import org.eclipse.ptp.core.IPNode;
import org.eclipse.ptp.core.IPProcess;
import org.eclipse.ptp.core.IPUniverse;
import org.eclipse.ptp.core.IProcessListener;
import org.eclipse.ptp.core.MonitoringSystemChoices;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.core.events.IModelEvent;
import org.eclipse.ptp.core.events.IModelRuntimeNotifierEvent;
import org.eclipse.ptp.core.events.IModelSysChangedEvent;
import org.eclipse.ptp.core.events.INodeEvent;
import org.eclipse.ptp.core.events.IProcessEvent;
import org.eclipse.ptp.core.events.ModelRuntimeNotifierEvent;
import org.eclipse.ptp.core.events.ModelSysChangedEvent;
import org.eclipse.ptp.core.events.NodeEvent;
import org.eclipse.ptp.core.events.ProcessEvent;
import org.eclipse.ptp.core.util.BitList;
import org.eclipse.ptp.internal.core.elementcontrols.IPProcessControl;
import org.eclipse.ptp.internal.core.elementcontrols.IPUniverseControl;
import org.eclipse.ptp.rtsystem.IControlSystem;
import org.eclipse.ptp.rtsystem.IMonitoringSystem;
import org.eclipse.ptp.rtsystem.IRuntimeListener;
import org.eclipse.ptp.rtsystem.IRuntimeProxy;
import org.eclipse.ptp.rtsystem.JobRunConfiguration;
import org.eclipse.ptp.rtsystem.mpich2.MPICH2ControlSystem;
import org.eclipse.ptp.rtsystem.mpich2.MPICH2MonitoringSystem;
import org.eclipse.ptp.rtsystem.mpich2.MPICH2ProxyRuntimeClient;
import org.eclipse.ptp.rtsystem.ompi.OMPIControlSystem;
import org.eclipse.ptp.rtsystem.ompi.OMPIMonitoringSystem;
import org.eclipse.ptp.rtsystem.ompi.OMPIProxyRuntimeClient;
import org.eclipse.ptp.rtsystem.simulation.SimulationControlSystem;
import org.eclipse.ptp.rtsystem.simulation.SimulationMonitoringSystem;

public class ModelManager implements IModelManager, IRuntimeListener {
	private static int MAX_WAIT_DISCOVERY = 10000;		// maximum wait time for discovery (milliseconds)
	
	protected ListenerList modelListeners = new ListenerList();
	protected ListenerList nodeListeners = new ListenerList();
	protected ListenerList processListeners = new ListenerList();

	protected IPJob processRoot = null;
	protected IPUniverseControl universe = null;

	protected ILaunchConfiguration config = null;
	protected IControlSystem controlSystem = null;
	protected IMonitoringSystem monitoringSystem = null;
	protected IRuntimeProxy runtimeProxy = null;

	private int currentControlSystem = -1;
	private int currentMonitoringSystem = -1;
	private final int theMSChoiceID;
	private final int theCSChoiceID;
	private int numMachines = 1;
	private int[] numNodes = new int[]{255};
	
	private int jobID = 1;
	
	public void setPTPConfiguration(ILaunchConfiguration config) {
		this.config = config;
	}
	public ILaunchConfiguration getPTPConfiguration() {
		return config;
	}

	public ModelManager(int theMSChoiceID, int theCSChoiceID) {
		this.theMSChoiceID = theMSChoiceID;
		String MSChoice = MonitoringSystemChoices.getMSNameByID(theMSChoiceID);
		this.theCSChoiceID = theCSChoiceID;
		String CSChoice = ControlSystemChoices.getCSNameByID(theCSChoiceID);

		System.out.println("Your Control System Choice: '"+CSChoice+"'");
		System.out.println("Your Monitoring System Choice: '"+MSChoice+"'");
		
		if(ControlSystemChoices.getCSArrayIndexByID(theCSChoiceID) == -1 || MonitoringSystemChoices.getMSArrayIndexByID(theMSChoiceID) == -1) {
			throw new IllegalArgumentException("Illegal Control System and/or Monitoring System choices.");
		}
	}
	
	public ModelManager(int numMachines, int numNodes[]) {
		this(MonitoringSystemChoices.SIMULATED, ControlSystemChoices.SIMULATED);
		this.numMachines = numMachines;
		this.numNodes = (int[]) numNodes.clone();
	}
	
	public IControlSystem getControlSystem() {
		return controlSystem;
	}
	public int getControlSystemID() { 
		return currentControlSystem; 
	}
	public IMonitoringSystem getMonitoringSystem() {
		return monitoringSystem;
	}
	public int getMonitoringSystemID() { 
		return currentMonitoringSystem; 
	}
	public synchronized void refreshRuntimeSystems(IProgressMonitor monitor, boolean force) throws CoreException {
		int curMSID = getControlSystemID();
		int curCSID = getMonitoringSystemID();
		if(force || curMSID != theMSChoiceID || curCSID != theCSChoiceID) {
			if (monitor == null) {
				monitor = new NullProgressMonitor();
			}
			refreshRuntimeSystems(theCSChoiceID, theMSChoiceID, monitor);
		}
	}
	public synchronized void refreshRuntimeSystems(int controlSystemID, int monitoringSystemID, IProgressMonitor monitor) throws CoreException {
		System.err.println("refreshRuntimeSystems");
		try {
			monitor.beginTask("Refreshing runtime system...", 200);
			/*
			 * Shutdown runtime if it is already active
			 */
			System.out.println("SHUTTING DOWN CONTROL/MONITORING/PROXY systems where appropriate");
			if (controlSystem != null) {
				monitor.subTask("Shutting down control system...");
				controlSystem.shutdown();
				controlSystem = null;
				monitor.worked(10);
			}
			if (monitoringSystem != null) {
				monitor.subTask("Shutting down monitor system...");
				monitoringSystem.shutdown();
				monitoringSystem = null;
				monitor.worked(10);
			}
			if (runtimeProxy != null) {
				monitor.subTask("Shutting down runtime proxy...");
				runtimeProxy.shutdown();
				runtimeProxy = null;
				monitor.worked(10);
			}
			if (monitor.isCanceled()) {
				throw new CoreException(Status.CANCEL_STATUS);
			}
			monitor.worked(10);
	
			if(monitoringSystemID == MonitoringSystemChoices.SIMULATED && controlSystemID == ControlSystemChoices.SIMULATED) {
				/* load up the control and monitoring systems for the simulation */
				monitor.subTask("Starting simulation...");
				monitoringSystem = new SimulationMonitoringSystem(numMachines, numNodes);
				monitor.worked(10);
				controlSystem = new SimulationControlSystem();
				monitor.worked(10);
				runtimeProxy = null;
			}
			else if(monitoringSystemID == MonitoringSystemChoices.ORTE && controlSystemID == ControlSystemChoices.ORTE) {
				/* load up the control and monitoring systems for OMPI */
				monitor.subTask("Starting OMPI proxy runtime...");
				runtimeProxy = new OMPIProxyRuntimeClient(this);
				monitor.worked(10);
				
				if(!runtimeProxy.startup(monitor)) {
					System.err.println("Failed to start up the proxy runtime.");
					runtimeProxy = null;
					if (monitor.isCanceled()) {
						throw new CoreException(Status.CANCEL_STATUS);
					}
					//PTPCorePlugin.errorDialog("Failed to start OMPI proxy runtime",
					//	"There was an error starting the OMPI proxy runtime.  The path to 'ptp_orte_proxy' or 'orted' "+
					//	"may have been incorrect.  The 'orted' binary MUST be in your PATH to be found by 'ptp_orte_proxy'.  "+
					//	"Try checking the console log or error logs for more detailed information.\n\nDefaulting to "+
					//	"Simulation mode.  To change this, use the PTP preferences page.", null);
					
					/*
					int MSI = MonitoringSystemChoices.SIMULATED;
					int CSI = ControlSystemChoices.SIMULATED;
					Preferences p = PTPCorePlugin.getDefault().getPluginPreferences();
					p.setValue(PreferenceConstants.MONITORING_SYSTEM_SELECTION, MSI);
					p.setValue(PreferenceConstants.CONTROL_SYSTEM_SELECTION, CSI);
					PTPCorePlugin.getDefault().savePluginPreferences();
					if(!(monitoringSystem instanceof SimulationMonitoringSystem) || 
					   !(controlSystem instanceof SimulationControlSystem)) {
						refreshRuntimeSystems(ControlSystemChoices.SIMULATED, MonitoringSystemChoices.SIMULATED, monitor);
					}
					*/
					throw new CoreException(new Status(IStatus.ERROR, PTPCorePlugin.getUniqueIdentifier(), IStatus.ERROR, 
							"There was an error starting the OMPI proxy runtime.  The path to 'ptp_orte_proxy' or 'orted' "+
							"may have been incorrect.  The 'orted' binary MUST be in your PATH to be found by 'ptp_orte_proxy'.  "+
							"Try checking the console log or error logs for more detailed information.",
							null));
				}
				monitor.subTask("Starting OMPI monitoring system...");
				monitoringSystem = new OMPIMonitoringSystem((OMPIProxyRuntimeClient)runtimeProxy);
				monitor.worked(10);
				monitor.subTask("Starting OMPI control system...");
				controlSystem = new OMPIControlSystem((OMPIProxyRuntimeClient)runtimeProxy);
				monitor.worked(10);
			}
			else if(monitoringSystemID == MonitoringSystemChoices.MPICH2 && controlSystemID == ControlSystemChoices.MPICH2) {
				/* load up the control and monitoring systems for OMPI */
				monitor.subTask("Starting MPICH2 proxy runtime...");
				runtimeProxy = new MPICH2ProxyRuntimeClient(this);
				monitor.worked(10);
				
				if(!runtimeProxy.startup(monitor)) {
					System.err.println("Failed to start up the proxy runtime.");
					runtimeProxy = null;
					if (monitor.isCanceled()) {
						throw new CoreException(Status.CANCEL_STATUS);
					}
					throw new CoreException(new Status(IStatus.ERROR, PTPCorePlugin.getUniqueIdentifier(), IStatus.ERROR, 
							"There was an error starting the MPICH2 proxy runtime.  The path to 'ptp_mpich2_proxy' "+
							"may have been incorrect. Try checking the console log or error logs for more detailed information.",
							null));
				}
				monitor.subTask("Starting MPICH2 monitoring system...");
				monitoringSystem = new MPICH2MonitoringSystem((MPICH2ProxyRuntimeClient)runtimeProxy);
				monitor.worked(10);
				monitor.subTask("Starting MPICH2 control system...");
				controlSystem = new MPICH2ControlSystem((MPICH2ProxyRuntimeClient)runtimeProxy);
				monitor.worked(10);
			}
			else {
				throw new CoreException(new Status(IStatus.ERROR, PTPCorePlugin.getUniqueIdentifier(), IStatus.ERROR, "Invalid monitoring/control system selected.  Set using the PTP preferences page.", null));
			}
	
			universe = new PUniverse();
			monitor.subTask("Starting up monitor system...");
			monitoringSystem.startup();
			monitor.worked(10);
			monitor.subTask("Starting up control system...");
			controlSystem.startup();
			monitor.worked(10);
			try {
				monitor.subTask("Setup the monitoring system...");
				monitor.beginTask("", 1);
				monitoringSystem.addRuntimeListener(this);
				controlSystem.addRuntimeListener(this);		
				monitor.worked(1);
				monitoringSystem.initiateDiscovery();
				// Give discovery a chance to complete.  This is a fix to
				// bug 163289. Hopefully v2.0 design will find a better way.
				try {
					int count = 1;
					while (universe.getMachines().length < 1) {
						this.wait(500);
						count += 1;
						if (count*500 > MAX_WAIT_DISCOVERY) break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				monitor.done();
			} catch (CoreException e) {
				universe.removeChildren();
				throw e;
			}
			monitor.worked(10);
			fireEvent(new ModelSysChangedEvent(IModelSysChangedEvent.MONITORING_SYS_CHANGED, null));
			currentControlSystem = controlSystemID;
			currentMonitoringSystem = monitoringSystemID;
		} finally {
			if (!monitor.isCanceled())
				monitor.done();
		}
	}
	
	public void runtimeNodeGeneralChange(String[] keys, String[] values) {
		boolean newEntity = false;
		PMachine curmachine = null;
		PNode curnode = null;
		
		boolean one_node_changed = false;
		PNode the_one_changed_node = null;
		
		System.out.println("ModelManager.runtimeNodeGeneralName - #keys = "+keys.length+", #values = "+values.length);
		for(int i=0; i<keys.length; i++) {
			String key = keys[i];
			String value = values[i];
			if (key.equals(AttributeConstants.ATTRIB_MACHINEID)) {
				/* ok, so we're switching to this new machine.  Let's find it. */
				curmachine = (PMachine)universe.findMachineById(value);
				if(curmachine == null) {
					System.out.println("\t\tUnknown machine ID ("+value+"), adding to the model.");
					curmachine = new PMachine(universe, AttributeConstants.ATTRIB_MACHINE_NAME_PREFIX + value, value);
					universe.addChild(curmachine);
					newEntity = true;
				}
			} else if (curmachine != null && key.equals(AttributeConstants.ATTRIB_NODE_NUMBER)) {
				/* ok so we've got a machine that's not null, and we think we have a node
				 * number to look for in that machine.  So let's find it!
				 */
				curnode = (PNode)curmachine.findNodeByName(AttributeConstants.ATTRIB_NODE_NAME_PREFIX + value);
				if(curnode == null) {
					System.out.println("\t\tUnknown node number ("+value+"), adding to the model.");
					curnode = new PNode(curmachine, AttributeConstants.ATTRIB_NODE_NAME_PREFIX + value, value);
					curmachine.addChild(curnode);
					newEntity = true;
				}
				if (the_one_changed_node == null) {
					the_one_changed_node = curnode;
					one_node_changed = true;
				} else {
					one_node_changed = false;
				}
			} else if (curmachine != null && curnode != null) {
				curnode.setAttribute(key, value);
			} else {
				System.err.println("\t!!! ERROR: Received key/value attribute pair but have no associated machine/node to assign it to.");
			}
		}
		
		if (newEntity) {
			fireEvent(new ModelSysChangedEvent(IModelSysChangedEvent.MAJOR_SYS_CHANGED, null));
		}
		if (one_node_changed && the_one_changed_node != null) {
			fireEvent(new NodeEvent(the_one_changed_node, INodeEvent.STATUS_UPDATE_TYPE, null));
		} else {
			/* ok more than 1 node changed, too complex let's just let them know to do a refresh */
			fireEvent(new ModelSysChangedEvent(IModelSysChangedEvent.SYS_STATUS_CHANGED, null));
		}
	}

	public void runtimeProcessOutput(String ne, String output) {
		IPProcess p = universe.findProcessByName(ne);
		if (p != null) {
			p.addOutput(output);
			fireEvent(new ProcessEvent(p, IProcessEvent.ADD_OUTPUT_TYPE, output + "\n"));
		}
	}
	
	public void runtimeJobExited(String ne) {
		// not used
	}

	public void runtimeJobStateChanged(String nejob, String state) {
		System.out.println("*********** JOB STATE CHANGE: "+state+" (job = "+nejob+")");
		IPJob job = universe.findJobByName(nejob);
		if (job != null) {
			IPProcess[] procs = job.getProcesses();
			if (procs != null) {
				for (int i = 0; i < procs.length; i++) {
					procs[i].setStatus(state);
					fireEvent(new ProcessEvent(procs[i], IProcessEvent.STATUS_CHANGE_TYPE, procs[i].getStatus()));
					if (procs[i].getStatus().equals(IPProcess.EXITED)) {
						IPNode node = procs[i].getNode();
						//FIXME why node can be null???
						if (node != null) {
							fireEvent(new NodeEvent(node, INodeEvent.STATUS_UPDATE_TYPE, null));
						}
					}
				}
			}
			if (state.equals("running")) {
				fireEvent(new ModelRuntimeNotifierEvent(job.getIDString(), IModelRuntimeNotifierEvent.TYPE_JOB, IModelRuntimeNotifierEvent.RUNNING));
			} else if (state.equals("exited")) {
				fireEvent(new ModelRuntimeNotifierEvent(job.getIDString(), IModelRuntimeNotifierEvent.TYPE_JOB, IModelRuntimeNotifierEvent.STOPPED));
			} else if (state.equals("starting")) {
				fireEvent(new ModelRuntimeNotifierEvent(job.getIDString(), IModelRuntimeNotifierEvent.TYPE_JOB, IModelRuntimeNotifierEvent.STARTED));
			}
		}
	}

	public void runtimeNewJob(String ne) {
		// not used
	}

	/* 
	 * Update model when process attributes change
	 */
	public void runtimeProcAttrChange(String nejob, BitList cprocs, String kv, int[] dprocs, String[] kvs) {
		System.out.println("*********** PROC ATTRIBUTE CHANGE: (job = "+nejob+")");
		IPJob job = universe.findJobByName(nejob);
		if (job != null) {
			/* 
			 * First deal with common processes
			 */
			
			/*
			 * Now deal with different processes
			 */
			for (int i = 0; i < dprocs.length; i++) {
				IPProcess proc = job.findProcessByName(nejob+"_process"+dprocs[i]);
				String[] attr = kvs[i].split("=");
				if (attr.length == 2 && proc != null) {
					if (attr[0].equals(AttributeConstants.ATTRIB_PROCESS_PID)) {
						System.err.println("setting pid[" + proc.getName() + "]=" + attr[1]);
						proc.setPid(attr[1]);
					} else if (attr[0].equals(AttributeConstants.ATTRIB_PROCESS_NODE_NAME)) {
						if (attr[1].equals("localhost")) {
							IPNode node = universe.getMachines()[0].findNodeByName(AttributeConstants.ATTRIB_NODE_NAME_PREFIX + "0");
							proc.setNode(node);
						} else {
							IPNode[] nodes = universe.getMachines()[0].getNodes();
							for (int j = 0; j < nodes.length; j++) {
								IPNode node = nodes[j];
								if (node.getAttribute(AttributeConstants.ATTRIB_NODE_NAME).equals(attr[1])) {
									System.err.println("setting node[" + proc.getName() + "]=" + attr[1] + "(" + node.getNodeNumber() + ")");
									proc.setNode(node);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	public void shutdown() {
		modelListeners.clear();
		modelListeners = null;
		if(monitoringSystem != null)
			monitoringSystem.shutdown();
		if(controlSystem != null)
			controlSystem.shutdown();
		if (runtimeProxy != null)
			runtimeProxy.shutdown();
		currentControlSystem = -1;
		currentMonitoringSystem = -1;
	}
	public void addNodeListener(INodeListener listener) {
		nodeListeners.add(listener);
	}
	public void removeNodeListener(INodeListener listener) {
		nodeListeners.remove(listener);
	}
	public void addProcessListener(IProcessListener listener) {
		processListeners.add(listener);
	}
	public void removeProcessListener(IProcessListener listener) {
		processListeners.remove(listener);
	}
	public void addModelListener(IModelListener listener) {
		modelListeners.add(listener);
	}
	public void removeModelListener(IModelListener listener) {
		modelListeners.remove(listener);
	}
	public void fireEvent(final IProcessEvent event) {
        Object[] array = processListeners.getListeners();
        for (int i = 0; i < array.length; i++) {
            final IProcessListener l = (IProcessListener) array[i];
            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    l.processEvent(event);
                }
            });
        }
	}
	
	public void fireEvent(final INodeEvent event) {
        Object[] array = nodeListeners.getListeners();
        for (int i = 0; i < array.length; i++) {
            final INodeListener l = (INodeListener) array[i];
            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    l.nodeEvent(event);
                }
            });
        }
	}
	
	public void fireEvent(final IModelEvent event) {
        Object[] array = modelListeners.getListeners();
        for (int i = 0; i < array.length; i++) {
            final IModelListener l = (IModelListener) array[i];
            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    l.modelEvent(event);
                }
            });
        }
	}

	public void abortJob(String jobName) throws CoreException {
		/* we have a job name, so let's find it in the Universe - if it exists */
		IPJob j = getUniverse().findJobByName(jobName);
		if(j == null) {
			System.err.println("ERROR: tried to delete a job that was not found '"+jobName+"'");
			return;
		}
		try {
			controlSystem.terminateJob(j);
		} catch(CoreException e) {
			PTPCorePlugin.errorDialog("Fatal PTP Control System Error", "The PTP Control System is down.", null);
			return;
		}

		System.err.println("aborted");
		fireEvent(new ModelRuntimeNotifierEvent(j.getIDString(), IModelRuntimeNotifierEvent.TYPE_JOB, IModelRuntimeNotifierEvent.ABORTED));
	}
	
	public IPJob run(final ILaunch launch, final JobRunConfiguration jobRunConfig, IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating the job...");
		
		IPJob job = newJob(jobRunConfig.getNumberOfProcesses(), jobRunConfig.isDebug(), monitor);
		System.out.println("ModelManager.run() - new JobID = "+job.getJobNumberInt());

		controlSystem.run(job.getJobNumberInt(), jobRunConfig);
		
		return job;
	}
	
	protected void clearUsedMemory() {
		System.out.println("********** clearUsedMemory");
		Runtime rt = Runtime.getRuntime();
		long isFree = rt.freeMemory();
		long wasFree;
		do {
			wasFree = isFree;
			rt.gc();
			isFree = rt.freeMemory();
		} while (isFree > wasFree);
		rt.runFinalization();
	}
	public IPUniverse getUniverse() {
		return universe;
	}
	private int newJobID() {
		return this.jobID++;
	}
	private IPJob newJob(int numProcesses, boolean debug, IProgressMonitor monitor) throws CoreException {
		int jobID = newJobID();
		String jobName = "job"+jobID;
		String jobOwner = "";
		System.out.println("MODEL MANAGER: newJob("+jobID+")");
		PJob job = new PJob(universe, jobName, "" + (PJob.BASE_OFFSET + jobID) + "", jobID);		
		if (debug)
			job.setDebug();
		
		universe.addChild(job);
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		
		monitor.beginTask("", numProcesses);
		monitor.setTaskName("Creating processes....");
		/* we know that we succeeded, so we can create this many procs in the job.  we just
		 * need to run getProcsStatusForNewJob() to fill in the status later
		 */
		IPNode[] nodes = universe.getMachines()[0].getNodes();
		if (nodes.length > 0) {
			jobOwner = (String) nodes[0].getAttribute(AttributeConstants.ATTRIB_NODE_USER);
		}
		PProcess.deleteOutputFiles(jobName, jobOwner);
		for (int i = 0; i < numProcesses; i++) {		
			IPProcessControl proc = new PProcess(job, jobOwner, jobName+"_process"+i, "" + i + "", "0", i, IPProcess.STARTING, "", "");
			job.addChild(proc);			
		}

		/*
		 * This is needed for debug jobs because the runtimeJobStateChanged event is
		 * not generated (the debugger manages the process/job state) and as a consequence the
		 * UI JobManager listener is never called.
		 */
		if (debug) {
			fireEvent(new ModelRuntimeNotifierEvent(job.getIDString(), IModelRuntimeNotifierEvent.TYPE_JOB, IModelRuntimeNotifierEvent.STARTED));
		}
		return job;
	}
}
