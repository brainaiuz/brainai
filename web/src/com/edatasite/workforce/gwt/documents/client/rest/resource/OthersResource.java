package com.edatasite.workforce.gwt.documents.client.rest.resource;

import java.util.ArrayList;

public class OthersResource extends RestResource {

    public OthersResource() {
    }

    ArrayList<String> others = new ArrayList<>();
    ArrayList<OtherUserResource> otherUsers = new ArrayList<>();

    /**
     * Retrieve the others.
     *
     * @return the others
     */
    public ArrayList<String> getOthers() {
        return others;
    }

    /**
     * Modify the others.
     *
     * @param newOthers the others to set
     */
    public void setOthers(ArrayList<String> newOthers) {
        others = newOthers;
    }

    public ArrayList<OtherUserResource> getOtherUsers() {
        return otherUsers;
    }


    public void setOtherUsers(ArrayList<OtherUserResource> otherUsers) {
        this.otherUsers = otherUsers;
    }

    public String getUsernameOfUri(Integer userId) {
        for (OtherUserResource o : getOtherUsers()) {
            Integer toCheck = o.getObjectId();
            if (toCheck.equals(userId)) {
                return o.getUsername();
            }
        }
        return null;
    }

    @Override
    public String getLastModifiedSince() {
        return null;
    }
}
