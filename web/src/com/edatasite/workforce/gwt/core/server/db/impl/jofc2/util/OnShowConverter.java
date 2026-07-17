package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.util;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 14:49:34
 * To change this template use File | Settings | File Templates.
 */
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.elements.AnimatedElement;

public class OnShowConverter extends ConverterBase<AnimatedElement.OnShow> {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class c) {
		return AnimatedElement.OnShow.class.isAssignableFrom(c);
	}

	@Override
	public void convert(AnimatedElement.OnShow onShow, PathTrackingWriter writer, MarshallingContext mc) {
		writeNode(writer, "type", onShow.getType(), true);
	}
}
