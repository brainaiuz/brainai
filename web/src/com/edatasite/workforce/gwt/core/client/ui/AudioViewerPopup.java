package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.media.client.Audio;

/**
 * Created with IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 21.04.2020
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */

public class AudioViewerPopup extends KpiModal {

    private Audio audioPlayer;

    public AudioViewerPopup(String name, String imageUrl) {
        super();
        setCloseButton(true);
        setTitle(name);
        if(name.endsWith("mpeg")) {

        }
        onInitialize(imageUrl);
    }

    private void onInitialize(final String url) {
        audioPlayer = Audio.createIfSupported();
        audioPlayer.setControls(true);
        audioPlayer.setSrc(url);
        audioPlayer.setWidth("100%");
        add(audioPlayer);
    }

    public void playAudioTag() {
        audioPlayer.play();
    }

}
