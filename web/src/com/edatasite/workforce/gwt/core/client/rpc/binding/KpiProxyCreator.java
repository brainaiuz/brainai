package com.edatasite.workforce.gwt.core.client.rpc.binding;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;

/**
 * Created by Dilsh0d Madrahimov on 10/29/2018.
 */
public class KpiProxyCreator extends ProxyCreator {

    public KpiProxyCreator(JClassType serviceIntf) {
        super(serviceIntf);
    }

    @Override
    protected Class<? extends RemoteServiceProxy> getProxySupertype() {
        return KpiRemoteServiceProxy.class;
    }
}