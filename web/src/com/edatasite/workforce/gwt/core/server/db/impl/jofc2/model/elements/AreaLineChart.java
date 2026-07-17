package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.elements;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 14:21:01
 * To change this template use File | Settings | File Templates.
 */
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.metadata.Alias;

public class AreaLineChart extends LineChart {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static transient final Float DEFAULT_ALPHA = 0.35f;

    @Alias("fill-alpha")
    private Float fillAlpha;
    @Alias("fill")
    private String fill;
    private boolean loop;


    public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public AreaLineChart() {
        super(Style.Type.AREA_LINE.getType());
        setFillAlpha(DEFAULT_ALPHA);
    }

    public AreaLineChart(String type) {
        super(type);
        setFillAlpha(DEFAULT_ALPHA);
    }

    public Float getFillAlpha() {
        return fillAlpha;
    }

    public AreaLineChart setFillAlpha(Float fillAlpha) {
        this.fillAlpha = fillAlpha;
        return this;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}