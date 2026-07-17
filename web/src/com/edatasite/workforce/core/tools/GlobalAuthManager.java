package com.edatasite.workforce.core.tools;

/**
 * Created by IntelliJ IDEA.
 * User: sher
 * Date: 2/5/11
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GlobalAuthManager {

    <T> T executeQuery(GlobalAuthConnectionManagerImpl.Callback<T> callback, String query, Object... params);
}
