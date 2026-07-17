package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/1/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgendaTopicItem implements IsSerializable {

	private Integer objectID;
	private Integer number;
	private String name;
	private ArrayList<AgendaTopicDiscussionItem> discussionItems;

	public AgendaTopicItem() {

	}

	public AgendaTopicItem(Integer objectID, String name) {
		this.objectID = objectID;
		this.name = name;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<AgendaTopicDiscussionItem> getDiscussionItems() {
		return discussionItems;
	}

	public void setDiscussionItems(ArrayList<AgendaTopicDiscussionItem> discussionItems) {
		this.discussionItems = discussionItems;
	}
}
