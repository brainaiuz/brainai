/**
 * Omtype.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.edatasite.workforce.gwt.core.server.target;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface Omtype extends Service {
    String getPortAddress();

    PortType getPort() throws ServiceException;

    PortType getPort(String port_address) throws ServiceException;

    PortType getPort(URL portAddress) throws ServiceException;
}
