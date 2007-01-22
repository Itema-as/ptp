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
package org.eclipse.ptp.ui.managers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.ptp.core.IModelPresentation;
import org.eclipse.ptp.core.IPJob;
import org.eclipse.ptp.core.IPUniverse;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.ui.IManager;
import org.eclipse.ptp.ui.PTPUIPlugin;
import org.eclipse.ptp.ui.listeners.IJobChangedListener;
import org.eclipse.ptp.ui.listeners.ISetListener;
import org.eclipse.ptp.ui.model.ElementSet;
import org.eclipse.ptp.ui.model.IElement;
import org.eclipse.ptp.ui.model.IElementHandler;
import org.eclipse.ptp.ui.model.IElementSet;
import org.eclipse.ui.PlatformUI;

/**
 * @author Clement chu
 * 
 */
public abstract class AbstractUIManager implements IManager {
	protected IModelPresentation modelPresentation = null;
	protected String cur_set_id = EMPTY_ID;
	protected ListenerList setListeners = new ListenerList();
	protected ListenerList jListeners = new ListenerList();

	/** Constructor 
	 * 
	 */
	public AbstractUIManager() {
		modelPresentation = PTPCorePlugin.getDefault().getModelPresentation();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#shutdown()
	 */
	public void shutdown() {
		setListeners.clear();
		setListeners = null;
		jListeners.clear();
		jListeners = null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#addSetListener(org.eclipse.ptp.ui.listeners.ISetListener)
	 */
	public void addSetListener(ISetListener setListener) {
		setListeners.add(setListener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeSetListener(org.eclipse.ptp.ui.listeners.ISetListener)
	 */
	public void removeSetListener(ISetListener setListener) {
		setListeners.remove(setListener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#fireEvent(int, org.eclipse.ptp.ui.model.IElement[], org.eclipse.ptp.ui.model.IElementSet, org.eclipse.ptp.ui.model.IElementSet)
	 */
	public void fireSetEvent(final int eventType, final IElement[] elements, final IElementSet cur_set, final IElementSet pre_set) {
        Object[] array = setListeners.getListeners();
        for (int i = 0; i < array.length; i++) {
            final ISetListener setListener = (ISetListener) array[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					switch (eventType) {
					case CREATE_SET_TYPE:
						setListener.createSetEvent(cur_set, elements);
						break;
					case DELETE_SET_TYPE:
						setListener.deleteSetEvent(cur_set);
						break;
					case CHANGE_SET_TYPE:
						setListener.changeSetEvent(cur_set, pre_set);
						break;
					case ADD_ELEMENT_TYPE:
						setListener.addElementsEvent(cur_set, elements);
						break;
					case REMOVE_ELEMENT_TYPE:
						setListener.removeElementsEvent(cur_set, elements);
						break;
					}
				}
			});
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#addJobListener(org.eclipse.ptp.ui.listeners.IJobListener)
	 */
	public void addJobChangedListener(IJobChangedListener jobListener) {
		jListeners.add(jobListener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeJobListener(org.eclipse.ptp.ui.listeners.IJobListener)
	 */
	public void removeJobChangedListener(IJobChangedListener jobListener) {
		jListeners.remove(jobListener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#fireJobListener(int, java.lang.String, java.lang.String)
	 */
	public void fireJobChangedEvent(final int type, final String cur_job_id, final String pre_job_id) {
        Object[] array = jListeners.getListeners();
        for (int i = 0; i<array.length; i++) {
			final IJobChangedListener listener = (IJobChangedListener)array[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					listener.jobChangedEvent(type, cur_job_id, pre_job_id);
				}
			});
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#addToSet(org.eclipse.ptp.ui.model.IElement[], org.eclipse.ptp.ui.model.IElementSet)
	 */
	public void addToSet(IElement[] elements, IElementSet set) {
		for (int i = 0; i < elements.length; i++) {
			set.add(elements[i]);
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#addToSet(org.eclipse.ptp.ui.model.IElement[], java.lang.String, org.eclipse.ptp.ui.model.IElementHandler)
	 */
	public void addToSet(IElement[] elements, String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		addToSet(elements, set);
		updateMatchElementSets(set, elementHandler);
		fireSetEvent(ADD_ELEMENT_TYPE, elements, set, null);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#createSet(org.eclipse.ptp.ui.model.IElement[], java.lang.String, java.lang.String, org.eclipse.ptp.ui.model.IElementHandler)
	 */
	public String createSet(IElement[] elements, String setID, String setName, IElementHandler elementHandler) {
		IElementSet set = new ElementSet(elementHandler, setID, setName);
		addToSet(elements, set);
		elementHandler.add(set);
		updateMatchElementSets(set, elementHandler);
		fireSetEvent(CREATE_SET_TYPE, elements, set, null);
		return set.getID();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeSet(java.lang.String, org.eclipse.ptp.ui.model.IElementHandler)
	 */
	public void removeSet(String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		String[] sets = set.getMatchSets();
		for (int i = 0; i < sets.length; i++) {
			elementHandler.getSet(sets[i]).removeMatchSet(setID);
		}
		elementHandler.remove(setID);
		fireSetEvent(DELETE_SET_TYPE, null, set, null);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeFromSet(org.eclipse.ptp.ui.model.IElement[], java.lang.String, org.eclipse.ptp.ui.model.IElementHandler)
	 */
	public void removeFromSet(IElement[] elements, String setID, IElementHandler elementHandler) {
		IElementSet set = elementHandler.getSet(setID);
		for (int i = 0; i < elements.length; i++) {
			set.remove(elements[i]);
		}
		updateMatchElementSets(set, elementHandler);
		fireSetEvent(REMOVE_ELEMENT_TYPE, elements, set, null);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#updateMatchElementSets(org.eclipse.ptp.ui.model.IElementSet, org.eclipse.ptp.ui.model.IElementHandler)
	 */
	public void updateMatchElementSets(IElementSet targetSet, IElementHandler elementHandler) {
		IElementSet[] sets = elementHandler.getSortedSets();
		for (int i = 0; i < sets.length; i++) {
			if (sets[i].getID().equals(targetSet.getID()))
				continue;
			IElement[] elements = sets[i].getElements();
			for (int j = 0; j < elements.length; j++) {
				if (targetSet.contains(elements[j].getID())) {
					targetSet.addMatchSet(sets[i].getID());
					sets[i].addMatchSet(targetSet.getID());
					break;
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#isNoJob(java.lang.String)
	 */
	public boolean isNoJob(String jid) {
		return (jid == null || jid.length() == 0);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#isJobStop(java.lang.String)
	 */
	public boolean isJobStop(String job_id) {
		if (isNoJob(job_id))
			return true;
		IPJob job = findJobById(job_id);
		return (job == null || job.isAllStop());
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#findJob(java.lang.String)
	 */
	public IPJob findJob(String job_name) {
		IPUniverse universe = modelPresentation.getUniverse();
		if (universe == null)
			return null;
		return universe.findJobByName(job_name);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#findJobById(java.lang.String)
	 */
	public IPJob findJobById(String job_id) {
		if (job_id == null) {
			return null;
		}
		
		IPUniverse universe = modelPresentation.getUniverse();
		if (universe == null)
			return null;
		// IPElement element = universe.findChild(job_id);
		IPJob job = universe.findJobById(job_id);
		return job;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeJob(org.eclipse.ptp.core.IPJob)
	 */
	public void removeJob(IPJob job) {
		IPUniverse universe = modelPresentation.getUniverse();
		if (universe != null) {
			//remove launch from debug view
			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunch[] launches = launchManager.getLaunches();
			for (int i=0; i<launches.length; i++) {
				if (launches[i].getAttribute("JOB_ID").equals(job.getIDString())) {
					launchManager.removeLaunch(launches[i]);
				}
			}
			universe.deleteJob(job);
			fireJobChangedEvent(IJobChangedListener.REMOVED, null, job.getIDString());
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#removeAllStoppedJobs()
	 */
	public void removeAllStoppedJobs() {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor pmonitor) throws InvocationTargetException {
				if (pmonitor == null)
					pmonitor = new NullProgressMonitor();
				try {
					IPUniverse universe = modelPresentation.getUniverse();
					if (universe != null) {
						IPJob[] jobs = universe.getJobs();
						pmonitor.beginTask("Removing stopped jobs...", jobs.length);
						for (int i = 0; i < jobs.length; i++) {
							if (pmonitor.isCanceled())
								throw new InvocationTargetException(new Exception("Cancelled by user"));
							if (jobs[i].isAllStop())
								removeJob(jobs[i]);
							pmonitor.worked(1);
						}
					}
				} finally {
					pmonitor.done();
				}
			}
		};
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
		} catch (InterruptedException e) {
			PTPUIPlugin.log(e);
		} catch (InvocationTargetException e1) {
			PTPUIPlugin.log(e1);
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#hasStoppedJob()
	 */
	public boolean hasStoppedJob() {
		IPUniverse universe = modelPresentation.getUniverse();
		if (universe == null)
			return false;
		
		IPJob[] jobs = universe.getJobs();
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i].isAllStop())
				return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.ui.IManager#getStatus(org.eclipse.ptp.ui.model.IElement)
	 */
	public int getStatus(IElement element) {
		return getStatus(element.getID());
	}		
}
