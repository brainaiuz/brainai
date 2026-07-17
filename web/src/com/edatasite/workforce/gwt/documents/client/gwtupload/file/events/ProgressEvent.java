package com.edatasite.workforce.gwt.documents.client.gwtupload.file.events;

public class ProgressEvent extends ProgressEventBase<ProgressHandler> {
	private static final Type<ProgressHandler> TYPE = new Type<>("progress", new ProgressEvent());

	protected ProgressEvent() {
	}

	public Type<ProgressHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProgressHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ProgressHandler handler) {
		handler.onProgress(this);
	}
}
