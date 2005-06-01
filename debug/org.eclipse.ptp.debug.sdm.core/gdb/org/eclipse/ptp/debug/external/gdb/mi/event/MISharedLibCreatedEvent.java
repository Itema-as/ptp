/*******************************************************************************
 * Copyright (c) 2000, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.debug.external.gdb.mi.event;

import org.eclipse.ptp.debug.external.gdb.mi.MISession;



/**
 *
 */
public class MISharedLibCreatedEvent extends MICreatedEvent {

	String filename;

	public MISharedLibCreatedEvent(MISession source, String name) {
		this(source, 0, name);
	}

	public MISharedLibCreatedEvent(MISession source, int id, String name) {
		super(source, id);
		filename = name;
	}

	public String getName() {
		return filename;
	}

}
