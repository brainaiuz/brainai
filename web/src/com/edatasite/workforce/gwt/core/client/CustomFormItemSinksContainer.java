package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.IntroductionPageSummary;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class CustomFormItemSinksContainer extends SinksContainer {

    public CustomFormItemSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {

        Integer objectID = null;
        Integer fID = null;
        String formID = null;
        String name = null;
        String lookUpType = null;
        Integer lookUpTypeId = null;
        String fromType = null;
        Integer convertFormId = null;
        boolean isCopy = false;
        boolean isPage = false;
        String type = "";
        if (params.length > 4) {
            if (params[3] != null && "CONVERT".equals(params[3])) {
                fID = Integer.parseInt(params[1]);
                formID = params[2];
                fromType = params[4];
                convertFormId = params[5] != null ? Integer.parseInt(params[5]) : null;
            } else if (params[5] != null && "copyItemForm".equals(params[5])) {
                objectID = Integer.parseInt(params[1]);
                fID = Integer.parseInt(params[2]);
                formID = params[3];
                name = params[4];
                if (params[5] != null && "copyItemForm".equals(params[5])) {
                    isCopy = true;
                }
                lookUpType = params[6] != null ? params[6] : null;
                lookUpTypeId = params[7] != null ? Integer.parseInt(params[7]) : null;
            }else if(params[3] != null && "intro".equals(params[3])){
//                objectID = Integer.parseInt(params[1]);
                fID = Integer.parseInt(params[1]);
                formID = params[2];
                type = params[3];
                name = params[4];
                if (params.length > 5) {
                    lookUpType = params[5] != null ? params[5] : null;
                    lookUpTypeId = params[6] != null ? Integer.parseInt(params[6]) : null;
                }
            } else {
                if (params[1] != null && params[1].length() > 0) {
                    objectID = Integer.parseInt(params[1]);
                }
                fID = Integer.parseInt(params[2]);
                formID = params[3];
                name = params[4];
                lookUpType = params[5] != null ? params[5] : null;
                lookUpTypeId = params[6] != null && params[6].length() > 0 ? Integer.parseInt(params[6]) : null;
                if (params[7] != null && "PAGE".equals(params[7])) {
                    isPage = true;
                }
            }
        } else if (params.length > 3) {
            fID = Integer.parseInt(params[1]);
            formID = params[2];
            name = params[3];
        }

        if ("intro".equals(type)) {
            addView(new IntroductionPageSummary(fID, formID, name, lookUpType, lookUpTypeId));
        } else {
            if (fromType != null && convertFormId != null) {
                addView(new AddCustomFormItemView(fID, formID, fromType, convertFormId));
            } else {
                addView(new AddCustomFormItemView(objectID, fID, formID, name, isCopy, lookUpType, lookUpTypeId, isPage));
            }
        }
    }
}
