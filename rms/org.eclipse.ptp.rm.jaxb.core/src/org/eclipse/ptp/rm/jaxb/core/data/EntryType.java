//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 10:28:09 AM EDT 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for entry-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="entry-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="keyGroup" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="keyIndex" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="valueGroup" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="valueIndex" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entry-type")
public class EntryType {

    @XmlAttribute(name = "key")
    protected String key;
    @XmlAttribute(name = "keyGroup")
    protected Integer keyGroup;
    @XmlAttribute(name = "keyIndex")
    protected Integer keyIndex;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "valueGroup")
    protected Integer valueGroup;
    @XmlAttribute(name = "valueIndex")
    protected Integer valueIndex;

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the keyGroup property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getKeyGroup() {
        if (keyGroup == null) {
            return  0;
        } else {
            return keyGroup;
        }
    }

    /**
     * Sets the value of the keyGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKeyGroup(Integer value) {
        this.keyGroup = value;
    }

    /**
     * Gets the value of the keyIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getKeyIndex() {
        if (keyIndex == null) {
            return  0;
        } else {
            return keyIndex;
        }
    }

    /**
     * Sets the value of the keyIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKeyIndex(Integer value) {
        this.keyIndex = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the valueGroup property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getValueGroup() {
        if (valueGroup == null) {
            return  0;
        } else {
            return valueGroup;
        }
    }

    /**
     * Sets the value of the valueGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueGroup(Integer value) {
        this.valueGroup = value;
    }

    /**
     * Gets the value of the valueIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getValueIndex() {
        if (valueIndex == null) {
            return  0;
        } else {
            return valueIndex;
        }
    }

    /**
     * Sets the value of the valueIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueIndex(Integer value) {
        this.valueIndex = value;
    }

}
