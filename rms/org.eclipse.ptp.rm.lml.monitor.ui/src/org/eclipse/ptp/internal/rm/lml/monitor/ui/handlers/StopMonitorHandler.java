/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.rm.lml.monitor.ui.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ptp.internal.rm.lml.monitor.ui.LMLMonitorUIPlugin;
import org.eclipse.ptp.internal.rm.lml.monitor.ui.messages.Messages;
import org.eclipse.ptp.rm.lml.monitor.core.IMonitorControl;
import org.eclipse.ui.handlers.HandlerUtil;

public class StopMonitorHandler extends AbstractHandler implements IHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			final List<IMonitorControl> monitors = new ArrayList<IMonitorControl>();
			for (Iterator<?> itr = ((IStructuredSelection) selection).iterator(); itr.hasNext();) {
				Object sel = itr.next();
				if (sel instanceof IMonitorControl) {
					monitors.add((IMonitorControl) sel);
				}
			}
			for (IMonitorControl monitor : monitors) {
				try {
					if (monitor.isActive()) {
						monitor.stop();
					}
				} catch (CoreException e) {
					ErrorDialog.openError(HandlerUtil.getActiveShell(event), Messages.StopMonitorHandler_Stop_Monitors,
							Messages.StopMonitorHandler_Unable_to_stop_monitor,
							new Status(IStatus.WARNING, LMLMonitorUIPlugin.getUniqueIdentifier(), e.getLocalizedMessage()));
				}
			}
		}
		return null;
	}
}
