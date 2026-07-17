package com.edatasite.workforce.gwt.core.client.ui.customlist;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Mar 30, 2010
 * Time: 6:37:19 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This enumeration indicates that how the list should be drawn.
 * According to its value related conception would be applied to
 * the list in order to draw proper view.
 */
public enum Design {

    /**
     * This value is list's default value that only draws simple
     * list view and no extra conceptions will be used.
     */
    NONE,

    /**
     * This value permits to draw the list with checkboxes that supports
     * their functionality. Only enabling this value allows list to
     * utilize checkbox's proper methods.
     */
    CHECK,

    /**
     * This value permits to draw the list with trees that supports their
     * functionality. Showing this value through building list allows
     * to add children to the tree, otherwise no successful adaption
     * will be observed.
     */
    TREE,

    /**
     * This value permits to draw the list with delete option. Current value
     * allows to delete items from the list.
     */
    DELETE
}