//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 10:28:09 AM EDT 
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
 * <p>Java class for target-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="target-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="match" type="{http://eclipse.org/ptp/rm}match-type" maxOccurs="unbounded"/>
 *         &lt;element name="test" type="{http://eclipse.org/ptp/rm}test-type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="else" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="add" type="{http://eclipse.org/ptp/rm}add-type"/>
 *                   &lt;element name="append" type="{http://eclipse.org/ptp/rm}append-type"/>
 *                   &lt;element name="put" type="{http://eclipse.org/ptp/rm}put-type"/>
 *                   &lt;element name="set" type="{http://eclipse.org/ptp/rm}set-type"/>
 *                   &lt;element name="throw" type="{http://eclipse.org/ptp/rm}throw-type"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="matchAll" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="allowOverwrites" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target-type", propOrder = {
    "match",
    "test",
    "_else"
})
public class TargetType {

    @XmlElement(required = true)
    protected List<MatchType> match;
    protected List<TestType> test;
    @XmlElement(name = "else")
    protected TargetType.Else _else;
    @XmlAttribute(name = "ref")
    protected String ref;
    @XmlAttribute(name = "matchAll")
    protected Boolean matchAll;
    @XmlAttribute(name = "allowOverwrites")
    protected Boolean allowOverwrites;

    /**
     * Gets the value of the match property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the match property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMatch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MatchType }
     * 
     * 
     */
    public List<MatchType> getMatch() {
        if (match == null) {
            match = new ArrayList<MatchType>();
        }
        return this.match;
    }

    /**
     * Gets the value of the test property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the test property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TestType }
     * 
     * 
     */
    public List<TestType> getTest() {
        if (test == null) {
            test = new ArrayList<TestType>();
        }
        return this.test;
    }

    /**
     * Gets the value of the else property.
     * 
     * @return
     *     possible object is
     *     {@link TargetType.Else }
     *     
     */
    public TargetType.Else getElse() {
        return _else;
    }

    /**
     * Sets the value of the else property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetType.Else }
     *     
     */
    public void setElse(TargetType.Else value) {
        this._else = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the matchAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isMatchAll() {
        if (matchAll == null) {
            return false;
        } else {
            return matchAll;
        }
    }

    /**
     * Sets the value of the matchAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMatchAll(Boolean value) {
        this.matchAll = value;
    }

    /**
     * Gets the value of the allowOverwrites property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAllowOverwrites() {
        if (allowOverwrites == null) {
            return false;
        } else {
            return allowOverwrites;
        }
    }

    /**
     * Sets the value of the allowOverwrites property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllowOverwrites(Boolean value) {
        this.allowOverwrites = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="add" type="{http://eclipse.org/ptp/rm}add-type"/>
     *         &lt;element name="append" type="{http://eclipse.org/ptp/rm}append-type"/>
     *         &lt;element name="put" type="{http://eclipse.org/ptp/rm}put-type"/>
     *         &lt;element name="set" type="{http://eclipse.org/ptp/rm}set-type"/>
     *         &lt;element name="throw" type="{http://eclipse.org/ptp/rm}throw-type"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "add",
        "append",
        "put",
        "set",
        "_throw"
    })
    public static class Else {

        protected AddType add;
        protected AppendType append;
        protected PutType put;
        protected SetType set;
        @XmlElement(name = "throw")
        protected ThrowType _throw;

        /**
         * Gets the value of the add property.
         * 
         * @return
         *     possible object is
         *     {@link AddType }
         *     
         */
        public AddType getAdd() {
            return add;
        }

        /**
         * Sets the value of the add property.
         * 
         * @param value
         *     allowed object is
         *     {@link AddType }
         *     
         */
        public void setAdd(AddType value) {
            this.add = value;
        }

        /**
         * Gets the value of the append property.
         * 
         * @return
         *     possible object is
         *     {@link AppendType }
         *     
         */
        public AppendType getAppend() {
            return append;
        }

        /**
         * Sets the value of the append property.
         * 
         * @param value
         *     allowed object is
         *     {@link AppendType }
         *     
         */
        public void setAppend(AppendType value) {
            this.append = value;
        }

        /**
         * Gets the value of the put property.
         * 
         * @return
         *     possible object is
         *     {@link PutType }
         *     
         */
        public PutType getPut() {
            return put;
        }

        /**
         * Sets the value of the put property.
         * 
         * @param value
         *     allowed object is
         *     {@link PutType }
         *     
         */
        public void setPut(PutType value) {
            this.put = value;
        }

        /**
         * Gets the value of the set property.
         * 
         * @return
         *     possible object is
         *     {@link SetType }
         *     
         */
        public SetType getSet() {
            return set;
        }

        /**
         * Sets the value of the set property.
         * 
         * @param value
         *     allowed object is
         *     {@link SetType }
         *     
         */
        public void setSet(SetType value) {
            this.set = value;
        }

        /**
         * Gets the value of the throw property.
         * 
         * @return
         *     possible object is
         *     {@link ThrowType }
         *     
         */
        public ThrowType getThrow() {
            return _throw;
        }

        /**
         * Sets the value of the throw property.
         * 
         * @param value
         *     allowed object is
         *     {@link ThrowType }
         *     
         */
        public void setThrow(ThrowType value) {
            this._throw = value;
        }

    }

}
