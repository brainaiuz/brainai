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
package com.edatasite.workforce.gwt.core.server.controllers.signup.captcha;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.server.controllers.signup.captcha.impl.DefaultCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;

/**
 * This servlet creates a random captcha image, stores the correct answer in the http
 * session, and streams the generated captcha image as PNG.
 *
 * @author Richard "Shred" Körber
 */
public class CaptchaServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 3241024444677649962L;
    private static final Logger LOG = LoggerFactory.getLogger(CaptchaServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            // Prepare header
            response.setDateHeader("Date", System.currentTimeMillis());
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/png");

            // Write captcha image
            DefaultCaptchaService captchaService = (DefaultCaptchaService) ApplicationContextProvider.applicationContext.getBean("defaultCaptchaService");
            BufferedImage challenge = captchaService.createCaptcha(request.getSession());
            ImageIO.write(challenge, "png", response.getOutputStream());
        } catch (IOException | BeansException ex) {
            LOG.warn("Failed to generate captcha image", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()); //NOSONAR
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DefaultCaptchaService captchaService = (DefaultCaptchaService) ApplicationContextProvider.applicationContext.getBean("defaultCaptchaService");
        if (request.getParameter("captchaLetter") != null) {
            char captchaLetter = captchaService.getCaptchaLetter();
            response.getWriter().write(String.valueOf(captchaLetter));
            response.getWriter().flush();
        } else {
            String x = request.getParameter("x");
            boolean valid = false;
            if (x != null) {
                valid = captchaService.isValidCaptcha(request.getSession(), Integer.parseInt(x));
            }
            response.getWriter().write(valid ? "true" : "false");
            response.getWriter().flush();
        }
    }
}
