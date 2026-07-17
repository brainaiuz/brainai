package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/2/12
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "featuresMore")
public class EdsFeaturesMore extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID; //message ID

	@Column(name = "message")
	@Type(type = "text")
	private String message; //message content

	@Column(name = "message_code", length = 1000)
	private String message_code; //message code

	@Column(name = "message_subject")
	@Type(type = "text")
	private String message_subject; //message subject

	@Column(name = "locale")
	private Locale locale; //message locale - en, ru, e.t.c

	@Override
	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_code() {
		return message_code;
	}

	public void setMessage_code(String message_code) {
		this.message_code = message_code;
	}

	public String getMessage_subject() {
		return message_subject;
	}

	public void setMessage_subject(String message_subject) {
		this.message_subject = message_subject;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}