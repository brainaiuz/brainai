package com.finnetlimited.reportservice.core.client.gwtrpc;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 20:46:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class DisableProvider {
    public boolean completed = false;

    public abstract void enable();

    public abstract void disable();
}
