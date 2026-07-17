/**
 * BindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.ser.*;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Vector;

public class BindingStub extends Stub implements PortType {
    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    static OperationDesc[] _operations;

    static {
        _operations = new OperationDesc[22];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }

    private static void _initOperationDesc1() {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName("AggiornaRecord");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRecordXml"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParChiave"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[0] = oper;

        oper = new OperationDesc();
        oper.setName("AllegaFile");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRecordXml"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParFile"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;

        oper = new OperationDesc();
        oper.setName("CancellaRecord");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParChiave"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[2] = oper;

        oper = new OperationDesc();
        oper.setName("ConnessioniAttive");
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[3] = oper;

        oper = new OperationDesc();
        oper.setName("ControlloOperazione");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParOperazione"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "shortinteger"), int.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRecordXml"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[4] = oper;

        oper = new OperationDesc();
        oper.setName("EseguiOperazione");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParOperazione"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParParametro1"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParParametro2"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParParametro3"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParParametro4"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParParametro5"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[5] = oper;

        oper = new OperationDesc();
        oper.setName("GestisciLibreria");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParLibreria"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParOperazione"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParControllo"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[6] = oper;

        oper = new OperationDesc();
        oper.setName("InserisciRecord");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRecordXml"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[7] = oper;

        oper = new OperationDesc();
        oper.setName("LeggiAllegato");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParChiave"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParAlt"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParLar"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[8] = oper;

        oper = new OperationDesc();
        oper.setName("leggiLista");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParElencoCampi"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTipoLettura"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "shortinteger"), int.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParWhere"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParSort"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParNumRecord"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParCampo1"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParCampo2"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParCampo3"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2() {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName("leggiSingolo");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParChiave"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[10] = oper;

        oper = new OperationDesc();
        oper.setName("leggiStrutturaBase");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "crm_web.SW_STRUTTURA.list"));
        oper.setReturnClass(TableStructure[].class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "row"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[11] = oper;

        oper = new OperationDesc();
        oper.setName("leggiStrutturaLista");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[12] = oper;

        oper = new OperationDesc();
        oper.setName("leggiStrutturaSingolo");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[13] = oper;

        oper = new OperationDesc();
        oper.setName("ListaMinings");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[14] = oper;

        oper = new OperationDesc();
        oper.setName("ListaStampe");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[15] = oper;

        oper = new OperationDesc();
        oper.setName("login_new");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParUtente"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParPassword"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParControllo"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParPersonal"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "login_new.return"));
        oper.setReturnClass(LoginResponse.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[16] = oper;

        oper = new OperationDesc();
        oper.setName("logout");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[17] = oper;

        oper = new OperationDesc();
        oper.setName("Richiesta");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRichiestaConnessione"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParRichiestaParametri"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[18] = oper;

        oper = new OperationDesc();
        oper.setName("StampaLista");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "_connectionid"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParTabella"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParReport"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParListaXml"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3() {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName("test");
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[20] = oper;

        oper = new OperationDesc();
        oper.setName("UtentiConnessi");
        param = new ParameterDesc(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ParControllo"), ParameterDesc.IN, new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[21] = oper;

    }

    public BindingStub() throws AxisFault {
        this(null);
    }

    public BindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public BindingStub(Service service) throws AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.1");
        Class cls;
        QName qName;
        QName qName2;
        Class beansf = BeanSerializerFactory.class;
        Class beandf = BeanDeserializerFactory.class;
        Class enumsf = EnumSerializerFactory.class;
        Class enumdf = EnumDeserializerFactory.class;
        Class arraysf = ArraySerializerFactory.class;
        Class arraydf = ArrayDeserializerFactory.class;
        Class simplesf = SimpleSerializerFactory.class;
        Class simpledf = SimpleDeserializerFactory.class;
        Class simplelistsf = SimpleListSerializerFactory.class;
        Class simplelistdf = SimpleListDeserializerFactory.class;
        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "crm_web.SW_STRUTTURA");
        cachedSerQNames.add(qName);
        cls = TableStructure.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "crm_web.SW_STRUTTURA.list");
        cachedSerQNames.add(qName);
        cls = TableStructure[].class;
        cachedSerClasses.add(cls);
        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "crm_web.SW_STRUTTURA");
        qName2 = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "row");
        cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new ArrayDeserializerFactory());

        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "login_new.return");
        cachedSerQNames.add(qName);
        cls = LoginResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "shortinteger");
        cachedSerQNames.add(qName);
        cls = int.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(BaseSerializerFactory.createFactory(SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(BaseDeserializerFactory.createFactory(SimpleDeserializerFactory.class, cls, qName));

        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_32");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(BaseSerializerFactory.createFactory(SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(BaseDeserializerFactory.createFactory(SimpleDeserializerFactory.class, cls, qName));

        qName = new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "string_max");
        cachedSerQNames.add(qName);
        cls = String.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(BaseSerializerFactory.createFactory(SimpleSerializerFactory.class, cls, qName));
        cachedDeserFactories.add(BaseDeserializerFactory.createFactory(SimpleDeserializerFactory.class, cls, qName));

    }

    protected Call createCall() throws RemoteException {
        try {
            Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        QName qName =
                                (QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)
                                    cachedSerFactories.get(i);
                            Class df = (Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            SerializerFactory sf = (SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (java.lang.Throwable _t) {
            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public String updateRecord(String _connectionid, String table, String recordXml, String key) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "AggiornaRecord"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, table, recordXml, key});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String attachFile(String _connectionid, String parTabella, String parRecordXml, byte[] parFile) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "AllegaFile"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parRecordXml, parFile});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String deleteRecord(String _connectionid, String table, String key) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "CancellaRecord"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, table, key});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public int activeConnections() throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ConnessioniAttive"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (Integer) _resp;
                } catch (Exception _exception) {
                    return (Integer) JavaUtils.convert(_resp, int.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String controlloOperazione(String _connectionid, String parTabella, int parOperazione, String parRecordXml) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ControlloOperazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parOperazione, parRecordXml});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String eseguiOperazione(String _connectionid, String parTabella, String parOperazione, String parParametro1, String parParametro2, String parParametro3, String parParametro4, String parParametro5) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "EseguiOperazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parOperazione, parParametro1, parParametro2, parParametro3, parParametro4, parParametro5});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public int libraryManagers(String parLibreria, int parOperazione, String parControllo) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "GestisciLibreria"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{parLibreria, parOperazione, parControllo});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (Integer) _resp;
                } catch (Exception _exception) {
                    return (Integer) JavaUtils.convert(_resp, int.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String insertRecord(String _connectionid, String table, String recordXml) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "InserisciRecord"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, table, recordXml});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public byte[] getAttachment(String _connectionid, String parTabella, String parChiave, String parAlt, String parLar) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "LeggiAllegato"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parChiave, parAlt, parLar});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (byte[]) _resp;
                } catch (Exception _exception) {
                    return (byte[]) JavaUtils.convert(_resp, byte[].class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String getListRecords(String _connectionid, String table, String fieldList, int readType, String where, String sort, int numOfRecord, String field1, String field2, String field3) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "leggiLista"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, table, fieldList, readType, where, sort, numOfRecord, field1, field2, field3});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String getSingleRecord(String _connectionid, String parTabella, String parChiave) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "leggiSingolo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parChiave});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public TableStructure[] getTableStructure(String _connectionid, String table) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "leggiStrutturaBase"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, table});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (TableStructure[]) _resp;
                } catch (Exception _exception) {
                    return (TableStructure[]) JavaUtils.convert(_resp, TableStructure[].class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String getListStructure(String _connectionid, String parTabella) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "leggiStrutturaLista"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String getSingleRecordStructure(String _connectionid, String parTabella) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "leggiStrutturaSingolo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String listaMinings(String _connectionid, String parTabella) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ListaMinings"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String getReports(String _connectionid, String parTabella) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "ListaStampe"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public LoginResponse login(String username, String password, String controller, String personal) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "login_new"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{username, password, controller, personal});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (LoginResponse) _resp;
                } catch (Exception _exception) {
                    return (LoginResponse) JavaUtils.convert(_resp, LoginResponse.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public int logout(String _connectionid) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "logout"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (Integer) _resp;
                } catch (Exception _exception) {
                    return (Integer) JavaUtils.convert(_resp, int.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String request(String parRichiestaConnessione, String parRichiestaParametri) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "Richiesta"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{parRichiestaConnessione, parRichiestaParametri});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public byte[] stampaLista(String _connectionid, String parTabella, String parReport, String parListaXml) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "StampaLista"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{_connectionid, parTabella, parReport, parListaXml});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (byte[]) _resp;
                } catch (Exception _exception) {
                    return (byte[]) JavaUtils.convert(_resp, byte[].class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String test() throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "test"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public String onlineUsers(String controller) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://127.0.0.1:1968");
        _call.setEncodingStyle(null);
        _call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName(cachedEndpoint.toString() + ".wsdl", "UtentiConnessi"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{controller});

            if (_resp instanceof RemoteException) {
                throw (RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
