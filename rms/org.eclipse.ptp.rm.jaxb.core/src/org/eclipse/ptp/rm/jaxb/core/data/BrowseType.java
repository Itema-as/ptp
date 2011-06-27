//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.27 at 09:36:08 AM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for browse-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="browse-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="text-layout-data" type="{http://org.eclipse.ptp/rm}layout-data-type" minOccurs="0"/>
 *         &lt;element name="button-layout-data" type="{http://org.eclipse.ptp/rm}layout-data-type" minOccurs="0"/>
 *         &lt;element name="font" type="{http://org.eclipse.ptp/rm}font-type" minOccurs="0"/>
 *         &lt;element name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="text-control-state" type="{http://org.eclipse.ptp/rm}control-state-type" minOccurs="0"/>
 *         &lt;element name="button-control-state" type="{http://org.eclipse.ptp/rm}control-state-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="background" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="directory" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="foreground" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="localOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="readOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="saveValueTo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="textStyle" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="uri" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "browse-type", propOrder = { "textLayoutData", "buttonLayoutData", "font", "tooltip", "textControlState",
		"buttonControlState" })
public class BrowseType {

	@XmlElement(name = "text-layout-data")
	protected LayoutDataType textLayoutData;
	@XmlElement(name = "button-layout-data")
	protected LayoutDataType buttonLayoutData;
	protected FontType font;
	protected String tooltip;
	@XmlElement(name = "text-control-state")
	protected ControlStateType textControlState;
	@XmlElement(name = "button-control-state")
	protected ControlStateType buttonControlState;
	@XmlAttribute
	protected String background;
	@XmlAttribute
	protected Boolean directory;
	@XmlAttribute
	protected String foreground;
	@XmlAttribute
	protected Boolean localOnly;
	@XmlAttribute
	protected Boolean readOnly;
	@XmlAttribute
	protected String saveValueTo;
	@XmlAttribute
	protected String textStyle;
	@XmlAttribute
	protected String title;
	@XmlAttribute
	protected Boolean uri;

	/**
	 * Gets the value of the background property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * Gets the value of the buttonControlState property.
	 * 
	 * @return possible object is {@link ControlStateType }
	 * 
	 */
	public ControlStateType getButtonControlState() {
		return buttonControlState;
	}

	/**
	 * Gets the value of the buttonLayoutData property.
	 * 
	 * @return possible object is {@link LayoutDataType }
	 * 
	 */
	public LayoutDataType getButtonLayoutData() {
		return buttonLayoutData;
	}

	/**
	 * Gets the value of the font property.
	 * 
	 * @return possible object is {@link FontType }
	 * 
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Gets the value of the foreground property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getForeground() {
		return foreground;
	}

	/**
	 * Gets the value of the saveValueTo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSaveValueTo() {
		return saveValueTo;
	}

	/**
	 * Gets the value of the textControlState property.
	 * 
	 * @return possible object is {@link ControlStateType }
	 * 
	 */
	public ControlStateType getTextControlState() {
		return textControlState;
	}

	/**
	 * Gets the value of the textLayoutData property.
	 * 
	 * @return possible object is {@link LayoutDataType }
	 * 
	 */
	public LayoutDataType getTextLayoutData() {
		return textLayoutData;
	}

	/**
	 * Gets the value of the textStyle property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTextStyle() {
		return textStyle;
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the value of the tooltip property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Gets the value of the directory property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isDirectory() {
		if (directory == null) {
			return false;
		} else {
			return directory;
		}
	}

	/**
	 * Gets the value of the localOnly property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isLocalOnly() {
		if (localOnly == null) {
			return false;
		} else {
			return localOnly;
		}
	}

	/**
	 * Gets the value of the readOnly property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isReadOnly() {
		if (readOnly == null) {
			return false;
		} else {
			return readOnly;
		}
	}

	/**
	 * Gets the value of the uri property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isUri() {
		if (uri == null) {
			return false;
		} else {
			return uri;
		}
	}

	/**
	 * Sets the value of the background property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBackground(String value) {
		this.background = value;
	}

	/**
	 * Sets the value of the buttonControlState property.
	 * 
	 * @param value
	 *            allowed object is {@link ControlStateType }
	 * 
	 */
	public void setButtonControlState(ControlStateType value) {
		this.buttonControlState = value;
	}

	/**
	 * Sets the value of the buttonLayoutData property.
	 * 
	 * @param value
	 *            allowed object is {@link LayoutDataType }
	 * 
	 */
	public void setButtonLayoutData(LayoutDataType value) {
		this.buttonLayoutData = value;
	}

	/**
	 * Sets the value of the directory property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setDirectory(Boolean value) {
		this.directory = value;
	}

	/**
	 * Sets the value of the font property.
	 * 
	 * @param value
	 *            allowed object is {@link FontType }
	 * 
	 */
	public void setFont(FontType value) {
		this.font = value;
	}

	/**
	 * Sets the value of the foreground property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setForeground(String value) {
		this.foreground = value;
	}

	/**
	 * Sets the value of the localOnly property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setLocalOnly(Boolean value) {
		this.localOnly = value;
	}

	/**
	 * Sets the value of the readOnly property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setReadOnly(Boolean value) {
		this.readOnly = value;
	}

	/**
	 * Sets the value of the saveValueTo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSaveValueTo(String value) {
		this.saveValueTo = value;
	}

	/**
	 * Sets the value of the textControlState property.
	 * 
	 * @param value
	 *            allowed object is {@link ControlStateType }
	 * 
	 */
	public void setTextControlState(ControlStateType value) {
		this.textControlState = value;
	}

	/**
	 * Sets the value of the textLayoutData property.
	 * 
	 * @param value
	 *            allowed object is {@link LayoutDataType }
	 * 
	 */
	public void setTextLayoutData(LayoutDataType value) {
		this.textLayoutData = value;
	}

	/**
	 * Sets the value of the textStyle property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTextStyle(String value) {
		this.textStyle = value;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Sets the value of the tooltip property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTooltip(String value) {
		this.tooltip = value;
	}

	/**
	 * Sets the value of the uri property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setUri(Boolean value) {
		this.uri = value;
	}

}
