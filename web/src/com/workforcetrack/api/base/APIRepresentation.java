package com.workforcetrack.api.base;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 17.05.12
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public interface APIRepresentation {

    Map<String, Object> getAsMap(String... ignoreFields);

    List<String> getFieldsName();
}
