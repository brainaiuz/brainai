package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;

import java.util.ArrayList;
import java.util.HashMap;

public interface WhatsAppMessageManager extends Manager<EdsWhatsAppMessage> {

    ArrayList<EdsWhatsAppMessage> getAllMessagesList();

    HashMap<String,ArrayList<Integer>> getMessageOwnersDataMap();
}
