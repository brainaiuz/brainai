package com.edatasite.workforce.gwt.core.client.ui.filterparams;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 22, 2010
 * Time: 3:55:57 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This interface is responsible for any type of handling. It catches events
 * and carries necessary value with them. Further actions will depend on that
 * value. Working process is like com.google.gwt.user.client.Command and
 * there is one advantage that you can send any information with it.
 *
 * @param <T> - target, in order to use proper logic or conception.
 */
public interface SuperPuperHandler<T> {

    void onFire(T target);
}
