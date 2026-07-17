package com.edatasite.workforce.gwt.documents.client.gwtupload.file.impl;

import com.edatasite.workforce.gwt.documents.client.gwtupload.file.File;

import com.google.gwt.core.client.JavaScriptObject;

public class FileListImpl extends JavaScriptObject {
	protected FileListImpl() {
	}
	public final native int getLength() /*-{
      return this.length;
	}-*/; 
	public final native File getItem(int index) /*-{
      return this[index];
	}-*/; 
}

