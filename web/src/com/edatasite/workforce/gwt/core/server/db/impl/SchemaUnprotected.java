package com.edatasite.workforce.gwt.core.server.db.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Anvarbek
 * Date: Aug 9, 2010
 * Time: 8:11:33 PM
 *
 * Warning!!!!!!!!
 * This security tag annotation points that the particular method is not invocation safe, i.e.
 * can be used to fetch data from any unrelated schema
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaUnprotected {
}
