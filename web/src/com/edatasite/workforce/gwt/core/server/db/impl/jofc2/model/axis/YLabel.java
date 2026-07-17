package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.axis;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 14:42:58
 * To change this template use File | Settings | File Templates.
 */
public class YLabel extends Label {

	private static final long serialVersionUID = 8573779527357782439L;
	private Integer y;

	public YLabel() {}

	public YLabel(String text, Integer y) {
		super(text);
		this.y = y;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
}
