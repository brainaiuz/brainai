package com.edatasite.workforce.gwt.profile.client.services;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReferenceClient {

    void createReference(CreateReferenceReq reference, AsyncCallback<ResultTO<ReferenceItem>> callback);

    void getReferenceById(Integer id, AsyncCallback<ResultTO<ReferenceItem>> callback);

    void getOrCreateOrgBoardReference(String code, AsyncCallback<ResultTO<ReferenceItem>> callback);

    void updateReferenceByCode(CreateReferenceReq reference, AsyncCallback<ResultTO<ReferenceItem>> callback);
}
