/**
 * TableStructure.java
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
import java.io.Serializable;

/**
 * Definizione base della struttura dati
 */
public class TableStructure implements Serializable {
    private String NOME;

    private String TIPO;

    private String SOTTOTIPO;

    private int LUNGHEZZA;

    private String ETICHETTA;

    private double LARGHEZZA;

    private int ALLINEAMENTO;

    private boolean FLAG_TOTAL;

    private boolean FLAG_NO_DISPLAY;

    private String DESCRIZIONE;

    private String NOME_SCHEMA;

    public TableStructure() {
    }

    public TableStructure(
            String NOME,
            String TIPO,
            String SOTTOTIPO,
            int LUNGHEZZA,
            String ETICHETTA,
            double LARGHEZZA,
            int ALLINEAMENTO,
            boolean FLAG_TOTAL,
            boolean FLAG_NO_DISPLAY,
            String DESCRIZIONE,
            String NOME_SCHEMA) {
        this.NOME = NOME;
        this.TIPO = TIPO;
        this.SOTTOTIPO = SOTTOTIPO;
        this.LUNGHEZZA = LUNGHEZZA;
        this.ETICHETTA = ETICHETTA;
        this.LARGHEZZA = LARGHEZZA;
        this.ALLINEAMENTO = ALLINEAMENTO;
        this.FLAG_TOTAL = FLAG_TOTAL;
        this.FLAG_NO_DISPLAY = FLAG_NO_DISPLAY;
        this.DESCRIZIONE = DESCRIZIONE;
        this.NOME_SCHEMA = NOME_SCHEMA;
    }


    /**
     * Gets the NOME value for this TableStructure.
     *
     * @return NOME
     */
    public String getNOME() {
        return NOME;
    }


    /**
     * Sets the NOME value for this TableStructure.
     *
     * @param NOME
     */
    public void setNOME(String NOME) {
        this.NOME = NOME;
    }


    /**
     * Gets the TIPO value for this TableStructure.
     *
     * @return TIPO
     */
    public String getTIPO() {
        return TIPO;
    }


