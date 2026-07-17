/*
 * Shredzone Commons
 *
 * Copyright (C) 2012 Richard "Shred" Körber
 *   http://commons.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.edatasite.workforce.gwt.core.server.controllers.signup.captcha.impl;

import org.shredzone.commons.captcha.CaptchaGenerator;
import org.shredzone.commons.captcha.CaptchaService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Default implementation of {@link CaptchaService}.
 *
 * @author Richard "Shred" Körber
 */
@Component("defaultCaptchaService")
public class DefaultCaptchaService implements CaptchaService {

    private static final String CHARSET = "ABCDEFGHJLMNOPQRSTUWZ";
    private static final int NUMBER_OF_CHARS = 5;
    private static final String CAPTCHA_NAME = "captcha.position";
    private static final String LASTCLICK_NAME = "captcha.lastclick";
    private static final String CAPTCHA_RESULT = "captcha.result";
    private final Random rnd = new Random();

    private char captchaLetter = 'X';

    @Resource
    private CaptchaGenerator captchaGenerator;

    public BufferedImage createCaptch(HttpSession session) {
        captchaLetter = CHARSET.charAt(rnd.nextInt(100)%20);
        int captchaPos = computeCaptchaPosition(session);
        return captchaGenerator.createCaptcha(computeChars(captchaPos));
    }

    @Override
    public BufferedImage createCaptcha(HttpSession session) {

        Color backgroundColor = new Color(240, 244, 247);

        Color textColor = new Color(131, 153, 184);

        Color circleColor = new Color(195, 208, 247);
        Font textFont = new Font("Arial", Font.PLAIN, 24);
        int charsToPrint = 5;
        int width = 150;
        int height = 30;
        int circlesToDraw = 5;
        float horizMargin = 20.0f;
        float imageQuality = 0.95f; // max is 1.0 (this is for jpeg)
        double rotationRange = 0.7; // this is radians
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Draw an oval
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);

        // lets make some noisey circles
        g.setColor(circleColor);
        for (int i = 0; i < circlesToDraw; i++) {
            int circleRadius = (int) (Math.random() * height / 2.0);
            int circleX = (int) (Math.random() * width - circleRadius);
            int circleY = (int) (Math.random() * height - circleRadius);
            g.drawOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
        }

        g.setColor(textColor);
        g.setFont(textFont);

        FontMetrics fontMetrics = g.getFontMetrics();
        int maxAdvance = fontMetrics.getMaxAdvance();
        int fontHeight = fontMetrics.getHeight();

        // i removed 1 and l and i because there are confusing to users...
        // Z, z, and N also get confusing when rotated
        // 0, O, and o are also confusing...
        // lowercase G looks a lot like a 9 so i killed it
        // this should ideally be done for every language...
        // i like controlling the characters though because it helps prevent confusion
        String elegibleChars = "123456789";
        char[] chars = elegibleChars.toCharArray();

        float spaceForLetters = -horizMargin * 2 + width;
        float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
        //String operation = ((int) Math.round(Math.random() * (chars.length - 1))) % 2 == 1 ? "+" + "-";

        StringBuilder finalString = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            double randomValue = Math.random();
            int randomIndex = (int) Math.round(randomValue * (chars.length - 1));

            char characterToShow = chars[randomIndex];
            if (i == 1) {
                characterToShow = '+';
            } else if (i == 3) {
                characterToShow = '=';
            }
            finalString.append(characterToShow);

            // this is a separate canvas used for the character so that
            // we can rotate it independently
            int charWidth = fontMetrics.charWidth(characterToShow);
            int charDim = Math.max(maxAdvance, fontHeight);
            int halfCharDim = (charDim / 2);

            BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
            Graphics2D charGraphics = charImage.createGraphics();
            charGraphics.translate(halfCharDim, halfCharDim);
            double angle = (Math.random() - 0.5) * rotationRange;
            charGraphics.transform(AffineTransform.getRotateInstance(angle));
            charGraphics.translate(-halfCharDim, -halfCharDim);
            charGraphics.setColor(textColor);
            charGraphics.setFont(textFont);

