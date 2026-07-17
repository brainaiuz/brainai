package com.edatasite.workforce.gwt.core.client;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 20.06.2009
 * Time: 16:19:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class DisableProvider {
    public boolean completed = false;

    public abstract void enable();

    public abstract void disable();
}
