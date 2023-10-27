package example.khoa.ows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class HttpLoggingInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpLoggingInterceptor.class);

  @Override
  public ClientHttpResponse intercept(HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
    LOGGER.debug("Request headers: {}", req.getHeaders());
    LOGGER.debug("Request body: {}", new String(reqBody, StandardCharsets.UTF_8));
    ClientHttpResponse response = ex.execute(req, reqBody);
    InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
    String body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
    LOGGER.debug("Response headers: {}", response.getHeaders());
    LOGGER.debug("Response body: {}", body);
    return response;
  }
}
