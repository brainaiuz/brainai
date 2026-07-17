package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 6, 2010
 * Time: 2:04:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveEvent<T> extends GwtEvent<SaveHandler<T>> {

    private static Type<SaveHandler<?>> TYPE;

    private final T target;

    private boolean cancelled;

    public SaveEvent(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

    public static <T> boolean fire(HasSaveHandlers<T> source, T target) {
        if (TYPE != null) {
            SaveEvent<T> event = new SaveEvent<>(target);
            source.fireEvent(event);
            return !event.isCancelled();
        }
        return true;
    }

    public static Type<SaveHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    public final Type<SaveHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SaveHandler<T> handler) {
        handler.onSave(this);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
