package example.khoa.ows;

import javax.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "ows")
@Validated
public class ServiceProperties {

  @NotBlank
  private String dbSchema;

  @NotBlank
  private String gebcoBaseUrl;

  @NotBlank
  private String gebcoAuthToken;

  public String getDbSchema() {
    return dbSchema;
  }

  public void setDbSchema(String dbSchema) {
    this.dbSchema = dbSchema;
  }

  public String getGebcoBaseUrl() {
    return gebcoBaseUrl;
  }

  public void setGebcoBaseUrl(String gebcoBaseUrl) {
    this.gebcoBaseUrl = gebcoBaseUrl;
  }

  public String getGebcoAuthToken() {
    return gebcoAuthToken;
  }

  public void setGebcoAuthToken(String gebcoAuthToken) {
    this.gebcoAuthToken = gebcoAuthToken;
  }
}
