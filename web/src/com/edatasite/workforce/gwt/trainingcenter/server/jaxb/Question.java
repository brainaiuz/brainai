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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}num-choices"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}answer"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}reference"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}learning-objective"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}state-standard"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:assignment}points-possible"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bank-id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="number" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="question-id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "numChoices",
    "answer",
    "reference",
    "learningObjective",
    "stateStandard",
    "pointsPossible"
})
@XmlRootElement(name = "question")
public class Question {

    @XmlElement(name = "num-choices", required = true)
    protected BigInteger numChoices;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String answer;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String reference;
    @XmlElement(name = "learning-objective", required = true)
    protected String learningObjective;
    @XmlElement(name = "state-standard", required = true)
    protected StateStandard stateStandard;
    @XmlElement(name = "points-possible", required = true)
    protected BigInteger pointsPossible;
    @XmlAttribute(name = "bank-id", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger bankId;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger number;
    @XmlAttribute(name = "question-id", namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    protected BigInteger questionId;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:assignment", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String type;

    /**
     * Gets the value of the numChoices property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getNumChoices() {
        return numChoices;
    }

    /**
     * Sets the value of the numChoices property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setNumChoices(BigInteger value) {
        this.numChoices = value;
    }

    /**
     * Gets the value of the answer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the value of the answer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnswer(String value) {
        this.answer = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the learningObjective property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearningObjective() {
        return learningObjective;
    }

    /**
     * Sets the value of the learningObjective property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearningObjective(String value) {
        this.learningObjective = value;
    }

    /**
     * Gets the value of the stateStandard property.
     * 
     * @return
     *     possible object is
     *     {@link StateStandard }
     *     
     */
    public StateStandard getStateStandard() {
        return stateStandard;
    }

    /**
     * Sets the value of the stateStandard property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateStandard }
     *     
     */
    public void setStateStandard(StateStandard value) {
        this.stateStandard = value;
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
     * Gets the value of the bankId property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getBankId() {
        return bankId;
    }

    /**
     * Sets the value of the bankId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setBankId(BigInteger value) {
        this.bankId = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setNumber(BigInteger value) {
        this.number = value;
    }

    /**
     * Gets the value of the questionId property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getQuestionId() {
        return questionId;
    }

    /**
     * Sets the value of the questionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setQuestionId(BigInteger value) {
        this.questionId = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
