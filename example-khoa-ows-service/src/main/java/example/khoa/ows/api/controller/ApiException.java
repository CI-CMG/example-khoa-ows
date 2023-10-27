package example.khoa.ows.api.controller;

import example.khoa.ows.api.view.ApiErrorView;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final ApiErrorView apiErrorView;
  private final HttpStatus httpStatus;

  public ApiException(HttpStatus httpStatus, ApiErrorView apiErrorView) {
    super();
    this.httpStatus = httpStatus;
    this.apiErrorView = apiErrorView;
  }

  public ApiException(String message, HttpStatus httpStatus, ApiErrorView apiErrorView) {
    super(message);
    this.httpStatus = httpStatus;
    this.apiErrorView = apiErrorView;
  }

  public ApiException(String message, Throwable cause, HttpStatus httpStatus,
      ApiErrorView apiErrorView) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.apiErrorView = apiErrorView;
  }

  public ApiException(Throwable cause, HttpStatus httpStatus, ApiErrorView apiErrorView) {
    super(cause);
    this.httpStatus = httpStatus;
    this.apiErrorView = apiErrorView;
  }

  public ApiErrorView getApiErrorView() {
    return apiErrorView;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
