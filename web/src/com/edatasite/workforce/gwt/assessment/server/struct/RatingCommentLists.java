package com.edatasite.workforce.gwt.assessment.server.struct;

import com.edatasite.workforce.gwt.assessment.client.rpc.RatingComment;

import java.util.ArrayList;
import java.util.List;

public class RatingCommentLists {

    public List<RatingComment> clients = new ArrayList<>();
    public List<RatingComment> managers = new ArrayList<>();
    public List<RatingComment> peers = new ArrayList<>();

    public void addClient(RatingComment rc) {
        clients.add(rc);
    }

    public void addManager(RatingComment rc) {
        managers.add(rc);
    }

    public void addPeer(RatingComment rc) {
        peers.add(rc);
    }
}