package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.exp;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PaymeExceptionHandler extends AbstractHandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler,
                                              Exception ex) {
//        try {
//            if (ex instanceof PaymeException paymeException) {
//                PaymeResponse errorResponse = PaymeResponse.error(
//                        paymeException.getRequestId(),
//                        PaymeError.error(
//                                paymeException.getCode(),
//                                paymeException.getDesc(),
//                                paymeException.getData()
//                        )
//                );
//
//                response.setStatus(HttpServletResponse.SC_OK); // Payme kutayotgan status kod
//                response.setContentType("application/json;charset=UTF-8");
//                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//                response.getWriter().flush();
//                response.getWriter().close();
//
//                return new ModelAndView(); // Important: model view bo'sh qaytadi
//            }
//        } catch (IOException handlerException) {
//            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
//        }
        return null; // Agar PaymeException bo'lmasa boshqa handlerlar uchun o'tkazamiz
    }
}