            int charX = (int) (0.5 * charDim - 0.5 * charWidth);
            charGraphics.drawString("" + characterToShow, charX,
                    ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));

            float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
            int y = ((height - charDim) / 2);
            //System.out.println("x=" + x + " height=" + height + " charDim=" + charDim + " y=" + y + " advance=" + maxAdvance + " fontHeight=" + fontHeight + " ascent=" + fontMetrics.getAscent());
            g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);

            charGraphics.dispose();
        }

        // let's stick the final string in the session
        int firstDigit = Integer.parseInt(finalString.substring(0, 1));
        int secoundDigit = Integer.parseInt(finalString.substring(2, 3));
        Integer result = firstDigit + secoundDigit;
        session.setAttribute(CAPTCHA_RESULT, result);
        System.out.println("Generated :" + result);
        return bufferedImage;
    }

    public boolean isValidCaptcha(HttpSession session, Integer userResponse) {
        if (userResponse != null) {
            Integer answer = (Integer) session.getAttribute(CAPTCHA_RESULT);
            return answer.equals(userResponse);
        }
        return false;
    }

    @Override
    public boolean isValidCaptcha(HttpSession session, int x, int y) {
        Integer pos = getCaptchaPosition(session);

        if (pos == null) {
            // There was no captcha generated yet, so the answer is always false.
            return false;
        }

        int cw = captchaGenerator.getWidth();
        int ch = captchaGenerator.getHeight();

        if (x < 0 || y < 0 || x >= cw || y >= ch) {
            // The click was outside of the captcha, so the answer is always false.
            return false;
        }

        if (x == 0 && y == 0) {
            // Ignore the simplest possible coordinate. No human being would click
            // there... ;-)
            return false;
        }

        int boxWidth = cw / NUMBER_OF_CHARS;
        int answer = x / boxWidth;

        setLastclickPosition(session, answer);

        return answer == pos;
    }

    /**
     * Compute a random set of characters, with exactly one 'X' at the given position.
     *
     * @param pos
     *            position of the 'X'
     * @return captcha text
     */
    private char[] computeChars(int pos) {
        char[] chars = new char[NUMBER_OF_CHARS];

        for (int ix = 0; ix < NUMBER_OF_CHARS; ix++) {
            if (ix == pos) {
                chars[ix] = captchaLetter;
            } else {
                chars[ix] = CHARSET.charAt(rnd.nextInt(CHARSET.length()));
            }
        }

        return chars;
    }

    /**
     * Computes the position of the correct captcha answer.
     * <p>
     * Makes sure the new correct answer is never at the same position as the previous
     * click. This will keep spammers from just clicking at the same position until they
     * gave the right answer by lucky chance.
     *
     * @param session
     *            {@link HttpSession} with captcha data
     * @return position of the correct answer
     */
    private int computeCaptchaPosition(HttpSession session) {
        int newPos;

        Integer oldPos = getLastclickPosition(session);
        if (oldPos != null) {
            // Make sure newPos is always != oldPos
            newPos = rnd.nextInt(NUMBER_OF_CHARS - 1);
            if (newPos >= oldPos) {
                newPos++;
            }
        } else {
            newPos = rnd.nextInt(NUMBER_OF_CHARS);
        }

        setCaptchaPosition(session, newPos);
        return newPos;
    }

    /**
     * Gets the last captcha position from the session.
     *
     * @param session
     *            {@link HttpSession}
     * @return the last captcha position, or {@code null} if there is none yet
     */
    private Integer getCaptchaPosition(HttpSession session) {
        return (Integer) session.getAttribute(CAPTCHA_NAME);
    }

    /**
     * Sets the captcha position.
     *
     * @param session
     *            {@link HttpSession}
     * @param pos
     *            captcha position to store
     */
    private void setCaptchaPosition(HttpSession session, int pos) {
        session.setAttribute(CAPTCHA_NAME, pos);
    }

    /**
     * Gets the position of the last click.
     *
     * @param session
     *            {@link HttpSession}
     * @return position of the last click, or {@code null} if the user did not click yet
     */
    private Integer getLastclickPosition(HttpSession session) {
        return (Integer) session.getAttribute(LASTCLICK_NAME);
    }

    /**
     * Sets the position of the last click.
     *
     * @param session
     *            {@link HttpSession}
     * @param pos
     *            position of the last click
     */
    private void setLastclickPosition(HttpSession session, int pos) {
        session.setAttribute(LASTCLICK_NAME, pos);
    }

    public char getCaptchaLetter() {
        return captchaLetter;
    }
}
