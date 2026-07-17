package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.12.2008
 * Time: 11:43:19
 * To change this template use File | Settings | File Templates.
 */

/**
 * Each transfer object that is used as the part of firstRequest() method of RoleProvider interface should implement
 * this interface
 */
public interface UserGrant {
    void setPermission(int permission);

    int getPermission();

}
