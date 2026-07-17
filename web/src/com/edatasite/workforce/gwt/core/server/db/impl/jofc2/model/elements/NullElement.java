package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.elements;

import java.io.Serializable;

import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.metadata.Converter;
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.util.NullConverter;

@Converter(NullConverter.class)
public class NullElement implements Serializable {

	@Override
	public String toString() {
		return null;
	}
}
