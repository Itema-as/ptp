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
import org.eclipse.ptp.debug.external.gdb.mi.output.MIConst;
import org.eclipse.ptp.debug.external.gdb.mi.output.MIExecAsyncOutput;
import org.eclipse.ptp.debug.external.gdb.mi.output.MIFrame;
import org.eclipse.ptp.debug.external.gdb.mi.output.MIResult;
import org.eclipse.ptp.debug.external.gdb.mi.output.MIResultRecord;
import org.eclipse.ptp.debug.external.gdb.mi.output.MITuple;
import org.eclipse.ptp.debug.external.gdb.mi.output.MIValue;




/**
 *
 *  *stopped,reason="end-stepping-range",thread-id="0",frame={addr="0x08048538",func="main",args=[{name="argc",value="1"},{name="argv",value="0xbffff18c"}],file="hello.c",line="13"}
 */
public class MISteppingRangeEvent extends MIStoppedEvent {

	public MISteppingRangeEvent(MISession source, MIExecAsyncOutput async) {
		super(source, async);
		parse();
	}

	public MISteppingRangeEvent(MISession source, MIResultRecord record) {
		super(source, record);
		parse();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("thread-id=").append(getThreadId()).append('\n'); //$NON-NLS-1$
		MIFrame f = getFrame();
		if (f != null) {
			buffer.append(getFrame().toString());
		}
		return buffer.toString();
	}

	void parse () {
		MIResult[] results = null;
		MIExecAsyncOutput exec = getMIExecAsyncOutput();
		MIResultRecord rr = getMIResultRecord();
		if (exec != null) {
			results = exec.getMIResults();
		} else if (rr != null) {
			results = rr.getMIResults();
		}
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				String var = results[i].getVariable();
				MIValue value = results[i].getMIValue();

				if (var.equals("thread-id")) { //$NON-NLS-1$
					if (value instanceof MIConst) {
						String str = ((MIConst)value).getString();
						try {
							int id = Integer.parseInt(str.trim());
							setThreadId(id);
						} catch (NumberFormatException e) {
						}
					}
				} else if (var.equals("frame")) { //$NON-NLS-1$
					if (value instanceof MITuple) {
						MIFrame f = new MIFrame((MITuple)value);
						setFrame(f);
					}
				}
			}
		}
	}
}
