package com.edatasite.shared.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Entity;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Artem Pak
 * User: artem
 * Date: 09.01.2006
 * Time: 13:49:49
 * Software Team
 */
public abstract class EdsObject implements EdsClass, Serializable, Cloneable {

    public static final int SYSTEM_BUILTIN = 1;// system defined builtin
    public static final int BUILT_IN = 2; // built in object indicates that object has been creaeted by built-in system settings
    public static final int INHERITED = 3;// indicates that object has been created due to inheritence from parent object
    public static final int CUSTOM = 4;// custom object indicates that obeject created by customized settings
    public static final int DEFAULT = 5;//indicates that object has been created by company or other defult settings
    public static final int TEMP = 6;//temp folder

    public static String getEntityName(EdsObject o) {
        if (o instanceof HibernateProxy proxy) {
            return proxy.getHibernateLazyInitializer().getEntityName();
        }
        Entity entity = o.getClass().getAnnotation(Entity.class);
        if (entity != null) {
            return !"".equals(entity.name()) ? entity.name() : o.getClass().getName();
        }
        return "";
    }

    public static <T extends EdsObject> ArrayList<Integer> getObjectIDs(List<T> list) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (T domainObject : list) {
                if (domainObject != null && domainObject.getObjectID() != null) {
                    ids.add(domainObject.getObjectID());
                }
            }
        }
        return ids;
    }

    /**
     * this method must be overridden if there is no @name field in T
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends EdsObject> ArrayList<SelectItem> getAsSelectItems(List<T> list) {
        if (list != null && list.size() > 0) {
            ArrayList<SelectItem> result = new ArrayList<>();
            for (T t : list) {
                result.add(new SelectItem(t.getObjectID(), t.getName()));
            }
            return result;
        }
        return null;
    }

    /**
     * @param namableObjects must be domain objects which have name field...
     * @param ids
     * @return
     */
    public static <T extends EdsObject> List<String> getNames(Map<Integer, T> namableObjects, Integer... ids) {
        List<String> names = new ArrayList<>();
        if (ids != null) {
            for (Integer id : ids) {
                if (namableObjects.containsKey(id)) {
                    T namableObject = namableObjects.get(id);
                    if (namableObject != null) {
                        names.add(namableObject.getName());
                    }
                }
            }
        }
        return names;
    }

    /**
     * list must be type of List<Eds...>
     *
     * @param list
     * @return
     */
    public static <E extends EdsObject> Map<Integer, E> getListAsMapIntegerAndValue(Collection<E> list) {
        Map<Integer, E> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (E object : list) {
                map.put(object.getObjectID(), object);
            }
        }
        return map;
    }

    public abstract Integer getObjectID();

    public String getName() {
        return toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdsObject unObject)) {
            return false;
        }
        if (getObjectID() == null) {
            return false;
        }

        String table1 = getEntityName(this);
        String table2 = getEntityName(unObject);
        return !(table1 == null || !table1.equals(table2)) && getObjectID().equals(unObject.getObjectID());
    }

    public int hashCode() {
        return (getObjectID() != null ? getObjectID().hashCode() : 0);
    }

    public String toString() {
        return "[objectID=" + getObjectID() + "]";
    }

    public Boolean isNew() {
        return getObjectID() == null;
    }

    public Boolean hasAccess(EdsUser user) {
        return null;
    }

    private <T extends EdsObject> T getShallowClonedObject(T obj) {
        Class<? extends EdsObject> cls = obj.getClass();
        while (cls != null) {
            for (Field f : cls.getDeclaredFields()) {
                f.setAccessible(true);
                if (Collection.class.isAssignableFrom(f.getType())) {
                    try {
                        Object o = f.get(obj);
                        if (o != null) {
                            try {
                                Object newInstance = o.getClass().newInstance();
                                f.set(obj, newInstance);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (f.getName().equalsIgnoreCase("objectid")) {
                    try {
                        f.set(obj, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            cls = (Class<? extends EdsObject>) cls.getSuperclass();
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public <T extends EdsObject> T cloneShallow() {
        T clone = null;
        try {
            clone = (T) super.clone();
            clone = getShallowClonedObject(clone);
            if (clone instanceof ObjectIdentifier) {
                ((ObjectIdentifier) clone).setObjectKey(null);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), getName());
    }

    public TreeSelectItem getAsTreeSelectItem() {
        return new TreeSelectItem(getObjectID(), getName());
    }

    protected final String refactorNulls(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("\0", " ");
        }
        return s;
    }

    public String getDomainType() {
        return null;
    }
}
