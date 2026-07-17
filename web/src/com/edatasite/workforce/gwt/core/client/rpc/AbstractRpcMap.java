package com.edatasite.workforce.gwt.core.client.rpc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Virus on 7/22/14.
 */
public abstract class AbstractRpcMap {

	protected HashMap<String, String> getInstance() {
		return null;
	}

    protected void addString(String typeID, String entityID) {
        if (entityID != null){
            getInstance().put(typeID, entityID);
        }else{
            getInstance().remove(typeID);
        }
    }

	protected String getString(String type) {
		return getInstance().get(type);
	}

	protected void addInteger(String typeID, Integer entity) {
		if (entity == null) {
			addString(typeID, null);
		} else {
			addString(typeID, String.valueOf(entity));
		}
	}

	protected Integer getInteger(String type) {
		if (getString(type) == null) {
			return null;
		}
		return Integer.valueOf(getString(type));
	}

	protected void addInt(String typeID, int entity) {
		addInteger(typeID, entity);
	}

	protected int getInt(String type) {
		if (getString(type) == null) {
			return 0;
		}
		return getInteger(type) == null ? 0 : getInteger(type);
	}

	protected void addLong(String typeID, Long entity) {
		if (entity == null) {
			addString(typeID, null);
		} else {
			addString(typeID, String.valueOf(entity));
		}
	}

	protected Long getLong(String type) {
		if (getString(type) == null) {
			return null;
		}
		return Long.valueOf(getString(type));
	}

	protected void addDouble(String typeID, Double entity) {
		if (entity == null) {
			addString(typeID, null);
		} else {
			addString(typeID, String.valueOf(entity));
		}
	}

	protected Double getDouble(String type) {
		if (getString(type) == null) {
			return null;
		}
		return Double.valueOf(getString(type));
	}

	protected void addBigDecimal(String typeID, BigDecimal entity) {
		if (entity == null) {
			addString(typeID, null);
		} else {
			addString(typeID, String.valueOf(entity));
		}
	}

	protected BigDecimal getBigDecimal(String type) {
		if (getString(type) == null) {
			return null;
		}

		return new BigDecimal(getString(type));
	}

	protected void add_long(String typeID, long entity) {
		addLong(typeID, entity);
	}

	protected long get_long(String typeID) {
		return getLong(typeID) == null ? 0 : getLong(typeID);
	}

	protected void addDate(String typeID, Date entity, boolean withOffset) {
		if (entity == null) {
			addString(typeID, null);
		} else if (withOffset){
			addString(typeID, JsonDateUtils.setDate(entity));
		} else {
            addString(typeID, JsonDateUtils.setNonConvertableDate(entity));
        }
	}

	protected Date getDate(String type) {
		return JsonDateUtils.getDate(getString(type));
	}

	protected Date getNonConvertibleDate(String type) {
		return JsonDateUtils.getNonConvertableDate(getString(type));
	}

	protected void addBoolean(String typeID, Boolean entity) {
		if (entity == null) {
			addString(typeID, null);
		} else {
			addString(typeID, String.valueOf(entity));
		}
	}

	public Boolean getBoolean(String type) {
		if (getString(type) == null) {
			return null;
		}
		return Boolean.valueOf(getString(type));
	}

	protected boolean getBool(String typeID) {
		return Boolean.TRUE.equals(getBoolean(typeID));
	}

	protected void addBool(String typeID, boolean entity) {
		addBoolean(typeID, entity);
	}


}
