package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;
import java.util.Map;


public class UploadHandlerForm extends UploadHandlerAbstract {
    private Map<String, InputElement> inputs;

    public UploadHandlerForm(UploadProgressHandlers progressHandlers, Options options) {
        super(progressHandlers, options);
        this.inputs = new HashMap<>();
    }

    @Override
    protected String add(Object o) {
        InputElement fileInput = (InputElement) o;
        fileInput.setAttribute("name", "qqfile");
        String id = UUID.uuid();
        inputs.put(id, fileInput);
        fileInput.removeFromParent();
        return id;
    }

    @Override
    protected String getName(String id) {
        final String result = inputs.get(id).getValue().replace("\\\\", "/");
        return result.substring(result.lastIndexOf('/') + 1);
    }

    @Override
    protected int getSize(String id) {
        return -1;
    }

    @Override
    protected String uploadFile(String id) {
        InputElement inputElement = inputs.get(id);

        // probably file has just been dequeued
        if (inputElement == null) {
            return null;
        }

        final String fileName = getName(id);
        final Frame iframe = createFrame(id);
        final FormElement form = createForm(iframe.getElement().getAttribute("name"));
        form.appendChild(inputElement);
        attachLoadEvent(iframe, id, fileName);
		String action = form.getAction();
		action = action + "&uuid="+id;
		form.setAction(action);
        form.submit();
        log("submit");
        form.removeFromParent();
        return id;
    }

    private void attachLoadEvent(final Frame iframe, final String id, final String fileName) {
        iframe.addLoadHandler(event -> {
            final Element e = Element.as(event.getNativeEvent().getEventTarget());
            if (e == iframe.getElement()) {
                if (!iframe.getElement().hasParentElement()) {
                    return;
                }
                if (iframe.getElement().getOwnerDocument() != null && iframe.getElement().getOwnerDocument().getBody() != null
                        && iframe.getElement().getOwnerDocument().getBody().getInnerHTML().equals("false")) {
                    return;
                }
                onComplete(iframe, id, fileName);
            }
        });
    }

    private void onComplete(final Frame iframe, String id, String fileName) {
        log("Iframe loaded; file with id `" + id + "` has been successfully uploaded");
        final JSONValue jsonValue = getIframeContentJSON(iframe);
        if (jsonValue == null) {
            progressHandlers.onFailure(id, fileName);
//            showError(messages.errorUploadingFile(fileName));
        } else {
            progressHandlers.onComplete(id, fileName, jsonValue);
        }
        inputs.remove(id);
        Timer t = new Timer() {
            @Override
            public void run() {
                iframe.removeFromParent();
            }
        };
        t.schedule(1);
    }

    private JSONValue getIframeContentJSON(Frame iframe) {
        FrameElement frameElement = iframe.getElement().cast();
        Document contentDocument = frameElement.getContentDocument();
        if (contentDocument == null) {
            return null;
        }
        return JSONParser.parseStrict(contentDocument.getBody().getInnerText());
    }

    @Override
    protected void cancelUploadFile(String id) {

    }

    private Frame createFrame(String id) {
        Frame frame = new Frame("javascript:false;");
        frame.getElement().setAttribute("name", id);
        frame.getElement().setAttribute("id", id);
        frame.getElement().getStyle().setDisplay(Style.Display.NONE);
        RootPanel.get().add(frame);
        return frame;
    }

    private FormElement createForm(String iframeName) {
        final FormElement form = Document.get().createFormElement();
        form.setMethod("post");
        final String enctype = "multipart/form-data";
        form.setEnctype(enctype);
        form.setAttribute("encoding", enctype);
        form.setAction(options.getAction());
        form.setTarget(iframeName);
        form.getStyle().setDisplay(Style.Display.NONE);
        Document.get().getBody().appendChild(form);
        return form;
    }
}
