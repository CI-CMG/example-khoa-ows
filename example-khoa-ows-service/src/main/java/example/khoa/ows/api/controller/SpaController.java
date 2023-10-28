package example.khoa.ows.api.controller;

import example.khoa.ows.ServiceProperties;
import example.khoa.ows.api.view.ApiErrorView;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpaController implements ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpaController.class);

  private final String indexHtml;
  private final ServiceProperties properties;

  @Autowired
  public SpaController(
      @Value("classpath:static/index.html") Resource indexHtmlFile,
      @Value("#{servletContext.contextPath}") String servletContextPath,
      ServiceProperties properties
  ) {
    this.properties = properties;
    try (InputStream in = indexHtmlFile.getInputStream()) {
      indexHtml = StreamUtils.copyToString(in, StandardCharsets.UTF_8)
          .replaceAll("@contextRoot@", servletContextPath);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read index.html", e);
    }
  }

  @GetMapping({"", "/view/**"})
  @ResponseBody
  public String loadApplication() {
    return indexHtml;
  }

}
