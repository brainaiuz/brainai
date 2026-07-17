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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}num-attempts"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}start-date"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}end-date"/>
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}response-list"/>
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
    "numAttempts",
    "startDate",
    "endDate",
    "responseList"
})
@XmlRootElement(name = "results", namespace = "urn:schemas-examview-com:testmgr:results")
public class Results {

    @XmlElement(name = "num-attempts", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger numAttempts;
    @XmlElement(name = "start-date", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected String startDate;
    @XmlElement(name = "end-date", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected String endDate;
    @XmlElement(name = "response-list", namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected ResponseList responseList;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger id;

    /**
     * Gets the value of the numAttempts property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getNumAttempts() {
        return numAttempts;
    }

    /**
     * Sets the value of the numAttempts property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setNumAttempts(BigInteger value) {
        this.numAttempts = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the responseList property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseList }
     *     
     */
    public ResponseList getResponseList() {
        return responseList;
    }

    /**
     * Sets the value of the responseList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseList }
     *     
     */
    public void setResponseList(ResponseList value) {
        this.responseList = value;
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
