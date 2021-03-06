//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.13 at 09:16:36 AM CET 
//

package org.eclipse.ptp.rm.lml.core.elements;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * All el?-tags will be subtypes of this type. Tagname has
 * to be defined here. Data-elements must not repeat this
 * tagname.
 * 
 * 
 * <p>
 * Java class for scheme_element complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scheme_element">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}element_type">
 *       &lt;attribute name="tagname" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="mask" default="%d-">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="(([^%])*%(\-|\+|\s|#)*0(\-|\+|\s|#)*(\d)+d([^%])*)|(([^%])*%(\-|\+|\s|#)*d([^%\d])+)|(([^%])*%(\-|\+|\s|#)*1d([^%])*)"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="step" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" default="1" />
 *       &lt;attribute name="map" type="{http://eclipse.org/ptp/lml}namemapping_type" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scheme_element")
@XmlSeeAlso({
		SchemeElement1.class,
		SchemeElement9.class,
		SchemeElement8.class,
		SchemeElement7.class,
		SchemeElement6.class,
		SchemeElement5.class,
		SchemeElement4.class,
		SchemeElement3.class,
		SchemeElement2.class,
		SchemeElement10.class
})
public class SchemeElement
		extends ElementType
{

	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NCName")
	protected String tagname;
	@XmlAttribute
	protected String mask;
	@XmlAttribute
	@XmlSchemaType(name = "positiveInteger")
	protected BigInteger step;
	@XmlAttribute
	protected String map;

	/**
	 * Gets the value of the tagname property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getTagname() {
		return tagname;
	}

	/**
	 * Sets the value of the tagname property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTagname(String value) {
		this.tagname = value;
	}

	/**
	 * Gets the value of the mask property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getMask() {
		if (mask == null) {
			return "%d-";
		} else {
			return mask;
		}
	}

	/**
	 * Sets the value of the mask property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMask(String value) {
		this.mask = value;
	}

	/**
	 * Gets the value of the step property.
	 * 
	 * @return
	 *         possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getStep() {
		if (step == null) {
			return new BigInteger("1");
		} else {
			return step;
		}
	}

	/**
	 * Sets the value of the step property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setStep(BigInteger value) {
		this.step = value;
	}

	/**
	 * Gets the value of the map property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getMap() {
		return map;
	}

	/**
	 * Sets the value of the map property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMap(String value) {
		this.map = value;
	}

}
