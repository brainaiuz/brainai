package com.edatasite.shared.mail;

import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zohid
 * Date: 23.05.2007
 * Time: 17:30:26
 * To change this template use File | Settings | File Templates.
 */
public class EdsMailBody implements Cloneable {

    private static Logger log = LoggerFactory.getLogger(EdsMailBody.class);

    private String _smtpServerHost;
    private String _smtpServerPort;
    private String _login;
    private String _password;
    private String _smtpProtocol = "smtp";
    private String _fromName;

    private String _subject;
    private String _displaySubject;
    private String _message;
    private String _invitationContent;
    private List<InternetAddress> _replyTo;
    private List<InternetAddress> _from;
    private List<InternetAddress> _TORecipients;
    private List<InternetAddress> _CCRecipients;
    private List<InternetAddress> _BCCRecipients;
    private List<Upload> uploads = new ArrayList<>();
    private boolean smtpAuth;

    protected EdsMailBody clone() {
        try {
            return (EdsMailBody) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String getSmtpServerHost() {
        return _smtpServerHost;
    }

    public void setSmtpServerHost(String serverHost) {
        this._smtpServerHost = serverHost;
    }

    public String getSmtpServerPort() {
        return _smtpServerPort;
    }

    public void setSmtpServerPort(String smtpServerPort) {
        this._smtpServerPort = smtpServerPort;
    }

    public String getLogin() {
        return _login;
    }

    public void setLogin(String login) {
        _login = login;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getPassword() {
        return _password;
    }

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }

    public String get_smtpProtocol() {
        return _smtpProtocol;
    }

    public void set_smtpProtocol(String _smtpProtocol) {
        this._smtpProtocol = _smtpProtocol;
    }

    public String getFromName() {
        return _fromName;
    }

    public void setFromName(String fromName) {
        this._fromName = fromName;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String get_invitationContent() {
        return _invitationContent;
    }

    public void set_invitationContent(String _invitationContent) {
        this._invitationContent = _invitationContent;
    }

    public List<InternetAddress> getFrom() {
        return _from;
    }

    public void addReplyTo(String replyTo) throws Exception {
        _addReplyTo(new InternetAddress(replyTo));
    }

    public void addFrom(String from, String personal) throws Exception {
        _addFrom(new InternetAddress(from), personal);
    }

    public void addTORecipient(String recipient) throws Exception {
        _addTORecipient(new InternetAddress(recipient));
    }

    public List<InternetAddress> getCCRecipients() {
        return _CCRecipients;
    }

    public void addCCRecipient(String recipient) throws Exception {
        _addCCRecipient(new InternetAddress(recipient));
    }

    public List<InternetAddress> getBCCRecipients() {
        return _BCCRecipients;
    }

    public void addBCCRecipient(String recipient) throws Exception {
        _addBCCRecipient(new InternetAddress(recipient));
    }

    private void _addFrom(InternetAddress from, String personal) throws Exception {
        if (from == null) {
            throw new NullPointerException("Entered empty 'from' address");
        }
        if (_from == null) {
            _from = new ArrayList<>();
        }
        from.setPersonal(personal);
        if (getSubject().equals("Account Confirmation")) {
            if (getDisplaySubject().equals("")) {
                setDisplaySubject(getSubject());
            }
        } else {
            setDisplaySubject(getSubject());
        }
        _from.add(from);
    }

    private void _addReplyTo(InternetAddress replyTo) throws Exception {
        if (replyTo == null) {
            throw new NullPointerException("Entered empty 'replyTo' address");
        }
        if (_replyTo == null) {
            _replyTo = new ArrayList<>();
        }
        _replyTo.add(replyTo);
    }

    private void _addTORecipient(InternetAddress recipient) throws Exception {
        if (recipient == null) {
            throw new NullPointerException("Enter empty TORecipient");
        }
        if (_TORecipients == null) {
            _TORecipients = new ArrayList<>();
        }
        _TORecipients.add(recipient);
    }

    private void _addCCRecipient(InternetAddress recipient) throws Exception {
        if (recipient == null) {
            throw new NullPointerException("Enter empty CCRecipient");
        }
        if (_CCRecipients == null) {
            _CCRecipients = new ArrayList<>();
        }
        _CCRecipients.add(recipient);
    }

    private void _addBCCRecipient(InternetAddress recipient) throws Exception {
        if (recipient == null) {
            throw new NullPointerException("Entered empty BCCRecipient");
        }
        if (_BCCRecipients == null) {
            _BCCRecipients = new ArrayList<>();
        }
        _BCCRecipients.add(recipient);
    }

    protected InternetAddress[] getReplyToArray() {
        if (_replyTo == null) {
            return null;
        }
        InternetAddress[] replyToArr = new InternetAddress[_replyTo.size()];
        System.arraycopy(_replyTo.toArray(), 0, replyToArr, 0, replyToArr.length);
        return replyToArr;
    }

    protected InternetAddress[] getFromArray() {
        if (_from == null) {
            return null;
        }
        InternetAddress[] iaArr = new InternetAddress[_from.size()];
        System.arraycopy(_from.toArray(), 0, iaArr, 0, iaArr.length);
        return iaArr;
    }

    protected InternetAddress[] getTORecipientsArray() {
        if (_TORecipients == null) {
            return null;
        }
        InternetAddress[] iaArr = new InternetAddress[_TORecipients.size()];
        System.arraycopy(_TORecipients.toArray(), 0, iaArr, 0, iaArr.length);
        return iaArr;
    }

    protected InternetAddress[] getCCRecipientsArray() {
        if (_CCRecipients == null) {
            return null;
        }
        InternetAddress[] iaArr = new InternetAddress[_CCRecipients.size()];
        System.arraycopy(_CCRecipients.toArray(), 0, iaArr, 0, iaArr.length);
        return iaArr;
    }

    protected InternetAddress[] getBCCRecipientsArray() {
        if (_BCCRecipients == null) {
            return null;
        }
        InternetAddress[] iaArr = new InternetAddress[_BCCRecipients.size()];
        System.arraycopy(_BCCRecipients.toArray(), 0, iaArr, 0, iaArr.length);
        return iaArr;
    }

    public String getDisplaySubject() {
        return _displaySubject;
    }

    public void setDisplaySubject(String subject) {
        _displaySubject = subject;
    }

    public List<Upload> getUploads() {
        if (uploads == null) {
            uploads = new ArrayList<>();
        }
        return uploads;
    }

    public void setUploads(List<Upload> uploads) {
        this.uploads = uploads;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }
}
