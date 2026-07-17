package com.edatasite.workforce.gwt.documents.client.gwtupload.file;


public class File extends Blob {
	protected File() {
	}
	public final native String getName() /*-{
      // fix missing properties in Safari
      return this.fileName != null ? this.fileName : this.name;
	}-*/; 
	public final native String getType() /*-{
      return this.type;
	}-*/; 
	public final native String getUrn() /*-{
      return this.urn;
	}-*/; 
}
