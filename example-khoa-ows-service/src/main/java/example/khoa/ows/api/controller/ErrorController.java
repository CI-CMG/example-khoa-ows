package example.khoa.ows.api.controller;

import example.khoa.ows.api.view.ApiErrorView;
import io.swagger.v3.oas.annotations.Hidden;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Hidden
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

  @RequestMapping("/error")
  public String error(HttpServletRequest request) {
    Object exObj = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (exObj instanceof Exception) {
      LOGGER.debug("Web Exception", (Exception) exObj);
    }

    if (exObj instanceof ApiException) {
      throw (ApiException) exObj;
    }

    if (exObj instanceof Exception && ((Exception) exObj).getCause() instanceof ApiException) {
      throw (ApiException) ((Exception) exObj).getCause();
    }

    Object codeObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (codeObj instanceof Integer) {
      HttpStatus status = HttpStatus.resolve((Integer) codeObj);
      if (status == null) {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
      }
      ApiErrorView errorView = new ApiErrorView();
      errorView.addFlashError(status.getReasonPhrase());
      throw new ApiException(status, errorView);
    }

    if (exObj instanceof RuntimeException) {
      throw (RuntimeException) exObj;
    }
    if (exObj instanceof Exception) {
      throw new RuntimeException("An error occurred processing request", (Exception) exObj);
    }
    throw new IllegalStateException("An error occurred processing request");
  }
}