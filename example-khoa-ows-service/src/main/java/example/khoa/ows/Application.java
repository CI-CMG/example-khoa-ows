package example.khoa.ows;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
public class Application {

  public static void main(String[] args) {
    File svcHome = new ApplicationHome().getDir();
    String path = svcHome.getAbsolutePath();
    System.setProperty("svc.home", path);
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public RestTemplate restTemplate(
      RestTemplateBuilder builder,
      ServiceProperties serviceProperties,
      HttpLoggingInterceptor loggingInterceptor) {

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(10000);
    requestFactory.setReadTimeout(30000);

    return builder
        .requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory)) //To support logging without consuming the body
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceProperties.getGebcoAuthToken())
        .additionalInterceptors(loggingInterceptor)
        .build();
  }

}
