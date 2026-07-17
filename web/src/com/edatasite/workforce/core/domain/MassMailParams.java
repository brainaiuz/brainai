package com.edatasite.workforce.core.domain;

/**
 * Created by Azazello on 3/29/2017.
 */
public class MassMailParams {
    private String clusterType;
    private String companyHost = "app.kpi.com";
    private Integer companyID;
    private String host = "smtp-mailer.kpi.com";
    private String port = "25";
    private String login = "no-reply@mailer.kpi.com";
    private String password = "E9xv#PX@8F$cg";
    private String bouncedEmail = "bounced@mailer.kpi.com";
    private String bouncedPassword = "gE9xv@PX#8F$cgE";
    private String abuseEmail = "abuse@kpi.com";
    private String tolerateText = "We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}";
    private String tolerateHtml = "We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href=\"mailto:{abuseemail}\">{abuseemail}</a>";
    private String unsubscribeText = "\\n\\n\\n\\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\\n\\n";
    private String unsubscribeHtml = "<br><br><br><hr/><a href=\"http://{unsubscribeurl}/unsubscribe{encrypted}\" style=\"font-size:12px\"> Click here </a>to unsubscribe<br><br>";
    private boolean ssl = true;
    private boolean smtpAuth = true;

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    public String getCompanyHost() {
        return companyHost;
    }

    public void setCompanyHost(String companyHost) {
        this.companyHost = companyHost;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBouncedEmail() {
        return bouncedEmail;
    }

    public void setBouncedEmail(String bouncedEmail) {
        this.bouncedEmail = bouncedEmail;
    }

    public String getBouncedPassword() {
        return bouncedPassword;
    }

    public void setBouncedPassword(String bouncedPassword) {
        this.bouncedPassword = bouncedPassword;
    }

    public String getAbuseEmail() {
        return abuseEmail;
    }

    public void setAbuseEmail(String abuseEmail) {
        this.abuseEmail = abuseEmail;
    }

    public String getTolerateText() {
        return tolerateText;
    }

    public void setTolerateText(String tolerateText) {
        this.tolerateText = tolerateText;
    }

    public String getTolerateHtml() {
        return tolerateHtml;
    }

    public void setTolerateHtml(String tolerateHtml) {
        this.tolerateHtml = tolerateHtml;
    }

    public String getUnsubscribeText() {
        return unsubscribeText;
    }

    public void setUnsubscribeText(String unsubscribeText) {
        this.unsubscribeText = unsubscribeText;
    }

    public String getUnsubscribeHtml() {
        return unsubscribeHtml;
    }

    public void setUnsubscribeHtml(String unsubscribeHtml) {
        this.unsubscribeHtml = unsubscribeHtml;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }
}
