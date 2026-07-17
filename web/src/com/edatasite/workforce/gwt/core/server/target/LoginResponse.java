/**
 * LoginResponse.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;


import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

import javax.xml.namespace.QName;

/**
 * Return type for new persistent method login_new
 */
public class LoginResponse implements java.io.Serializable {
    private String _connectionid;

    private String _return;

    public LoginResponse() {
    }

    public LoginResponse(
            String _connectionid,
            String _return) {
        this._connectionid = _connectionid;
        this._return = _return;
    }


    /**
     * Gets the _connectionid value for this LoginResponse.
     *
     * @return _connectionid
     */
    public String get_connectionid() {
        return _connectionid;
    }


    /**
     * Sets the _connectionid value for this LoginResponse.
     *
     * @param _connectionid
     */
    public void set_connectionid(String _connectionid) {
        this._connectionid = _connectionid;
    }


    /**
     * Gets the _return value for this LoginResponse.
     *
     * @return _return
     */
    public String get_return() {
        return _return;
    }


    /**
     * Sets the _return value for this LoginResponse.
     *
     * @param _return
     */
    public void set_return(String _return) {
        this._return = _return;
    }

    private Object __equalsCalc = null;

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof LoginResponse)) return false;
        LoginResponse other = (LoginResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = ((this._connectionid == null && other.get_connectionid() == null) ||
                (this._connectionid != null &&
                        this._connectionid.equals(other.get_connectionid()))) &&
                ((this._return == null && other.get_return() == null) ||
                        (this._return != null &&
                                this._return.equals(other.get_return())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (get_connectionid() != null) {
            _hashCode += get_connectionid().hashCode();
        }
        if (get_return() != null) {
            _hashCode += get_return().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
            new TypeDesc(LoginResponse.class, true);

    static {
        typeDesc.setXmlType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "login_new.return"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("_connectionid");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("_return");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static Serializer getSerializer(
            String mechType,
            Class _javaType,
            QName _xmlType) {
        return
                new BeanSerializer(
                        _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static Deserializer getDeserializer(
            String mechType,
            Class _javaType,
            QName _xmlType) {
        return
                new BeanDeserializer(
                        _javaType, _xmlType, typeDesc);
    }

}
