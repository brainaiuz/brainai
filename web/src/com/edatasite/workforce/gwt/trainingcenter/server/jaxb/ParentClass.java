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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:class}class-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:class}instructor-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:class}school-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:class}city"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:class}state"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}preferences"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:student}student-list"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}assignment-list"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}space use="required""/>
 *       &lt;attribute name="app-version" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
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
    "className",
    "instructorName",
    "schoolName",
    "city",
    "state",
    "preferences",
    "studentList",
    "assignmentList"
})
@XmlRootElement(name = "class", namespace = "urn:schemas-examview-com:testmgr:class")
public class ParentClass {

    @XmlElement(name = "class-name", namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    protected String className;
    @XmlElement(name = "instructor-name", namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    protected String instructorName;
    @XmlElement(name = "school-name", namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    protected String schoolName;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String city;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String state;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected Preferences preferences;
    @XmlElement(name = "student-list", namespace = "urn:schemas-examview-com:testmgr:student", required = true)
    protected StudentList studentList;
    @XmlElement(name = "assignment-list", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected AssignmentList assignmentList;
    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String space;
    @XmlAttribute(name = "app-version", namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String appVersion;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:class", required = true)
    protected BigInteger id;

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the instructorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Sets the value of the instructorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstructorName(String value) {
        this.instructorName = value;
    }

    /**
     * Gets the value of the schoolName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * Sets the value of the schoolName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchoolName(String value) {
        this.schoolName = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the preferences property.
     * 
     * @return
     *     possible object is
     *     {@link Preferences }
     *     
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Sets the value of the preferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link Preferences }
     *     
     */
    public void setPreferences(Preferences value) {
        this.preferences = value;
    }

    /**
     * Gets the value of the studentList property.
     * 
     * @return
     *     possible object is
     *     {@link StudentList }
     *     
     */
    public StudentList getStudentList() {
        return studentList;
    }

    /**
     * Sets the value of the studentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentList }
     *     
     */
    public void setStudentList(StudentList value) {
        this.studentList = value;
    }

    /**
     * Gets the value of the assignmentList property.
     * 
     * @return
     *     possible object is
     *     {@link AssignmentList }
     *     
     */
    public AssignmentList getAssignmentList() {
        return assignmentList;
    }

    /**
     * Sets the value of the assignmentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssignmentList }
     *     
     */
    public void setAssignmentList(AssignmentList value) {
        this.assignmentList = value;
    }

    /**
     * Gets the value of the space property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpace() {
        return space;
    }

    /**
     * Sets the value of the space property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpace(String value) {
        this.space = value;
    }

    /**
     * Gets the value of the appVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Sets the value of the appVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppVersion(String value) {
        this.appVersion = value;
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
