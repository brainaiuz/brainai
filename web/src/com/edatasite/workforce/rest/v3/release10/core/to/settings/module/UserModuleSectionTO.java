package com.edatasite.workforce.rest.v3.release10.core.to.settings.module;

import com.edatasite.workforce.core.enums.ContextCode;
import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;

import java.util.List;

public class UserModuleSectionTO {
    private ContextCode module;
    private List<PseudoMenuItem> pseudoMenuItems;

    public UserModuleSectionTO() {
    }

    public UserModuleSectionTO(ContextCode module, List<PseudoMenuItem> pseudoMenuItems) {
        this.module = module;
        this.pseudoMenuItems = pseudoMenuItems;
    }

    public ContextCode getModule() {
        return module;
    }

    public List<PseudoMenuItem> getPseudoMenuItems() {
        return pseudoMenuItems;
    }
}
