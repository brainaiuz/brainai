package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.auth.EmailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class ContactsTO extends ResponseData {
    private ArrayList<PhoneTO> phones;
    private ArrayList<String> emails;
    private ArrayList<EmailTO> emailTo;

    public ContactsTO() {
    }

    public ContactsTO(ArrayList<PhoneTO> phones, ArrayList<String> emails) {
        this.phones = phones;
        this.emails = emails;
    }

    public ArrayList<PhoneTO> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<PhoneTO> phones) {
        this.phones = phones;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }
    public ArrayList<EmailTO> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(ArrayList<EmailTO> emailTo) {
        this.emailTo = emailTo;
    }
}
