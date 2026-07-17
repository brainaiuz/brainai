package com.edatasite.workforce.gwt.trainingcenter.server.jaxb;


import javax.xml.bind.annotation.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{urn:schemas-examview-com:testmgr:results}results" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "results"
})
@XmlRootElement(name = "results-list", namespace = "urn:schemas-examview-com:testmgr:results")
public class ResultsList {

    @XmlElement(namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected List<Results> results;
    @XmlAttribute(namespace = "urn:schemas-examview-com:testmgr:results", required = true)
    protected BigInteger count;

    /**
     * Gets the value of the results property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the results property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Results }
     * 
     * 
     */
    public List<Results> getResults() {
        if (results == null) {
            results = new ArrayList<>();
        }
        return this.results;
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

}
