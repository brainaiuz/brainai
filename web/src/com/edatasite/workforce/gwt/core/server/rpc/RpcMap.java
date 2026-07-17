package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractRpcMap;
import com.edatasite.workforce.gwt.core.client.rpc.JsonDateUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Virus on 11/15/14.
 */
public class RpcMap extends AbstractRpcMap {
    private HashMap<String, String> valueMap = null;
    private HashMap<String, Object> childMap = null;

    public HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public HashMap<String, Object> getChild() {
        return childMap = childMap == null ? new HashMap<>() : childMap;
    }

    public void addChild(String type, Object entity) {
        if (entity != null)
            getChild().put(type, entity);
    }
    /*public void addString(String typeID, String entityID) {
        if (entityID != null)
            getInstance().put(typeID, entityID);
    }

    public String getString(String type) {
        return super.getString(type);
    }

    public void addInteger(String typeID, Integer entity) {
        super.addInteger(typeID, entity);
    }

    public Integer getInteger(String type) {
        return super.getInteger(type);
    }

    public void addInt(String typeID, int entity) {
        super.addInt(typeID, entity);
    }

    public int getInt(String type) {
        return super.getInt(type);
    }

    public void addLong(String typeID, Long entity) {
        super.addLong(typeID, entity);
    }

    public Long getLong(String type) {
        return super.getLong(type);
    }

    public void addDouble(String typeID, Double entity) {
        super.addDouble(typeID, entity);
    }

    public Double getDouble(String type) {
        return super.getDouble(type);
    }

    public void add_long(String typeID, long entity) {
        super.add_long(typeID, entity);
    }

    public long get_long(String typeID) {
        return super.get_long(typeID);
    }

    public void addDate(String typeID, Date entity) {
        super.addDate(typeID, entity);
    }

    public Date getDate(String type) {
        return super.getDate(type);
    }

    public void addBoolean(String typeID, Boolean entity) {
        super.addBoolean(typeID, entity);
    }

    public Boolean getBoolean(String type) {
        return super.getBoolean(type);
    }

    public boolean getBool(String typeID) {
        return super.getBool(typeID);
    }

    public void addBool(String typeID, boolean entity) {
        super.addBool(typeID, entity);
    }*/

    public static <T> RpcMap get(T t) throws Exception {
        if (t == null) {
            return null;
        }
        RpcMap rpcMap = new RpcMap();
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object o = field.get(t);
            if (o == null || "objectid".equals(field.getName().toLowerCase())) {
                continue;
            }
            if (o instanceof String) {
                rpcMap.addString(field.getName(), field.get(t).toString());
            } else if (o instanceof Integer) {
                rpcMap.addInteger(field.getName(), (Integer) field.get(t));
            } else if (o instanceof Boolean) {
                rpcMap.addBoolean(field.getName(), (Boolean) field.get(t));
            } else if (o instanceof Date) {
                rpcMap.addDate(field.getName(), (Date) field.get(t), true);
            } else if (o instanceof Double) {
                rpcMap.addDouble(field.getName(), (Double) field.get(t));
            } else if (int.class.getSimpleName().equals(field.getType().getSimpleName())) {
                rpcMap.addInt(field.getName(), (Integer) field.get(t));
            } else if (boolean.class.getSimpleName().equals(field.getType().getSimpleName())) {
                rpcMap.addBool(field.getName(), (Boolean) field.get(t));
            } else if (double.class.getSimpleName().equals(field.getType().getSimpleName())) {
                rpcMap.addDouble(field.getName(), (Double) field.get(t));
            } else if (o instanceof EdsAuditInfo) {
                EdsAuditInfo auditInfo = ((EdsAuditInfo) o);
                rpcMap.addDate("creationDate", auditInfo.getCreationDate(), true);
                rpcMap.addDate("modificationDate", auditInfo.getModificationDate(), true);
            }
        }
        return rpcMap;
    }

    public static <T> void set(HashMap rpcMap, T t) throws Exception {
        if (!(rpcMap == null || rpcMap.get("valueMap") == null)) {
            for (Field field : t.getClass().getDeclaredFields()) {
                Object obj = ((HashMap) rpcMap.get("valueMap")).get(field.getName());
                if (obj == null && null != rpcMap.get("childMap")) {
                    obj = ((HashMap) rpcMap.get("childMap")).get(field.getName());
                    if (obj != null) {
                        Object o = field.getType().newInstance();
                        field.setAccessible(true);
                        field.set(t, o);
                        set((HashMap) obj, o);
                        continue;
                    }
                }
                if (obj != null) {
                    field.setAccessible(true);
                    if (String.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, obj);
                    } else if (Integer.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Integer.valueOf((String) obj));
                    } else if (Boolean.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Boolean.valueOf((String) obj));
                    } else if (Date.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, JsonDateUtils.getDate((String) obj));
                    } else if (Double.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Double.valueOf((String) obj));
                    } else if (int.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Integer.valueOf((String) obj));
                    } else if (boolean.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Boolean.valueOf((String) obj));
                    } else if (double.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        field.set(t, Double.valueOf((String) obj));
                    } else if (EdsAuditInfo.class.getSimpleName().equals(field.getType().getSimpleName())) {
                        EdsAuditInfo auditInfo = ((EdsAuditInfo) obj);
                        ((EdsAuditInfo) field.get(t)).setCreationDate(auditInfo.getCreationDate());
                        ((EdsAuditInfo) field.get(t)).setModificationDate(auditInfo.getModificationDate());
                    }
                } else if (EdsAuditInfo.class.getSimpleName().equals(field.getType().getSimpleName())) {
                    EdsAuditInfo auditInfo = ((EdsAuditInfo) obj);
                    if (auditInfo == null) auditInfo = new EdsAuditInfo();
                    auditInfo.setCreationDate(JsonDateUtils.getDate("" +  ((HashMap) rpcMap.get("valueMap")).get("creationDate")));
                    auditInfo.setModificationDate(JsonDateUtils.getDate("" + ((HashMap) rpcMap.get("valueMap")).get("modificationDate")));
//                    ((EdsAuditInfo) field.get(t)).setCreationDate(auditInfo.getCreationDate());
//                    ((EdsAuditInfo) field.get(t)).setModificationDate(auditInfo.getModificationDate());
                    field.setAccessible(true);
                    field.set(t, auditInfo);
                }
            }
        }
    }
}