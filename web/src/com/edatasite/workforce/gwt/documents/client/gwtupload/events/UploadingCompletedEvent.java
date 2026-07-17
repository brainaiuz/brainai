package com.edatasite.workforce.gwt.documents.client.gwtupload.events;

import com.google.gwt.event.shared.GwtEvent;

public class UploadingCompletedEvent extends GwtEvent<UploadingCompletedEventHandler> {
  public static Type<UploadingCompletedEventHandler> TYPE = new Type<>();

  public UploadingCompletedEvent() {
  }

  @Override
  public Type<UploadingCompletedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(UploadingCompletedEventHandler handler) {
    handler.onUploadingCompleted(this);
  }
}
