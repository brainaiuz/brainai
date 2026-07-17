package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:14:14
 */
public interface Oracle {

    /**
     * Returns the strongly typed value for the specified display text.
     *
     * @param displayText
     * @return the strongly typed value
     */
    String getValue(String displayText);

    /**
     * Returns the display text for the specified value.
     *
     * @param value
     * @return the display text
     */
    String getDisplayText(String value);
}
