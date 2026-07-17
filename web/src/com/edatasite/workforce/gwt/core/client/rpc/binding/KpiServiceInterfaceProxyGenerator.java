package com.edatasite.workforce.gwt.core.client.rpc.binding;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

/**
 * Created by Dilsh0d Madrahimov on 10/29/2018.
 */
public class KpiServiceInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {
    @Override
    protected ProxyCreator createProxyCreator(JClassType remoteService) {
        return new KpiProxyCreator(remoteService);
    }
}