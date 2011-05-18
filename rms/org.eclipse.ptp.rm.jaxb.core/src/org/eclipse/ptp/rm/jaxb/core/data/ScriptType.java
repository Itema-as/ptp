//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.17 at 07:44:35 PM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * The script element specifies the contents line by line. Each line can be an
 * arbitrary set of arguments, each of which can be optionally resolved in the
 * environment, and which can be checked to see if the result after resolution
 * should be considered undefined and thus not included in the contents. The
 * insertEnvironmentAfter (line number) indicates where to insert extra
 * environment variable definitions passed in from the Environment Tab, if any.
 * 
 * 
 * <p>
 * Java class for script-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="script-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="line" type="{http://org.eclipse.ptp/rm}line-type" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="deleteAfterSubmit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="insertEnvironmentAfter" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "script-type", propOrder = { "line" })
public class ScriptType {

	@XmlElement(required = true)
	protected List<LineType> line;
	@XmlAttribute
	protected Boolean deleteAfterSubmit;
	@XmlAttribute
	protected Integer insertEnvironmentAfter;

	/**
	 * Gets the value of the insertEnvironmentAfter property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public int getInsertEnvironmentAfter() {
		if (insertEnvironmentAfter == null) {
			return -1;
		} else {
			return insertEnvironmentAfter;
		}
	}

	/**
	 * Gets the value of the line property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the line property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getLine().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link LineType }
	 * 
	 * 
	 */
	public List<LineType> getLine() {
		if (line == null) {
			line = new ArrayList<LineType>();
		}
		return this.line;
	}

	/**
	 * Gets the value of the deleteAfterSubmit property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isDeleteAfterSubmit() {
		if (deleteAfterSubmit == null) {
			return true;
		} else {
			return deleteAfterSubmit;
		}
	}

	/**
	 * Sets the value of the deleteAfterSubmit property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setDeleteAfterSubmit(Boolean value) {
		this.deleteAfterSubmit = value;
	}

	/**
	 * Sets the value of the insertEnvironmentAfter property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setInsertEnvironmentAfter(Integer value) {
		this.insertEnvironmentAfter = value;
	}

}
