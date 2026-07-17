package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.google.gwt.user.client.Command;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

public class FileWidget extends Div {
    private String fileId;
    private Div contentWrapper;
    private Div status;
    private Div row;
    private Span progress;
    private Span name;
    private Command removeCommand;

    public FileWidget(String fileId, String fileName) {
        super("kpi-upload__upload-file kpi-upload__upload-file--in-process");
        this.fileId = fileId;
        contentWrapper = new Div("kpi-upload__content-wrapper");
        status = new Div("kpi-upload__upload-file-status");
        row = new Div("kpi-upload__upload-file-row");
        progress = new Span();
        progress.addStyleName("kpi-upload__upload-file-progress");
        progress.getElement().setInnerHTML("<svg width=\"23\" height=\"23\" viewBox=\"0 0 23 23\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                "<path d=\"M8.5 0C10.7543 0 12.9163 0.895533 14.5104 2.48959C16.1045 4.08365 17 6.24566 17 8.5C17 10.7543 16.1045 12.9163 14.5104 14.5104C12.9163 16.1045 10.7543 17 8.5 17C6.24566 17 4.08365 16.1045 2.48959 14.5104C0.895533 12.9163 0 10.7543 0 8.5C0 6.24566 0.895533 4.08365 2.48959 2.48959C4.08365 0.895533 6.24566 0 8.5 0\" transform=\"translate(3 3)\" stroke=\"#8D9BA8\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-dasharray=\"0 250.2\" ></path>\n" +
                "</svg>");
        name = new Span(fileName);
        name.addStyleName("kpi-upload__upload-file-name");
        contentWrapper.add(status);
        contentWrapper.add(row);
        row.add(progress);
        row.add(name);
        add(contentWrapper);
        Div delete = new Div("kpi-upload__upload-file-delete");
        MaterialIcon deleteIcon = new MaterialIcon();
        deleteIcon.addStyleName("ficon--close");
        delete.add(deleteIcon);
        delete.addClickHandler((event) -> {
            if (removeCommand != null) {
                removeCommand.execute();
            }
        });
        add(delete);
    }

    public void setCompleted(double percentage) {
        if (percentage >= 100) {
            removeStyleName("kpi-upload__upload-file--in-process");
            progress.getElement().setInnerHTML("<i class=\"ficon--check\"></i>");
        } else {
            this.getElement().getElementsByTagName("path").getItem(0).getStyle().setProperty("strokeDasharray", (percentage / 2 + 2) + " 250.2");
        }
    }

    public Command getRemoveCommand() {
        return removeCommand;
    }

    public void setRemoveCommand(Command removeCommand) {
        this.removeCommand = removeCommand;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
