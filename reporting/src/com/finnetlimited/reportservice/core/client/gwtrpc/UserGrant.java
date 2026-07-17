package com.finnetlimited.reportservice.core.client.gwtrpc;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 16:16:08
 * To change this template use File | Settings | File Templates.
 */
public interface UserGrant {
    void setPermission(int permission);

    int getPermission();
}
