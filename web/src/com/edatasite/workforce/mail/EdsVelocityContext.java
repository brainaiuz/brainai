package com.edatasite.workforce.mail;

import org.apache.velocity.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EdsVelocityContext implements Context {

    private Map<String, Object> localValues = Collections.synchronizedMap(new HashMap<>());

    public Object put(String string, Object object) {
        Object result = localValues.get(string);
        localValues.put(string, object);
        return result;
    }

    public Object get(String string) {
        return localValues.get(string);
    }

    public boolean containsKey(Object object) {
        return localValues.containsKey(object);
    }

    public Object[] getKeys() {
        return new Object[0];
    }

    public Object remove(Object object) {
        Object result = localValues.get(object);
        localValues.remove(object);
        return result;
    }
}
