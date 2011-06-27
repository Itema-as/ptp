/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.control.ui.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ptp.rm.jaxb.control.ui.handlers.ControlStateListener;
import org.eclipse.ptp.rm.jaxb.control.ui.messages.Messages;
import org.eclipse.ptp.rm.jaxb.core.data.ControlStateRuleType;
import org.eclipse.swt.widgets.Button;

/**
 * Implementation of the rule defined against button selection.
 * 
 * @author arossi
 * 
 */
public class ControlStateRule {

	private ControlStateRule not;
	private List<ControlStateRule> and;
	private List<ControlStateRule> or;
	private Button button;
	private boolean selected;

	/**
	 * Composes the rule; also wires the source with the target listener.
	 * 
	 * @param rule
	 *            JAXB element
	 * @param map
	 *            index of Widget controls
	 */
	public ControlStateRule(ControlStateRuleType rule, Map<String, Button> map, ControlStateListener listener) throws Throwable {
		ControlStateRuleType.Not not = rule.getNot();
		if (not != null) {
			this.not = new ControlStateRule(not.getRule(), map, listener);
		} else {
			ControlStateRuleType.And and = rule.getAnd();
			if (and != null) {
				List<ControlStateRuleType> list = and.getRule();
				if (!list.isEmpty()) {
					this.and = new ArrayList<ControlStateRule>();
					for (ControlStateRuleType t : list) {
						this.and.add(new ControlStateRule(t, map, listener));
					}
				}
			} else {
				ControlStateRuleType.Or or = rule.getOr();
				if (or != null) {
					List<ControlStateRuleType> list = or.getRule();
					if (!list.isEmpty()) {
						this.or = new ArrayList<ControlStateRule>();
						for (ControlStateRuleType t : list) {
							this.or.add(new ControlStateRule(t, map, listener));
						}
					}
				} else {
					button = map.get(rule.getButton());
					if (button == null) {
						throw new Throwable(Messages.ControlStateRule_0 + rule.getButton());
					}
					button.addSelectionListener(listener);
					selected = rule.isSelected();
				}
			}
		}
	}

	/**
	 * Triggered when an event is received. Recursively evaluates all clauses.
	 * Bottoms out in the check of the button selection.
	 * 
	 * @return whether the rule is satisfied
	 */
	public boolean evaluate() {
		if (not != null) {
			return !not.evaluate();
		}
		if (and != null) {
			for (ControlStateRule rule : and) {
				if (!rule.evaluate()) {
					return false;
				}
			}
			return true;
		}
		if (or != null) {
			for (ControlStateRule rule : or) {
				if (rule.evaluate()) {
					return true;
				}
			}
			return false;
		}
		return selected == button.getSelection();
	}
}
