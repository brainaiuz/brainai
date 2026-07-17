package com.edatasite.workforce.gwt.documents.client.gwtupload.xhr;

import com.edatasite.workforce.gwt.documents.client.gwtupload.file.FileList;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.impl.FileListImpl;

import com.google.gwt.dom.client.DataTransfer;

public class DataTransferAdvanced extends DataTransfer {
  protected DataTransferAdvanced() {
  }

  public final FileList getFiles() {
    return new FileList(getFileList());
  }

  private native FileListImpl getFileList() /*-{
    return this.files;
  }-*/;
  
  public final native String getEffectAllowed() /*-{
    return this.effectAllowed;
  }-*/;

  public final native void setDropEffect(String effect) /*-{
    this.dropEffect = effect;
  }-*/;
}
