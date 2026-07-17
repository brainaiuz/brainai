package com.edatasite.workforce.gwt.core.server.servlets;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.Map;

public class WfmRegistrar implements PropertyEditorRegistrar {

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /**
     * @return the beanClassLoader
     */
    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    private Map customEditors;

    /**
     * @return the customEditors
     */
    public Map getCustomEditors() {
        return customEditors;
    }

    /**
     * @param customEditors the customEditors to set
     */
    public void setCustomEditors(Map customEditors) {
        this.customEditors = customEditors;
    }

    public void registerCustomEditors(PropertyEditorRegistry binder) {
        if (customEditors != null) {
            for (Object o : customEditors.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object key = entry.getKey();
                Class requiredType = null;
                if (key instanceof Class) {
                    requiredType = (Class) key;
                } else if (key instanceof String) {
                    String className = (String) key;
                    requiredType = ClassUtils.resolveClassName(className,
                            beanClassLoader);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid key ["
                                    + key
                                    + "] for custom editor: needs to be Class or String.");
                }
                Object value = entry.getValue();
                if (!(value instanceof PropertyEditor)) {
                    throw new IllegalArgumentException("Mapped value [" + value
                            + "] for custom editor key [" + key
                            + "] is not of required type ["
                            + PropertyEditor.class.getName() + "]");
                }
                binder.registerCustomEditor(requiredType,
                        (PropertyEditor) value);
            }
        }
    }


}
