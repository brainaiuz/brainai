package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BoolItem implements IsSerializable {

    private Integer id;
    private boolean checked;
    private Integer[] collaborators;
	private Double weight;

    public BoolItem() {

    }

    public BoolItem(Integer id, boolean checked) {
        this.id = id;
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer[] getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Integer[] collaborators) {
        this.collaborators = collaborators;
    }

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
