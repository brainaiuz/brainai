/**
 * OmtypeLocator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

public class OmtypeLocator extends Service implements Omtype {

    public OmtypeLocator() {
    }

    public OmtypeLocator(EngineConfiguration config) {
        super(config);
    }

    public OmtypeLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for eventiPort
    private String port_address = "http://93.63.196.160:1968/crm_web/eventi";

    public String getPortAddress() {
        return port_address;
    }

    // The WSDD service name defaults to the port name.
    private String eventiPortWSDDServiceName = "eventiPort";

    public String geteventiPortWSDDServiceName() {
        return eventiPortWSDDServiceName;
    }

    public void seteventiPortWSDDServiceName(String name) {
        eventiPortWSDDServiceName = name;
    }

    public PortType getPort() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(port_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getPort(endpoint);
    }

    public PortType getPort(String port_address) throws ServiceException {
        this.port_address = port_address;
        URL endpoint;
        try {
            endpoint = new URL(port_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getPort(endpoint);
    }

    public PortType getPort(URL portAddress) throws ServiceException {
        try {
            BindingStub _stub = new BindingStub(portAddress, this);
            _stub.setPortName(geteventiPortWSDDServiceName());
            return _stub;
        } catch (AxisFault e) {
            return null;
        }
    }

    public void seteventiPortEndpointAddress(String address) {
        port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                BindingStub _stub = new BindingStub(new URL(port_address), this);
                _stub.setPortName(geteventiPortWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("eventiPort".equals(inputPortName)) {
            return getPort();
        } else {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "eventi_omtype");
    }

    private HashSet ports = null;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://93.63.196.160:1968/crm_web/eventi.wsdl", "eventiPort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws ServiceException {

        if ("eventiPort".equals(portName)) {
            seteventiPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