    /**
     * Sets the TIPO value for this TableStructure.
     *
     * @param TIPO
     */
    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }


    /**
     * Gets the SOTTOTIPO value for this TableStructure.
     *
     * @return SOTTOTIPO
     */
    public String getSOTTOTIPO() {
        return SOTTOTIPO;
    }


    /**
     * Sets the SOTTOTIPO value for this TableStructure.
     *
     * @param SOTTOTIPO
     */
    public void setSOTTOTIPO(String SOTTOTIPO) {
        this.SOTTOTIPO = SOTTOTIPO;
    }


    /**
     * Gets the LUNGHEZZA value for this TableStructure.
     *
     * @return LUNGHEZZA
     */
    public int getLUNGHEZZA() {
        return LUNGHEZZA;
    }


    /**
     * Sets the LUNGHEZZA value for this TableStructure.
     *
     * @param LUNGHEZZA
     */
    public void setLUNGHEZZA(int LUNGHEZZA) {
        this.LUNGHEZZA = LUNGHEZZA;
    }


    /**
     * Gets the ETICHETTA value for this TableStructure.
     *
     * @return ETICHETTA
     */
    public String getETICHETTA() {
        return ETICHETTA;
    }


    /**
     * Sets the ETICHETTA value for this TableStructure.
     *
     * @param ETICHETTA
     */
    public void setETICHETTA(String ETICHETTA) {
        this.ETICHETTA = ETICHETTA;
    }


    /**
     * Gets the LARGHEZZA value for this TableStructure.
     *
     * @return LARGHEZZA
     */
    public double getLARGHEZZA() {
        return LARGHEZZA;
    }


    /**
     * Sets the LARGHEZZA value for this TableStructure.
     *
     * @param LARGHEZZA
     */
    public void setLARGHEZZA(double LARGHEZZA) {
        this.LARGHEZZA = LARGHEZZA;
    }


    /**
     * Gets the ALLINEAMENTO value for this TableStructure.
     *
     * @return ALLINEAMENTO
     */
    public int getALLINEAMENTO() {
        return ALLINEAMENTO;
    }


    /**
     * Sets the ALLINEAMENTO value for this TableStructure.
     *
     * @param ALLINEAMENTO
     */
    public void setALLINEAMENTO(int ALLINEAMENTO) {
        this.ALLINEAMENTO = ALLINEAMENTO;
    }


    /**
     * Gets the FLAG_TOTAL value for this TableStructure.
     *
     * @return FLAG_TOTAL
     */
    public boolean isFLAG_TOTAL() {
        return FLAG_TOTAL;
    }


    /**
     * Sets the FLAG_TOTAL value for this TableStructure.
     *
     * @param FLAG_TOTAL
     */
    public void setFLAG_TOTAL(boolean FLAG_TOTAL) {
        this.FLAG_TOTAL = FLAG_TOTAL;
    }


    /**
     * Gets the FLAG_NO_DISPLAY value for this TableStructure.
     *
     * @return FLAG_NO_DISPLAY
     */
    public boolean isFLAG_NO_DISPLAY() {
        return FLAG_NO_DISPLAY;
    }


    /**
     * Sets the FLAG_NO_DISPLAY value for this TableStructure.
     *
     * @param FLAG_NO_DISPLAY
     */
    public void setFLAG_NO_DISPLAY(boolean FLAG_NO_DISPLAY) {
        this.FLAG_NO_DISPLAY = FLAG_NO_DISPLAY;
    }


    /**
     * Gets the DESCRIZIONE value for this TableStructure.
     *
     * @return DESCRIZIONE
     */
    public String getDESCRIZIONE() {
        return DESCRIZIONE;
    }


    /**
     * Sets the DESCRIZIONE value for this TableStructure.
     *
     * @param DESCRIZIONE
     */
    public void setDESCRIZIONE(String DESCRIZIONE) {
        this.DESCRIZIONE = DESCRIZIONE;
    }


    /**
     * Gets the NOME_SCHEMA value for this TableStructure.
     *
     * @return NOME_SCHEMA
     */
    public String getNOME_SCHEMA() {
        return NOME_SCHEMA;
    }


    /**
     * Sets the NOME_SCHEMA value for this TableStructure.
     *
     * @param NOME_SCHEMA
     */
    public void setNOME_SCHEMA(String NOME_SCHEMA) {
        this.NOME_SCHEMA = NOME_SCHEMA;
    }

    private Object __equalsCalc = null;

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof TableStructure)) return false;
        TableStructure other = (TableStructure) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = ((this.NOME == null && other.getNOME() == null) ||
                (this.NOME != null &&
                        this.NOME.equals(other.getNOME()))) &&
                ((this.TIPO == null && other.getTIPO() == null) ||
                        (this.TIPO != null &&
                                this.TIPO.equals(other.getTIPO()))) &&
                ((this.SOTTOTIPO == null && other.getSOTTOTIPO() == null) ||
                        (this.SOTTOTIPO != null &&
                                this.SOTTOTIPO.equals(other.getSOTTOTIPO()))) &&
                this.LUNGHEZZA == other.getLUNGHEZZA() &&
                ((this.ETICHETTA == null && other.getETICHETTA() == null) ||
                        (this.ETICHETTA != null &&
                                this.ETICHETTA.equals(other.getETICHETTA()))) &&
                this.LARGHEZZA == other.getLARGHEZZA() &&
                this.ALLINEAMENTO == other.getALLINEAMENTO() &&
                this.FLAG_TOTAL == other.isFLAG_TOTAL() &&
                this.FLAG_NO_DISPLAY == other.isFLAG_NO_DISPLAY() &&
                ((this.DESCRIZIONE == null && other.getDESCRIZIONE() == null) ||
                        (this.DESCRIZIONE != null &&
                                this.DESCRIZIONE.equals(other.getDESCRIZIONE()))) &&
                ((this.NOME_SCHEMA == null && other.getNOME_SCHEMA() == null) ||
                        (this.NOME_SCHEMA != null &&
                                this.NOME_SCHEMA.equals(other.getNOME_SCHEMA())));
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
        if (getNOME() != null) {
            _hashCode += getNOME().hashCode();
        }
        if (getTIPO() != null) {
            _hashCode += getTIPO().hashCode();
        }
        if (getSOTTOTIPO() != null) {
            _hashCode += getSOTTOTIPO().hashCode();
        }
        _hashCode += getLUNGHEZZA();
        if (getETICHETTA() != null) {
            _hashCode += getETICHETTA().hashCode();
        }
        _hashCode += Double.valueOf(getLARGHEZZA()).hashCode();
        _hashCode += getALLINEAMENTO();
        _hashCode += (isFLAG_TOTAL() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFLAG_NO_DISPLAY() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDESCRIZIONE() != null) {
            _hashCode += getDESCRIZIONE().hashCode();
        }
        if (getNOME_SCHEMA() != null) {
            _hashCode += getNOME_SCHEMA().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
            new TypeDesc(TableStructure.class, true);

    static {
        typeDesc.setXmlType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "crm_web.SW_STRUTTURA"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("NOME");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "NOME"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("TIPO");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "TIPO"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("SOTTOTIPO");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "SOTTOTIPO"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("LUNGHEZZA");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "LUNGHEZZA"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("ETICHETTA");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ETICHETTA"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("LARGHEZZA");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "LARGHEZZA"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("ALLINEAMENTO");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ALLINEAMENTO"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("FLAG_TOTAL");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "FLAG_TOTAL"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("FLAG_NO_DISPLAY");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "FLAG_NO_DISPLAY"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("DESCRIZIONE");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "DESCRIZIONE"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("NOME_SCHEMA");
        elemField.setXmlName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "NOME_SCHEMA"));
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
