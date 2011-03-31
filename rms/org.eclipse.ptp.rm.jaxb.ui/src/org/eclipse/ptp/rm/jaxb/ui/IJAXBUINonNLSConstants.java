/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.ui;

import org.eclipse.ptp.rm.jaxb.core.IJAXBNonNLSConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public interface IJAXBUINonNLSConstants extends IJAXBNonNLSConstants {

	int DEFAULT = UNDEFINED;

	Color DKBL = Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
	Color DKMG = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
	Color DKRD = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

	String COURIER = "Courier";//$NON-NLS-1$

	String COLUMN_NAME = "Name";//$NON-NLS-1$
	String COLUMN_VALUE = "Value";//$NON-NLS-1$
	String COLUMN_DEFAULT = "Default";//$NON-NLS-1$
	String COLUMN_DESC = "Description";//$NON-NLS-1$
	String COLUMN_STATUS = "Status";//$NON-NLS-1$
	String COLUMN_TOOLTIP = "Tooltip";//$NON-NLS-1$
	String COLUMN_TYPE = "Type";//$NON-NLS-1$

	String TABLE = "table";//$NON-NLS-1$
	String TREE = "tree";//$NON-NLS-1$
	String LABEL = "label";//$NON-NLS-1$
	String TEXT = "text";//$NON-NLS-1$
	String CHECKBOX = "checkbox";//$NON-NLS-1$
	String SPINNER = "spinner";//$NON-NLS-1$
	String COMBO = "combo";//$NON-NLS-1$
	String RADIOBUTTON = "radioButton";//$NON-NLS-1$
	String BROWSELOCAL = "browseLocal";//$NON-NLS-1$
	String BROWSEREMOTE = "browseRemote";//$NON-NLS-1$

	// SWT
	String ALL = "SWT.ALL";//$NON-NLS-1$
	String ARROW = "SWT.ARROW";//$NON-NLS-1$
	String BACKGROUND = "SWT.BACKGROUND";//$NON-NLS-1$
	String BALLOON = "SWT.BALLOON";//$NON-NLS-1$
	String BAR = "SWT.BAR";//$NON-NLS-1$
	String BEGINNING = "SWT.BEGINNING";//$NON-NLS-1$
	String BORDER = "SWT.BORDER";//$NON-NLS-1$
	String BORDER_DASH = "SWT.BORDER_DASH";//$NON-NLS-1$
	String BORDER_DOT = "SWT.BORDER_DOT";//$NON-NLS-1$
	String BORDER_SOLID = "SWT.BORDER_SOLID";//$NON-NLS-1$
	String BOTTOM = "SWT.BOTTOM";//$NON-NLS-1$
	String CASCADE = "SWT.CASCADE";//$NON-NLS-1$
	String CENTER = "SWT.CENTER";//$NON-NLS-1$
	String CHECK = "SWT.CHECK";//$NON-NLS-1$
	String DIALOG_TRIM = "SWT.DIALOG_TRIM";//$NON-NLS-1$
	String DOWN = "SWT.DOWN";//$NON-NLS-1$
	String DROP_DOWN = "SWT.DROP_DOWN";//$NON-NLS-1$
	String FILL = "SWT.FILL";//$NON-NLS-1$
	String FILL_BOTH = "GridData.FILL_BOTH";//$NON-NLS-1$
	String FILL_EVEN_ODD = "SWT.FILL_EVEN_ODD";//$NON-NLS-1$
	String FILL_HORIZONTAL = "GridData.FILL_HORIZONTAL";//$NON-NLS-1$
	String FILL_VERTICAL = "GridData.FILL_VERTICAL";//$NON-NLS-1$
	String FILL_WINDING = "SWT.FILL_WINDING";//$NON-NLS-1$
	String FOREGROUND = "SWT.FOREGROUND";//$NON-NLS-1$
	String FULL_SELECTION = "SWT.FULL_SELECTION";//$NON-NLS-1$
	String H_SCROLL = "SWT.H_SCROLL";//$NON-NLS-1$
	String HORIZONTAL = "SWT.HORIZONTAL";//$NON-NLS-1$
	String LEAD = "SWT.LEAD";//$NON-NLS-1$
	String LEFT = "SWT.LEFT";//$NON-NLS-1$
	String LEFT_TO_RIGHT = "SWT.LEFT_TO_RIGHT";//$NON-NLS-1$
	String LINE_CUSTOM = "SWT.LINE_CUSTOM";//$NON-NLS-1$
	String LINE_DASH = "SWT.LINE_DASH";//$NON-NLS-1$
	String LINE_DASHDOT = "SWT.LINE_DASHDOT";//$NON-NLS-1$
	String LINE_DASHDOTDOT = "SWT.LINE_DASHDOTDOT";//$NON-NLS-1$
	String LINE_DOT = "SWT.LINE_DOT";//$NON-NLS-1$
	String LINE_SOLID = "SWT.LINE_SOLID";//$NON-NLS-1$
	String MODELESS = "SWT.MODELESS";//$NON-NLS-1$
	String MULTI = "SWT.MULTI";//$NON-NLS-1$
	String NO = "SWT.NO";//$NON-NLS-1$
	String NO_BACKGROUND = "SWT.NO_BACKGROUND";//$NON-NLS-1$
	String NO_FOCUS = "SWT.NO_FOCUS";//$NON-NLS-1$
	String NO_MERGE_PAINTS = "SWT.NO_MERGE_PAINTS";//$NON-NLS-1$
	String NO_RADIO_GROUP = "SWT.NO_RADIO_GROUP";//$NON-NLS-1$
	String NO_REDRAW_RESIZE = "SWT.NO_REDRAW_RESIZE";//$NON-NLS-1$
	String NO_SCROLL = "SWT.NO_SCROLL";//$NON-NLS-1$
	String NO_TRIM = "SWT.NO_TRIM";//$NON-NLS-1$
	String NONE = "SWT.NONE";//$NON-NLS-1$
	String NORMAL = "SWT.NORMAL";//$NON-NLS-1$
	String ON_TOP = "SWT.ON_TOP";//$NON-NLS-1$
	String OPEN = "SWT.OPEN";//$NON-NLS-1$
	String POP_UP = "SWT.POP_UP";//$NON-NLS-1$
	String PRIMARY_MODAL = "SWT.PRIMARY_MODAL";//$NON-NLS-1$
	String PUSH = "SWT.PUSH";//$NON-NLS-1$
	String RADIO = "SWT.RADIO";//$NON-NLS-1$
	String READ_ONLY = "SWT.READ_ONLY";//$NON-NLS-1$
	String RESIZE = "SWT.RESIZE";//$NON-NLS-1$
	String RIGHT = "SWT.RIGHT";//$NON-NLS-1$
	String RIGHT_TO_LEFT = "SWT.RIGHT_TO_LEFT";//$NON-NLS-1$
	String SCROLL_LINE = "SWT.SCROLL_LINE";//$NON-NLS-1$
	String SCROLL_LOCK = "SWT.SCROLL_LOCK";//$NON-NLS-1$
	String SCROLL_PAGE = "SWT.SCROLL_PAGE";//$NON-NLS-1$
	String SHADOW_ETCHED_IN = "SWT.SHADOW_ETCHED_IN";//$NON-NLS-1$
	String SHADOW_ETCHED_OUT = "SWT.SHADOW_ETCHED_OUT";//$NON-NLS-1$
	String SHADOW_IN = "SWT.SHADOW_IN";//$NON-NLS-1$
	String SHADOW_NONE = "SWT.SHADOW_NONE";//$NON-NLS-1$
	String SHADOW_OUT = "SWT.SHADOW_OUT";//$NON-NLS-1$
	String SHELL_TRIM = "SWT.SHELL_TRIM";//$NON-NLS-1$
	String SHORT = "SWT.SHORT";//$NON-NLS-1$
	String SIMPLE = "SWT.SIMPLE";//$NON-NLS-1$
	String SINGLE = "SWT.SINGLE";//$NON-NLS-1$
	String SMOOTH = "SWT.SMOOTH";//$NON-NLS-1$
	String TITLE = "SWT.TITLE";//$NON-NLS-1$
	String TOGGLE = "SWT.TOGGLE";//$NON-NLS-1$
	String TOP = "SWT.TOGGLE";//$NON-NLS-1$
	String UP = "SWT.UP";//$NON-NLS-1$
	String V_SCROLL = "SWT.V_SCROLL";//$NON-NLS-1$
	String VERTICAL = "SWT.VERTICAL";//$NON-NLS-1$
	String WRAP = "SWT.WRAP";//$NON-NLS-1$
	String YES = "SWT.YES";//$NON-NLS-1$
}
