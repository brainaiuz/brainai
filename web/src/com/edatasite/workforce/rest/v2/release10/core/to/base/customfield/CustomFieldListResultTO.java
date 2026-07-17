package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class CustomFieldListResultTO extends ResponseData {

    private ArrayList<CustomFieldListTO> custom_fields;

    public CustomFieldListResultTO() {
    }

    public CustomFieldListResultTO(ArrayList<CustomFieldListTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public ArrayList<CustomFieldListTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldListTO> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
