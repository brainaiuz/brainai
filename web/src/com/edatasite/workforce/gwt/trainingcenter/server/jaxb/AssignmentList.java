package com.edatasite.workforce.gwt.trainingcenter.server.jaxb;


import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}assignment"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="next-id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "assignment"
})
@XmlRootElement(name = "assignment-list", namespace = "urn:schemas-examview-com:testmgr:assignment")
public class AssignmentList {

    @XmlElement(name = "assignment", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected Assignment assignment;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger count;
    @XmlAttribute(name = "next-id", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger nextId;

    /**
     * Gets the value of the assignment property.
     * 
     * @return
     *     possible object is
     *     {@link Assignment }
     *     
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Sets the value of the assignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Assignment }
     *     
     */
    public void setAssignment(Assignment value) {
        this.assignment = value;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

    /**
     * Gets the value of the nextId property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getNextId() {
        return nextId;
    }

    /**
     * Sets the value of the nextId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setNextId(BigInteger value) {
        this.nextId = value;
    }

}
