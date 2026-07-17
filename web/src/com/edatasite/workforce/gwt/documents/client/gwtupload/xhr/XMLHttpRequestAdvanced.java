package com.edatasite.workforce.gwt.documents.client.gwtupload.xhr;

import com.edatasite.workforce.gwt.documents.client.gwtupload.file.File;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class XMLHttpRequestAdvanced extends XMLHttpRequest {
  protected XMLHttpRequestAdvanced() {
  }

  public static XMLHttpRequestAdvanced create() {
    return (XMLHttpRequestAdvanced)XMLHttpRequest.create();
  }

  public final native void send(File file) /*-{
    this.send(file);
  }-*/;

  public final native void setOnUploadProgress(OnUploadProgressHandler handler) /*-{
    var _this = this;
    this.upload.onprogress = $entry(function(e) {
      handler.@com.edatasite.workforce.gwt.documents.client.gwtupload.xhr.OnUploadProgressHandler::onUploadProgress(Lcom/edatasite/workforce/gwt/documents/client/gwtupload/xhr/OnUploadProgressEvent;)(e);
    });
  }-*/;
}
