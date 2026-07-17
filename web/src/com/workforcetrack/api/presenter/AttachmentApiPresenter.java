package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 05/09/12
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
public class AttachmentApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMap(FileResource[] fileResources) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        if (fileResources.length > 0) {
         for(FileResource resource: fileResources) {
             arrayList.add(convertToMap(resource));
         }
        }
        map.put(TOTAL_COUNT, arrayList.size());
        map.put(ITEMS, arrayList);
        return map;
    }

    public Map<String, Object> convertToMap(FileResource fileResource) {
        Map<String, Object> map = new LinkedHashMap<>();
        MFileResource fr = new MFileResource(fileResource);
        if (fileResource != null) {
            map.put(OBJECT_ID, fr.getObjectID());
            map.put(NAME, fr.getName());
            map.put(DESCRIPTION, fr.getDescription());
            map.put(CONTENT_TYPE, fr.getContentType());
            map.put(FILE_SIZE, fr.getFileSize());
            map.put(URL, fr.getUrl());
        }
        return map;
    }
}
