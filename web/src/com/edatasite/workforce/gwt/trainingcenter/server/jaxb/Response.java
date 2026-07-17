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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}text"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}points-earned"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bank-id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="question-id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "text",
    "pointsEarned"
})
@XmlRootElement(name = "response", namespace = "urn:schemas-examview-com:testmgr:results")
public class Response {

    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String text;
    @XmlElement(name = "points-earned", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger pointsEarned;
    @XmlAttribute(name = "bank-id", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger bankId;
    @XmlAttribute(name = "question-id", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger questionId;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the pointsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getPointsEarned() {
        return pointsEarned;
    }

    /**
     * Sets the value of the pointsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setPointsEarned(BigInteger value) {
        this.pointsEarned = value;
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

}
