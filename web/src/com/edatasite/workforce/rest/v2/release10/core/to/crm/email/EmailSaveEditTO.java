package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LinksTO;

import java.util.ArrayList;

/**
 * Created by Farrukh Abdurakhmonov on 4/24/2018.
 */
public class EmailSaveEditTO extends DraftEmailInfoTO {
    private ArrayList<LinksTO> links;

    public ArrayList<LinksTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinksTO> links) {
        this.links = links;
    }
}
