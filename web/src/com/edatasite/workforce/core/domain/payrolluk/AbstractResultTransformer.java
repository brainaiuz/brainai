package com.edatasite.workforce.core.domain.payrolluk;

import org.hibernate.transform.ResultTransformer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResultTransformer implements ResultTransformer {

    protected static final Map<String, Integer> aliasMap = new HashMap<>();

    protected void initAliasMap(String[] aliases) {
        aliasMap.clear();
        int index = 0;
        for (String alias : aliases) {
            aliasMap.put(alias, index);
            index++;
        }
    }

    protected static Integer getAlias(String val) {
        return aliasMap.get(val);
    }

    protected static Integer getInteger(Object[] tuple, String alias) {
        return (Integer) tuple[getAlias(alias)];
    }

    protected static Long getLong(Object[] tuple, String alias) {
        Object data = tuple[getAlias(alias)];
        if (data != null) {
            if (data instanceof BigInteger) {
                return ((BigInteger) data).longValue();
            } else if (data instanceof Long) {
                return (Long) data;
            }
        }
        return null;
    }

    protected static BigDecimal getBigDecimal(Object[] tuple, String alias) {
        Object data = tuple[getAlias(alias)];
        if (data != null) {
            if (data instanceof BigDecimal) {
                return (BigDecimal) data;
            } else if (data instanceof Double) {
                return BigDecimal.valueOf((Double) data);
            }
        }
        return null;
    }

    protected static Double getDouble(Object[] tuple, String alias) {
        Object data = tuple[getAlias(alias)];
        if (data != null) {
            if (data instanceof BigDecimal) {
                return ((BigDecimal) data).doubleValue();
            } else if (data instanceof Double) {
                return (Double) data;
            }
        }
        return null;
    }

    protected static String getString(Object[] tuple, String alias) {
        return (String) tuple[getAlias(alias)];
    }

    protected static Boolean getBoolean(Object[] tuple, String alias) {
        return (Boolean) tuple[getAlias(alias)];
    }

    protected static Date getDate(Object[] tuple, String alias) {
        return (Date) tuple[getAlias(alias)];
    }
}
