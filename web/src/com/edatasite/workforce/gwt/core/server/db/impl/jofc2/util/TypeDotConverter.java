package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.util;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 14:52:29
 * To change this template use File | Settings | File Templates.
 */
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.elements.LineChart;

public class TypeDotConverter extends ConverterBase<LineChart.Style> {

	@Override
	public void convert(LineChart.Style o, PathTrackingWriter writer, MarshallingContext mc) {
		writeNode(writer, "type", o.getType(), false);
		writeNode(writer, "colour", o.getColour(), true);
		writeNode(writer, "dot-size", o.getDotSize(), true);
		writeNode(writer, "halo-size", o.getHaloSize(), true);
		writeNode(writer, "rotation", o.getRotation(), true);
		writeNode(writer, "hallow", o.getHallow(), true);
		writeNode(writer, "sides", o.getSides(), true);
	}

	public boolean canConvert(Class clazz) {
		return LineChart.Style.class.isAssignableFrom(clazz);
	}
}
