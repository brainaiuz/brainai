package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable;

import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SnapshotView extends KpiModal {
    interface SnapshotViewUiBinder extends UiBinder<Widget, SnapshotView> {
    }

    private static final SnapshotViewUiBinder uiBinder = GWT.create(SnapshotViewUiBinder.class);
    private com.google.gwt.dom.client.SpanElement actual;
    private com.google.gwt.dom.client.ButtonElement cloceBtn;

    @UiField
    HTMLPanel htmlPanel;
    //    @UiField
//    HeadingElement employeeName;
//    @UiField
//    DivElement position;
    @UiField
    SpanElement inLabel;
    @UiField
    SpanElement outLabel;
    @UiField
    SpanElement inActualLabel;
    @UiField
    SpanElement outActualLabel;
    @UiField
    LabelElement outLocationLabel;
    @UiField
    LabelElement inLocationLabel;
    @UiField
    LabelElement inFaceIdLabel;
    @UiField
    LabelElement outFaceIdLabel;
    @UiField
    DivElement inTimeLabel;
    @UiField
    DivElement outTimeLabel;
    @UiField
    ImageElement outImg;
    @UiField
    ImageElement inImg;
    @UiField
    DivElement inImgDiv;
    @UiField
    DivElement outImgDiv;
    @UiField
    IFrameElement inMapFrame;
    @UiField
    IFrameElement outMapFrame;
    @UiField
    AnchorElement inMapLink;
    @UiField
    AnchorElement outMapLink;
    @UiField
    ImageElement inNoLocationImg;
    @UiField
    ImageElement outNoLocationImg;

    private static final DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("HH:mm");
    private InOutPairDto inOutPairDto;

    public SnapshotView(InOutPairDto inOutPairDto) {
        this.inOutPairDto = inOutPairDto;
        render();
    }

    private void render() {
        this.getModalHeader().removeFromParent();
        add(uiBinder.createAndBindUi(this));

        // Инициализируем элементы вручную, раз их нет в XML
        this.actual = com.google.gwt.dom.client.Document.get().createSpanElement();
        this.cloceBtn = com.google.gwt.dom.client.Document.get().createButtonElement();
        this.cloceBtn.setClassName("btn btn--primary");

        inLabel.setInnerHTML(wfmStrings.in());
        outLabel.setInnerHTML(wfmStrings.out());
        inActualLabel.setInnerHTML(wfmStrings.actual());
        outActualLabel.setInnerHTML(wfmStrings.actual());
        inFaceIdLabel.setInnerHTML(inOutPairDto.getInProject() != null ? inOutPairDto.getInProject().getName() : inOutPairDto.getInDevice());
        outFaceIdLabel.setInnerHTML(inOutPairDto.getOutProject() != null ? inOutPairDto.getOutProject().getName() : inOutPairDto.getOutDevice());
        inLocationLabel.setInnerHTML(formatCoords(inOutPairDto.getInLatitude(), inOutPairDto.getInLongitude()));
        outLocationLabel.setInnerHTML(formatCoords(inOutPairDto.getOutLatitude(), inOutPairDto.getOutLongitude()));

        inTimeLabel.setInnerHTML(wfmStrings.time() + " : " + (inOutPairDto.getInTime() != null ? TIME_FORMAT.format(inOutPairDto.getInTime().getNonConvertedDate()) : wfmStrings.notAvailable()));
        outTimeLabel.setInnerHTML(wfmStrings.time() + " : " + (inOutPairDto.getOutTime() != null ? TIME_FORMAT.format(inOutPairDto.getOutTime().getNonConvertedDate()) : wfmStrings.notAvailable()));

        actual.setInnerHTML(inOutPairDto.getDurationMinutes() != null ? wfmStrings.actual() + ": " + formatMinutesToHHmm(inOutPairDto.getDurationMinutes().intValue() / 60) : wfmStrings.actual() + " : N/A");

        if (inOutPairDto.getInSnapshotLink() != null) {
            inImg.setSrc(inOutPairDto.getInSnapshotLink());
        } else {
            inImgDiv.removeClassName("wg_photo-upload--uploaded");
        }
        setMapLocation(inMapFrame, inNoLocationImg, inMapLink, inOutPairDto.getInLatitude(), inOutPairDto.getInLongitude());

        if (inOutPairDto.getOutSnapshotLink() != null) {
            outImg.setSrc(inOutPairDto.getOutSnapshotLink());
        } else {
            outImgDiv.removeClassName("wg_photo-upload--uploaded");
        }
        setMapLocation(outMapFrame, outNoLocationImg, outMapLink, inOutPairDto.getOutLatitude(), inOutPairDto.getOutLongitude());

        // Перенос футера в Java
        this.getFooter().clear();
        com.google.gwt.dom.client.Element footerElem = this.getFooter().getElement();
        footerElem.addClassName("modal-footer");

        // Используем createElement для тега strong
        com.google.gwt.dom.client.Element strongInfo = com.google.gwt.dom.client.Document.get().createElement("strong");
        strongInfo.addClassName("trmnlAtt-info");
        strongInfo.appendChild(actual);

        footerElem.appendChild(strongInfo);
        footerElem.appendChild(cloceBtn);

        cloceBtn.setInnerHTML(wfmStrings.close());
        DOM.sinkEvents(cloceBtn.cast(), Event.ONCLICK);
        DOM.setEventListener(cloceBtn.cast(), event -> close());

        this.addStyleName("trmnlAtt-modal");
    }

    private String formatCoords(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return "";
        }
        String coords = latitude + "," + longitude;
        return "<a href=\"https://maps.google.com/maps?q=" + coords + "\" target=\"_blank\">" + coords + "</a>";
    }

    private void setMapLocation(IFrameElement mapFrame, ImageElement noLocationImg, AnchorElement mapLink, Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            String coords = latitude + "," + longitude;
            mapFrame.setSrc("https://maps.google.com/maps?q=" + coords + "&z=16&output=embed");
            mapFrame.getStyle().setDisplay(Display.BLOCK);
            noLocationImg.getStyle().setDisplay(Display.NONE);
            mapLink.setHref("https://maps.google.com/maps?q=" + coords);
        } else {
            mapFrame.getStyle().setDisplay(Display.NONE);
            noLocationImg.getStyle().setDisplay(Display.BLOCK);
            mapLink.removeAttribute("href");
        }
    }

    private String formatMinutesToHHmm(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        String h = (hours < 10 ? "0" : "") + hours;
        String m = (mins < 10 ? "0" : "") + mins;
        return h + ":" + m;
    }

}

