package com.edatasite.shared.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * Created by IntelliJ IDEA.
 * User: zohid
 * Date: 23.05.2007
 * Time: 16:07:42
 * To change this template use File | Settings | File Templates.
 */
public class EdsSMTPAuthenticator extends Authenticator {

    private String login;
    private String password;

    public EdsSMTPAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }


    protected PasswordAuthentication getPasswordAuthentication() {
        if (password == null || "".equals(password)) {
            return null;
        } else {
            return new PasswordAuthentication(login, password);
        }
    }
}
