package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class InvoiceDynamicProjectLookupBox extends DynamicProjectLookupBox {
    public InvoiceDynamicProjectLookupBox(Integer objectId, SelectItem projectItem, String type, String viewType, Integer clientID) {
        super(projectItem, type, clientID, project -> InvoiceService.App.get().changeRelatedProject(objectId, viewType, project == null ? null : project.getId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(Void aVoid) {}
        }));
    }
}
