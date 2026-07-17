package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.org.json;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 15:05:15
 * To change this template use File | Settings | File Templates.
 */
public interface JSONString {
	/**
	 * The <code>toJSONString</code> method allows a class to produce its own JSON
	 * serialization.
	 *
	 * @return A strictly syntactically correct JSON text.
	 */
    String toJSONString();
}
