package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.gwt.core.server.security.XSSFilter;
import org.gwtwidgets.server.spring.RPCServiceExporter;
import org.gwtwidgets.server.spring.RPCServiceExporterFactory;

public class WfmServiceExporterFactoryBean implements RPCServiceExporterFactory {
    private XSSFilter xssFilter;

    public WfmServiceExporterFactoryBean(XSSFilter xssFilter) {
        super();
        this.xssFilter = xssFilter;
    }

    public RPCServiceExporter create() {
        return new WfmGWTRPCServiceExporter(xssFilter);
    }


}
