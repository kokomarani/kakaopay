package com.kakaopay.project.handler;

import com.kakaopay.project.exception.ErrorResponse;
import com.kakaopay.project.exception.ProjectException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class)
@RestController
public class ApiExceptionHandlerAdvice {
	@ExceptionHandler(value= ProjectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ErrorResponse exception(ProjectException exception, WebRequest request) {
		return new ErrorResponse(exception.getResultCode()
						, (StringUtils.isEmpty(exception.getMessage()) ? exception.getDefaultMsg() : exception.getMessage()));
	}
}
