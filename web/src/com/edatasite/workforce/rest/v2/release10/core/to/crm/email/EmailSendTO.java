package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 4/24/2018.
 */
public class EmailSendTO extends DraftEmailInfoTO {
    private ArrayList<LinkTO> links;

    public ArrayList<LinkTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkTO> links) {
        this.links = links;
    }
}
