package com.edatasite.workforce.rest.v2.release10.exceptionhandling;


import com.edatasite.workforce.rest.aspects.CheckPermissionException;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anvar Akramov on 11/8/17.
 */
public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean, ApiConstants {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    private HttpMessageConverter<?>[] messageConverters = null;

    private List<HttpMessageConverter<?>> allMessageConverters = null;

    /*private RestErrorResolver errorResolver;

    private RestErrorConverter<?> errorConverter;*/

    public RestExceptionHandler() {
        /*this.errorResolver = new DefaultRestErrorResolver();
        this.errorConverter = new MapRestErrorConverter();*/
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    /*public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public RestErrorResolver getErrorResolver() {
        return this.errorResolver;
    }

    public RestErrorConverter<?> getErrorConverter() {
        return errorConverter;
    }

    public void setErrorConverter(RestErrorConverter<?> errorConverter) {
        this.errorConverter = errorConverter;
    }*/

    @Override
    public void afterPropertiesSet() throws Exception {
        ensureMessageConverters();
    }

    @SuppressWarnings("unchecked")
    private void ensureMessageConverters() {

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();

        //user configured values take precedence:
        if (this.messageConverters != null && this.messageConverters.length > 0) {
            converters.addAll(Arrays.stream(this.messageConverters).toList());
        }

        //defaults next:
        new HttpMessageConverterHelper().addDefaults(converters);

        this.allMessageConverters = converters;
    }

    //leverage Spring's existing default setup behavior:
    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }

    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate.
     * <p/>
     * May be overridden in subclasses, in order to apply specific
     * exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
     * ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
     *                 if multipart resolution failed)
     * @param ex       the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ServletWebRequest webRequest = new ServletWebRequest(request, response);

//        RestErrorResolver resolver = getErrorResolver();

//        RestError error = resolver.resolveError(webRequest, handler, ex);

        ApiResult error = null;
        int errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        //Generate ErrorResponse
        try {
            if (ex instanceof RestException e1) {
                error = new ApiResult(e1.getUser_msg(), e1.getDeveloper_msg(), e1.getError_code());
                errorcode = e1.getStatus().value();
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_NOT_ACCEPTABLE;
            } else if (ex instanceof MissingPathVariableException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), SERVER_ERROR);
                errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            } else if (ex instanceof MissingServletRequestParameterException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof ServletRequestBindingException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof ConversionNotSupportedException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), SERVER_ERROR);
                errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            } else if (ex instanceof TypeMismatchException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof HttpMessageNotReadableException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof HttpMessageNotWritableException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), SERVER_ERROR);
                errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            } else if (ex instanceof MethodArgumentNotValidException) {
                List<String> errorList = ((MethodArgumentNotValidException) ex)
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();

                error = new ApiResult(IN_VALID_DATA, String.join(" \n", errorList), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof MissingServletRequestPartException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof BindException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), INVALID);
                errorcode = HttpServletResponse.SC_BAD_REQUEST;
            } else if (ex instanceof NoHandlerFoundException) {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage(), NOT_FOUND);
                errorcode = HttpServletResponse.SC_NOT_FOUND;
            } else if (ex instanceof NullPointerException) {//if ex type is null pointer, message will be null,so we use ex.toString
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.toString(), SERVER_ERROR);
                errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            } else if (ex instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) ex).getUndeclaredThrowable() instanceof CheckPermissionException) {
                error = new ApiResult(PERMISSION_MESSAGE, ex.toString(), ACCESS_DENIED);
                errorcode = HttpServletResponse.SC_FORBIDDEN;
            } else {
                error = new ApiResult(GENERAL_ERROR_MESSAGE, ex.getMessage() != null ? ex.getMessage() : ex.toString(), SERVER_ERROR);
                errorcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }
            log.error(ex.getMessage() != null ? ex.getMessage() : ex.toString());
            ex.printStackTrace();
        } catch (Exception e) {
            log.error("", e);
            e.printStackTrace();
        }
        //End of Generate ErrorResponse
        if (error == null) {
            return null;
        }

        ModelAndView mav = null;

        try {
//            mav = getModelAndView(webRequest, handler, error);
            response.setStatus(errorcode);
            mav = handleResponseBody(error, webRequest);
        } catch (Exception invocationEx) {
            log.error("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", invocationEx);
            ex.printStackTrace();
        }

        return mav;
    }

    /*protected ModelAndView getModelAndView(ServletWebRequest webRequest, Object handler, RestError error) throws Exception {

        applyStatusIfPossible(webRequest, error);

        Object body = error; //default the error instance in case they don't configure an error converter

        RestErrorConverter converter = getErrorConverter();
        if (converter != null) {
            body = converter.convert(error);
        }

        return handleResponseBody(body, webRequest);
    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, RestError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(error.getStatus().value());
        }
        //TODO support response.sendError ?
    }*/

    @SuppressWarnings("unchecked")
    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws ServletException, IOException {

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());

        Class<?> bodyType = body.getClass();

        List<HttpMessageConverter<?>> converters = this.allMessageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        //return empty model and view to short circuit the iteration and to let
                        //Spring know that we've rendered the view ourselves:
                        return new ModelAndView();
                    }
                }
            }
        }

        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType +
                    "] and " + acceptedMediaTypes);
        }
        return null;
    }
}
