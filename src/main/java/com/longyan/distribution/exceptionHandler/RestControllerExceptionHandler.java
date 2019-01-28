package com.longyan.distribution.exceptionHandler;

import com.sug.core.platform.exception.*;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.platform.web.rest.exception.SimpleErrorResponse;
import com.sug.core.platform.web.rest.exception.SimpleErrorResponseBuilder;
import com.sug.core.platform.web.rest.runtime.RuntimeEnvironment;
import com.sug.core.platform.web.rest.runtime.RuntimeSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice({
        "com.longyan.distribution.controller"
})
public class RestControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @Autowired
    protected SimpleErrorResponseBuilder errorResponseBuilder;

    @Autowired
    private RuntimeSettings runtimeSettings;

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public SimpleErrorResponse runtimeException(RuntimeException e) {
        return errorResponseBuilder.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),e, true);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleErrorResponse resourceNotFound(ResourceNotFoundException ex) {
        return errorResponseBuilder.createErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),ex, isLog());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public SimpleErrorResponse forbidden(ForbiddenException ex) {
        return errorResponseBuilder.createErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(),ex, isLog());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleErrorResponse invalidRequestError(InvalidRequestException e) {
        return errorResponseBuilder.createErrorResponse(e.getCode(), e, isLog());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleErrorResponse validationError(MethodArgumentNotValidException e) {
        return errorResponseBuilder.createValidationResponse(e, isLog());
    }


    @ExceptionHandler(value = SessionExpiredException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleErrorResponse sessionExpired(SessionExpiredException ex) {
        return errorResponseBuilder.createErrorResponse(ex, isLog());
    }

    @ExceptionHandler(value = VisitorNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleErrorResponse visitorNotFound(VisitorNotFoundException ex) {
        return errorResponseBuilder.createErrorResponse(ex, isLog());
    }

    @ExceptionHandler(value = LoginRequiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public SimpleErrorResponse loginRequired(LoginRequiredException ex) {
        return errorResponseBuilder.createErrorResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(),ex, isLog());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public SimpleErrorResponse invalidForm(HttpMessageNotReadableException ex) {
        return errorResponseBuilder.createErrorResponse(ex, isLog());
    }

    private boolean isLog() {
        return !RuntimeEnvironment.prod.equals(runtimeSettings.getEnvironment());
    }
}
