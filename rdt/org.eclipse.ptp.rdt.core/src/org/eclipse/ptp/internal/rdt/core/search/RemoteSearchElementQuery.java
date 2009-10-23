/*******************************************************************************
 * Copyright (c) 2006, 2008 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 * Markus Schorn (Wind River Systems)
 * IBM Corporation
 *******************************************************************************/

package org.eclipse.ptp.internal.rdt.core.search;

import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexLocationConverter;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ISourceReference;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ptp.internal.rdt.core.index.IndexQueries;

public class RemoteSearchElementQuery extends RemoteSearchQuery {
	private static final long serialVersionUID = 1L;

	private ISourceReference fElement;

	private String fPath;
	
	public RemoteSearchElementQuery(ICElement[] scope, ISourceReference element, String path, int flags) {
		super(scope, flags | IIndex.SEARCH_ACROSS_LANGUAGE_BOUNDARIES);
		fElement = element;
		fPath = path;
	}

	public IStatus runWithIndex(IIndex index, IIndexLocationConverter converter, IProgressMonitor monitor) throws OperationCanceledException {
		fConverter = converter;
		try {
			if (fElement instanceof ICElement) {
				IBinding binding= IndexQueries.elementToBinding(index, (ICElement) fElement, fPath);
				if (binding != null) {
					createMatches(index, binding);
				}
			}
			return Status.OK_STATUS;
		} catch (CoreException e) {
			return e.getStatus();
		}
	}
	
	public ISourceReference getSourceReference() {
		return fElement;
	}
}
