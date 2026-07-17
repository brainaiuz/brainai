package com.workforcetrack.api.aspects;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.05.12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRequest {

    boolean checkSession() default true;
}
