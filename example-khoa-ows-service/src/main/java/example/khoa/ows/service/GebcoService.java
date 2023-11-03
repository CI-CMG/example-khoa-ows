package example.khoa.ows.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.khoa.ows.ServiceProperties;
import example.khoa.ows.api.controller.ApiException;
import example.khoa.ows.api.view.ApiErrorView;
import example.khoa.ows.api.view.FeatureView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GebcoService {


  private final ObjectMapper objectMapper;
  private final RestTemplate restTemplate;
  private final String baseUrl;
  @Autowired
  public GebcoService(ObjectMapper objectMapper, RestTemplate restTemplate, ServiceProperties serviceProperties) {
    this.objectMapper = objectMapper;
    this.restTemplate = restTemplate;
    baseUrl = serviceProperties.getGebcoBaseUrl() + "/api/v1";
  }

  public GebcoFeature getFeature(String featureStateId) {
    return get(featureStateId);
  }


  public GebcoFeature createFeature(FeatureView featureView) {
    return post(owsToGebco(featureView));
  }

  public GebcoFeature updateFeature(FeatureView featureView, String featureStateId) {
    return put(owsToGebco(featureView), featureStateId);
  }

  public void deleteFeature(String featureStateId) {
    delete(featureStateId);
  }

  public GebcoFeature updateState(String featureStateId, String state) {
    GebcoFeature existing = getFeature(featureStateId);
    if (existing.getApprovalState().equals(state)) {
      return existing;
    }
    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setApprovalState(state);
    gebcoFeature.setVersion(existing.getVersion());
    return patch(gebcoFeature, featureStateId);
  }

  private GebcoFeature owsToGebco(FeatureView featureView) {
    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setName(featureView.getName());
    gebcoFeature.setGenericTerm(featureView.getGenericTerm());
    gebcoFeature.setVersion(featureView.getGebcoFeatureVersion());
    gebcoFeature.setGeometries(featureView.getGeometries());
    gebcoFeature.setProposerId(featureView.getProposerId());
    gebcoFeature.setDiscovererId(featureView.getDiscovererId());
    gebcoFeature.setDiscoveryYear(featureView.getDiscoveryYear());
    gebcoFeature.setProposalYear(featureView.getProposalYear());
    gebcoFeature.setMeetings(featureView.getMeetings());
    gebcoFeature.setMinDepth(featureView.getMinDepth());
    gebcoFeature.setMaxDepth(featureView.getMaxDepth());
    gebcoFeature.setTotalRelief(featureView.getTotalRelief());
    gebcoFeature.setDimension(featureView.getDimension());
    gebcoFeature.setHistory(featureView.getHistory());
    gebcoFeature.setRemarks(featureView.getRemarks());
    gebcoFeature.setComments(featureView.getComments());
    return gebcoFeature;
  }

  private String jsonSerialize(GebcoFeature gebcoFeature) {
    try {
      return objectMapper.writeValueAsString(gebcoFeature);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to serialize GEBCO feature request", e);
    }
  }

  private GebcoFeature jsonDeserializeGebcoFeature(ResponseEntity<String> response) {
    try {
      return objectMapper.readValue(response.getBody(), GebcoFeature.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to serialize GEBCO feature response", e);
    }
  }

  private ApiErrorView jsonDeserializeApiError(ResponseEntity<String> response) {
    ApiErrorView errorView;
    try {
      return objectMapper.readValue(response.getBody(), ApiErrorView.class);
    } catch (JsonProcessingException e) {
      errorView = new ApiErrorView();
      errorView.addFlashError("Unsuccessful request to GEBCO API: " + response.getStatusCode());
    }
    return errorView;
  }


  private GebcoFeature post(GebcoFeature gebcoFeature) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String requestBody = jsonSerialize(gebcoFeature);
    ResponseEntity<String> response = restTemplate.exchange(
        UriComponentsBuilder.fromUriString(baseUrl).pathSegment("feature").encode().toUriString(),
        HttpMethod.POST,
        new HttpEntity<>(requestBody, headers),
        String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return jsonDeserializeGebcoFeature(response);
    }
    throw new ApiException("Unsuccessful request to GEBCO API: " + response.getStatusCode(), response.getStatusCode(),
        jsonDeserializeApiError(response));
  }


  private GebcoFeature put(GebcoFeature gebcoFeature, String featureStateId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String requestBody = jsonSerialize(gebcoFeature);
    ResponseEntity<String> response = restTemplate.exchange(
        UriComponentsBuilder.fromUriString(baseUrl).pathSegment("feature").pathSegment(featureStateId).encode().toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(requestBody, headers),
        String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return jsonDeserializeGebcoFeature(response);
    }
    throw new ApiException("Unsuccessful request to GEBCO API: " + response.getStatusCode(), response.getStatusCode(),
        jsonDeserializeApiError(response));
  }

  private GebcoFeature patch(GebcoFeature gebcoFeature, String featureStateId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String requestBody = jsonSerialize(gebcoFeature);
    ResponseEntity<String> response = restTemplate.exchange(
        UriComponentsBuilder.fromUriString(baseUrl).pathSegment("feature-state").pathSegment(featureStateId).encode().toUriString(),
        HttpMethod.PATCH,
        new HttpEntity<>(requestBody, headers),
        String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return jsonDeserializeGebcoFeature(response);
    }
    throw new ApiException("Unsuccessful request to GEBCO API: " + response.getStatusCode(), response.getStatusCode(),
        jsonDeserializeApiError(response));
  }

  private GebcoFeature delete(String featureStateId) {
    ResponseEntity<String> response = restTemplate.exchange(
        UriComponentsBuilder.fromUriString(baseUrl).pathSegment("feature").pathSegment(featureStateId).encode().toUriString(),
        HttpMethod.DELETE,
        null,
        String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return jsonDeserializeGebcoFeature(response);
    }
    throw new ApiException("Unsuccessful request to GEBCO API: " + response.getStatusCode(), response.getStatusCode(),
        jsonDeserializeApiError(response));
  }

  private GebcoFeature get(String featureStateId) {
    ResponseEntity<String> response = restTemplate.exchange(
        UriComponentsBuilder.fromUriString(baseUrl).pathSegment("feature").pathSegment(featureStateId).encode().toUriString(),
        HttpMethod.GET,
        null,
        String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      return jsonDeserializeGebcoFeature(response);
    }
    throw new ApiException("Unsuccessful request to GEBCO API: " + response.getStatusCode(), response.getStatusCode(),
        jsonDeserializeApiError(response));
  }
}
