package com.edatasite.workforce.gwt.trainingcenter.server.jaxb;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}student-sort"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}assignment-sort"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}display-score"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}show-term"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}show-nclb"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}default-term"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}category-name" maxOccurs="unbounded"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}custom-1"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}custom-2"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:preferences}custom-3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "studentSort",
    "assignmentSort",
    "displayScore",
    "showTerm",
    "showNclb",
    "defaultTerm",
    "categoryName",
    "custom1",
    "custom2",
    "custom3"
})
@XmlRootElement(name = "preferences", namespace = "urn:schemas-examview-com:testmgr:preferences")
public class Preferences {

    @XmlElement(name = "student-sort", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected StudentSort studentSort;
    @XmlElement(name = "assignment-sort", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected AssignmentSort assignmentSort;
    @XmlElement(name = "display-score", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected DisplayScore displayScore;
    @XmlElement(name = "show-term", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String showTerm;
    @XmlElement(name = "show-nclb", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String showNclb;
    @XmlElement(name = "default-term", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected BigInteger defaultTerm;
    @XmlElement(name = "category-name", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected List<CategoryName> categoryName;
    @XmlElement(name = "custom-1", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected String custom1;
    @XmlElement(name = "custom-2", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected String custom2;
    @XmlElement(name = "custom-3", namespace = "urn:schemas-examview-com:testmgr:preferences", required = true)
    protected String custom3;

    /**
     * Gets the value of the studentSort property.
     * 
     * @return
     *     possible object is
     *     {@link StudentSort }
     *     
     */
    public StudentSort getStudentSort() {
        return studentSort;
    }

    /**
     * Sets the value of the studentSort property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentSort }
     *     
     */
    public void setStudentSort(StudentSort value) {
        this.studentSort = value;
    }

    /**
     * Gets the value of the assignmentSort property.
     * 
     * @return
     *     possible object is
     *     {@link AssignmentSort }
     *     
     */
    public AssignmentSort getAssignmentSort() {
        return assignmentSort;
    }

    /**
     * Sets the value of the assignmentSort property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssignmentSort }
     *     
     */
    public void setAssignmentSort(AssignmentSort value) {
        this.assignmentSort = value;
    }

    /**
     * Gets the value of the displayScore property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayScore }
     *     
     */
    public DisplayScore getDisplayScore() {
        return displayScore;
    }

    /**
     * Sets the value of the displayScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayScore }
     *     
     */
    public void setDisplayScore(DisplayScore value) {
        this.displayScore = value;
    }

    /**
     * Gets the value of the showTerm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowTerm() {
        return showTerm;
    }

    /**
     * Sets the value of the showTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowTerm(String value) {
        this.showTerm = value;
    }

    /**
     * Gets the value of the showNclb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowNclb() {
        return showNclb;
    }

    /**
     * Sets the value of the showNclb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowNclb(String value) {
        this.showNclb = value;
    }

    /**
     * Gets the value of the defaultTerm property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getDefaultTerm() {
        return defaultTerm;
    }

    /**
     * Sets the value of the defaultTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setDefaultTerm(BigInteger value) {
        this.defaultTerm = value;
    }

    /**
     * Gets the value of the categoryName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categoryName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategoryName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CategoryName }
     * 
     * 
     */
    public List<CategoryName> getCategoryName() {
        if (categoryName == null) {
            categoryName = new ArrayList<>();
        }
        return this.categoryName;
    }

    /**
     * Gets the value of the custom1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom1() {
        return custom1;
    }

    /**
     * Sets the value of the custom1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom1(String value) {
        this.custom1 = value;
    }

    /**
     * Gets the value of the custom2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom2() {
        return custom2;
    }

    /**
     * Sets the value of the custom2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom2(String value) {
        this.custom2 = value;
    }

    /**
     * Gets the value of the custom3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom3() {
        return custom3;
    }

    /**
     * Sets the value of the custom3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom3(String value) {
        this.custom3 = value;
    }

}
