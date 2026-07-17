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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}assignment-name"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}category"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}online-id"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}term"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}assignment-date"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}delivery-method"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}test-file"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}points-possible"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}question-list"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}results-list"/>
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
    "assignmentName",
    "category",
    "onlineId",
    "term",
    "assignmentDate",
    "deliveryMethod",
    "testFile",
    "pointsPossible",
    "questionList",
    "resultsList"
})
@XmlRootElement(name = "assignment")
public class Assignment {

    @XmlElement(name = "assignment-name", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected String assignmentName;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String category;
    @XmlElement(name = "online-id", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String onlineId;
    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger term;
    @XmlElement(name = "assignment-date", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected String assignmentDate;
    @XmlElement(name = "delivery-method", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String deliveryMethod;
    @XmlElement(name = "test-file", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected String testFile;
    @XmlElement(name = "points-possible", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger pointsPossible;
    @XmlElement(name = "question-list", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected QuestionList questionList;
    @XmlElement(name = "results-list", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected ResultsList resultsList;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger id;

    /**
     * Gets the value of the assignmentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignmentName() {
        return assignmentName;
    }

    /**
     * Sets the value of the assignmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignmentName(String value) {
        this.assignmentName = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the onlineId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnlineId() {
        return onlineId;
    }

    /**
     * Sets the value of the onlineId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnlineId(String value) {
        this.onlineId = value;
    }

    /**
     * Gets the value of the term property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getTerm() {
        return term;
    }

    /**
     * Sets the value of the term property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setTerm(BigInteger value) {
        this.term = value;
    }

    /**
     * Gets the value of the assignmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignmentDate() {
        return assignmentDate;
    }

    /**
     * Sets the value of the assignmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignmentDate(String value) {
        this.assignmentDate = value;
    }

    /**
     * Gets the value of the deliveryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the value of the deliveryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryMethod(String value) {
        this.deliveryMethod = value;
    }

    /**
     * Gets the value of the testFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestFile() {
        return testFile;
    }

    /**
     * Sets the value of the testFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestFile(String value) {
        this.testFile = value;
    }

    /**
     * Gets the value of the pointsPossible property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getPointsPossible() {
        return pointsPossible;
    }

    /**
     * Sets the value of the pointsPossible property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setPointsPossible(BigInteger value) {
        this.pointsPossible = value;
    }

    /**
     * Gets the value of the questionList property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionList }
     *     
     */
    public QuestionList getQuestionList() {
        return questionList;
    }

    /**
     * Sets the value of the questionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionList }
     *     
     */
    public void setQuestionList(QuestionList value) {
        this.questionList = value;
    }

    /**
     * Gets the value of the resultsList property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsList }
     *     
     */
    public ResultsList getResultsList() {
        return resultsList;
    }

    /**
     * Sets the value of the resultsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsList }
     *     
     */
    public void setResultsList(ResultsList value) {
        this.resultsList = value;
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
