package com.edatasite.workforce.gwt.trainingcenter.server.jaxb;


import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}first-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}last-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}student-id"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}pad-id"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}gender"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "firstName",
    "lastName",
    "studentId",
    "padId",
    "gender"
})
@XmlRootElement(name = "student", namespace = "urn:schemas-examview-com:testmgr:student")
public class Student {

    @XmlElement(name = "first-name", namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String firstName;
    @XmlElement(name = "last-name", namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    protected String lastName;
    @XmlElement(name = "student-id", namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    protected BigInteger studentId;
    @XmlElement(name = "pad-id", namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    protected BigInteger padId;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String gender;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    protected BigInteger id;

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the studentId property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getStudentId() {
        return studentId;
    }

    /**
     * Sets the value of the studentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setStudentId(BigInteger value) {
        this.studentId = value;
    }

    /**
     * Gets the value of the padId property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getPadId() {
        return padId;
    }

    /**
     * Sets the value of the padId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setPadId(BigInteger value) {
        this.padId = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

}
