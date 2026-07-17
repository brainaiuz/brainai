package com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;

import java.util.Date;

/**
 * This class need to Encript in Server site
 * User: ${Azam}
 * Date: 03.03.2020
 */
public final class RpcConvertToXmlLocal extends RpcConvertToXml {

    public RpcConvertToXmlLocal(ReportRpc report) {
        this.report = report;
    }

    public RpcConvertToXmlLocal() {
    }

    protected String getData(String data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return ServerUtils.encrypt(dt);
    }

    protected String getData(Boolean data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return ServerUtils.encrypt(dt);
    }

    protected String getData(Integer data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return ServerUtils.encrypt(dt);
    }

    protected String getData(Date data) {
        String dt = "";
        if (data != null) {
            dt += data;
        }
        return ServerUtils.encrypt(dt);
    }
}