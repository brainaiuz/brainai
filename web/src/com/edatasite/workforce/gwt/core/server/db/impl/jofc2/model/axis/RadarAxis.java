package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.axis;


import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.metadata.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16-Feb-2010
 * Time: 14:36:14
 * To change this template use File | Settings | File Templates.
 */
public class RadarAxis extends Axis{

	/**
	 * OFC radar axis label
	 */
	public static class Labels{

        private String colour;
		private List<Object> labels;

		/**
		 * Creates a new labels.
		 *
		 * @param labels
		 *            the labels
		 */
		public Labels(List<String> labels) {
			checkLabelsNotNull();
			this.labels.addAll(labels);
		}

		/**
		 * Creates a new labels.
		 *
		 * @param labels
		 *            the labels
		 */
		public Labels(String... labels) {
			addLabels(labels);
		}

		/**
		 * Adds the labels.
		 *
		 * @param labels
		 *            the labels
		 */
		public void addLabels(Label... labels) {
			checkLabelsNotNull();
			this.labels.addAll(Arrays.asList(labels));
		}

		/**
		 * Adds the labels.
		 *
		 * @param labels
		 *            the labels
		 */
		public void addLabels(List<Label> labels) {
			checkLabelsNotNull();
			this.labels.addAll(labels);
		}

		/**
		 * Adds the labels.
		 *
		 * @param labels
		 *            the labels
		 */
		public void addLabels(String... labels) {
			checkLabelsNotNull();
			this.labels.addAll(Arrays.asList(labels));
		}

		/**
		 * Gets the colour.
		 *
		 * @return the colour
		 */
		public String getColour() {
			return colour;
		}

		/**
		 * Gets the labels.
		 *
		 * @return the labels
		 */
		public List<Object> getLabels() {
			return labels;
		}

		/**
		 * Sets the colour in HTML hex format (#ffffff)
		 *
		 * @param colour
		 *            the new colour
		 */
		public void setColour(String colour) {
			this.colour = colour;
		}

		/**
		 * Check labels not null.
		 */
		private synchronized void checkLabelsNotNull() {
			if (labels == null) labels = new ArrayList<>();
		}
	}

    
	private Labels labels;
    @Alias("spoke-labels")
	private Labels spokelabels;

	/**
	 * Adds the labels.
	 *
	 * @param labels
	 *            the labels
	 */
	public void addLabels(String... labels) {
		checkLabelsNotNull();
		this.labels.addLabels(labels);
	}

	/**
	 * Adds the spoke labels.
	 *
	 * @param labels
	 *            the labels
	 */
	public void addSpokeLabels(String... labels) {
		checkSpokeLabelsNotNull();
		this.spokelabels.addLabels(labels);
	}

	/**
	 * Gets the labels.
	 *
	 * @return the labels
	 */
	public Labels getLabels() {
		return labels;
	}

	/**
	 * Gets the spoke labels.
	 *
	 * @return the labels
	 */
	public Labels getSpokeLabels() {
		return spokelabels;
	}

	/**
	 * Sets the labels.
	 *
	 * @param labels
	 *            the new labels
	 */
	public void setLabels(List<String> labels) {
		this.labels = new Labels(labels);
	}

	/**
	 * Sets the labels.
	 *
	 * @param labels
	 *            the new labels
	 */
	public void setLabels(String... labels) {
		this.labels = new Labels(labels);
	}

	/**
	 * Sets the radar axis labels.
	 *
	 * @param labels
	 *            the new radar axis labels
	 */
	public void setRadarAxisLabels(Labels labels) {
		this.labels = labels;
	}

	/**
	 * Sets the radar axis spoke labels.
	 *
	 * @param labels
	 *            the new radar axis spoke labels
	 */
	public void setSpokeLabels(Labels labels) {
		this.spokelabels = labels;
	}

	/**
	 * Sets the spoke labels.
	 *
	 * @param labels
	 *            the new spoke labels
	 */
	public void setSpokeLabels(List<String> labels) {
		this.spokelabels = new Labels(labels);
	}

	/**
	 * Sets the spoke labels.
	 *
	 * @param labels
	 *            the new spoke labels
	 */
	public void setSpokeLabels(String... labels) {
		this.spokelabels = new Labels(labels);
	}

	/**
	 * Check labels not null.
	 */
	private void checkLabelsNotNull() {
		if (labels == null) labels = new Labels();
	}

	/**
	 * Check labels not null.
	 */
	private void checkSpokeLabelsNotNull() {
		if (spokelabels == null) spokelabels = new Labels();
	}
}
