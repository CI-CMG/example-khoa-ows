package example.khoa.ows.api.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import example.khoa.ows.api.view.ApiErrorView;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    ApiErrorView errorView = new ApiErrorView();
    errorView.addFlashError(String.format("Missing Request Parameter '%s'", ex.getParameterName()));
    return handleExceptionInternal(ex, errorView, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    ApiErrorView errorView = new ApiErrorView();
    String name = ex.getPropertyName();
    if (!StringUtils.hasText(name)) {
      if (ex instanceof MethodArgumentTypeMismatchException) {
        name = ((MethodArgumentTypeMismatchException) ex).getName();
      }
    }
    errorView.addFlashError(String.format("Invalid Parameter '%s'", name));
    return handleExceptionInternal(ex, errorView, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiErrorView errorView = new ApiErrorView();
    errorView.addFlashError("Invalid Request");
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    errors.forEach(error -> errorView.addFormError(error.getField(), error.getDefaultMessage()));
    return handleExceptionInternal(ex, errorView, headers, HttpStatus.UNPROCESSABLE_ENTITY,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (ex.getCause() instanceof JsonMappingException) {
      return (ResponseEntity) handleJsonMappingException((JsonMappingException) ex.getCause());
    }

    ApiErrorView errorView = new ApiErrorView();
    errorView.addFlashError(status.getReasonPhrase());

    return handleExceptionInternal(ex, errorView, headers, status, request);
  }

  private static String toPath(List<Reference> pathParts) {
    StringBuilder pathBuilder = new StringBuilder();
    for (int i = 0; i < pathParts.size(); i++) {
      Reference reference = pathParts.get(i);
      if (reference.getIndex() >= 0) {
        pathBuilder.append("[").append(reference.getIndex()).append("]");
      } else {
        if (i != 0) {
          pathBuilder.append(".");
        }
        pathBuilder.append(reference.getFieldName());
      }
    }
    return pathBuilder.toString();
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    ApiErrorView errorView = new ApiErrorView();
    errorView.addFlashError("Invalid Request");
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    errors.forEach(error -> errorView.addFormError(error.getField(), error.isBindingFailure() ? "Invalid Type" : error.getDefaultMessage()));
    return handleExceptionInternal(ex, errorView, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    LOGGER.debug("Unable to process request", ex);

    if (body == null) {
      ApiErrorView errorView = new ApiErrorView();
      errorView.addFlashError(status.getReasonPhrase());
      body = errorView;
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }


  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ApiErrorView> handleJsonMappingException(JsonMappingException ex) {
    LOGGER.debug("Unable to process JSON", ex);
    ApiErrorView errorView = new ApiErrorView();
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    errorView.addFlashError("Invalid Request");
    errorView.addFormError(toPath(ex.getPath()), "Invalid Type");
    return new ResponseEntity<>(errorView, status);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorView> handleApiError(ApiException ex) {
    LOGGER.debug("Controller rejected request", ex);
    return new ResponseEntity<>(ex.getApiErrorView(), ex.getHttpStatus());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiErrorView> fallbackErrorHandler(RuntimeException e) {
    LOGGER.error("An error occurred while processing request", e);
    ApiErrorView errorView = new ApiErrorView();
    errorView.addFlashError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity<>(errorView, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
