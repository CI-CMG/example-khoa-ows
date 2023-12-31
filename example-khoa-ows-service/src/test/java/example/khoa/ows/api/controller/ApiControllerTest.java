package example.khoa.ows.api.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.khoa.ows.api.view.FeatureView;
import example.khoa.ows.db.FeatureRepository;
import example.khoa.ows.jpa.FeatureEntity;
import example.khoa.ows.service.GebcoFeature;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApiControllerTest {

  private static MockWebServer mockGebcoGazetteer;

  @Autowired
  private FeatureRepository featureRepository;
  @Autowired
  private TransactionTemplate tx;
  @Autowired
  private TestRestTemplate testRestClient;
  @Autowired
  private ObjectMapper objectMapper;


  @BeforeAll
  public static void beforeAll() throws IOException {
    mockGebcoGazetteer = new MockWebServer();
    mockGebcoGazetteer.start(20158);
  }

  @AfterAll
  public static void afterAll() throws Exception {
    mockGebcoGazetteer.shutdown();
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    tx.executeWithoutResult(s -> {
      featureRepository.deleteAll();
      featureRepository.flush();
    });
  }

  @AfterEach
  public void afterEach() throws Exception {
    tx.executeWithoutResult(s -> {
      featureRepository.deleteAll();
      featureRepository.flush();
    });
  }

  @Test
  public void testCreate() throws Exception {
    GebcoFeature createdFeature = new GebcoFeature();
    createdFeature.setFeatureId("12");
    createdFeature.setFeatureStateId("23");
    createdFeature.setName("Teddy Bear");
    createdFeature.setGenericTerm("Ridge");
    createdFeature.setVersion(1);
    createdFeature.setApprovalState("EDIT");
    createdFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    createdFeature.setProposerId("44");
    createdFeature.setDiscovererId("55");
    createdFeature.setDiscoveryYear(1987);
    createdFeature.setProposalYear(2023);
    createdFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    createdFeature.setMinDepth(10);
    createdFeature.setMaxDepth(100);
    createdFeature.setTotalRelief(45);
    createdFeature.setDimension("33 x 100");
    createdFeature.setHistory("Named after a teddy bear.");
    createdFeature.setRemarks("It's fluffy");
    createdFeature.setComments("Teddy");
    createdFeature.setTimeLastUpdated(Instant.now());
    createdFeature.setEditor("ows");
    createdFeature.setOwner("ows");

    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(createdFeature)));

    FeatureView newFeature = new FeatureView();
    newFeature.setName("Teddy Bear");
    newFeature.setGenericTerm("Ridge");
    newFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    newFeature.setProposerId("44");
    newFeature.setDiscovererId("55");
    newFeature.setDiscoveryYear(1987);
    newFeature.setProposalYear(2023);
    newFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    newFeature.setMinDepth(10);
    newFeature.setMaxDepth(100);
    newFeature.setTotalRelief(45);
    newFeature.setDimension("33 x 100");
    newFeature.setHistory("Named after a teddy bear.");
    newFeature.setRemarks("It's fluffy");
    newFeature.setComments("Teddy");
    newFeature.setOwsNote("Survey information");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/load",
        HttpMethod.POST,
        new HttpEntity<>(newFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature", request1.getRequestUrl().encodedPath());
    assertEquals("POST", request1.getMethod());

    createdFeature.setFeatureId(null);
    createdFeature.setFeatureStateId(null);
    createdFeature.setVersion(null);
    createdFeature.setEditor(null);
    createdFeature.setOwner(null);
    createdFeature.setTimeLastUpdated(null);
    createdFeature.setApprovalState(null);
    assertEquals(createdFeature, objectMapper.readValue(request1.getUtf8Body(), GebcoFeature.class));

    tx.executeWithoutResult(s -> {
      FeatureEntity expected = new FeatureEntity();
      expected.setId(response.getBody().getId());
      expected.setGebcoFeatureStateId("23");
      expected.setNote("Survey information");
      assertEquals(expected, featureRepository.findById(response.getBody().getId()).get());
    });

  }

  @Test
  public void testDisapprove() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature deletedFeature = new GebcoFeature();
    deletedFeature.setFeatureId("12");
    deletedFeature.setFeatureStateId("23");
    deletedFeature.setName("Teddy Bear");
    deletedFeature.setGenericTerm("Ridge");
    deletedFeature.setVersion(1);
    deletedFeature.setApprovalState("EDIT");
    deletedFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    deletedFeature.setProposerId("44");
    deletedFeature.setDiscovererId("55");
    deletedFeature.setDiscoveryYear(1987);
    deletedFeature.setProposalYear(2023);
    deletedFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    deletedFeature.setMinDepth(10);
    deletedFeature.setMaxDepth(100);
    deletedFeature.setTotalRelief(45);
    deletedFeature.setDimension("33 x 100");
    deletedFeature.setHistory("Named after a teddy bear.");
    deletedFeature.setRemarks("It's fluffy");
    deletedFeature.setComments("Teddy");
    deletedFeature.setTimeLastUpdated(Instant.now());
    deletedFeature.setEditor("ows");
    deletedFeature.setOwner("ows");

    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(deletedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(1);
    existingFeature.setGebcoApprovalState("EDIT");


    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/disapprove",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("DELETE", request1.getMethod());


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isEmpty());
    });
  }

  @Test
  public void testPending() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setFeatureId("12");
    gebcoFeature.setFeatureStateId("23");
    gebcoFeature.setName("Teddy Bear");
    gebcoFeature.setGenericTerm("Ridge");
    gebcoFeature.setVersion(1);
    gebcoFeature.setApprovalState("EDIT");
    gebcoFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    gebcoFeature.setProposerId("44");
    gebcoFeature.setDiscovererId("55");
    gebcoFeature.setDiscoveryYear(1987);
    gebcoFeature.setProposalYear(2023);
    gebcoFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    gebcoFeature.setMinDepth(10);
    gebcoFeature.setMaxDepth(100);
    gebcoFeature.setTotalRelief(45);
    gebcoFeature.setDimension("33 x 100");
    gebcoFeature.setHistory("Named after a teddy bear.");
    gebcoFeature.setRemarks("It's fluffy");
    gebcoFeature.setComments("Teddy");
    gebcoFeature.setTimeLastUpdated(Instant.now());
    gebcoFeature.setEditor("ows");
    gebcoFeature.setOwner("ows");


    GebcoFeature patchedFeature = gebcoFeature.copy();
    patchedFeature.setVersion(gebcoFeature.getVersion() + 1);
    patchedFeature.setApprovalState("PENDING");

    // GET check EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // GET check PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // PATCH set PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(patchedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(1);
    existingFeature.setGebcoApprovalState("EDIT");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/pending",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request2.getRequestUrl().encodedPath());
    assertEquals("GET", request2.getMethod());

    RecordedRequest request3 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request3.getRequestUrl().encodedPath());
    assertEquals("PATCH", request3.getMethod());

    GebcoFeature expectedPatch = new GebcoFeature();
    expectedPatch.setVersion(gebcoFeature.getVersion());
    expectedPatch.setApprovalState("PENDING");
    assertEquals(expectedPatch, objectMapper.readValue(request3.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isPresent());
    });
  }

  @Test
  public void testPendingNotInEditState() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setFeatureId("12");
    gebcoFeature.setFeatureStateId("23");
    gebcoFeature.setName("Teddy Bear");
    gebcoFeature.setGenericTerm("Ridge");
    gebcoFeature.setVersion(1);
    gebcoFeature.setApprovalState("READY");
    gebcoFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    gebcoFeature.setProposerId("44");
    gebcoFeature.setDiscovererId("55");
    gebcoFeature.setDiscoveryYear(1987);
    gebcoFeature.setProposalYear(2023);
    gebcoFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    gebcoFeature.setMinDepth(10);
    gebcoFeature.setMaxDepth(100);
    gebcoFeature.setTotalRelief(45);
    gebcoFeature.setDimension("33 x 100");
    gebcoFeature.setHistory("Named after a teddy bear.");
    gebcoFeature.setRemarks("It's fluffy");
    gebcoFeature.setComments("Teddy");
    gebcoFeature.setTimeLastUpdated(Instant.now());
    gebcoFeature.setEditor("ows");
    gebcoFeature.setOwner("ows");

    GebcoFeature editPatchedFeature = gebcoFeature.copy();
    editPatchedFeature.setVersion(gebcoFeature.getVersion() + 1);
    editPatchedFeature.setApprovalState("EDIT");

    GebcoFeature pendingPatchedFeature = gebcoFeature.copy();
    pendingPatchedFeature.setVersion(editPatchedFeature.getVersion() + 1);
    pendingPatchedFeature.setApprovalState("PENDING");

    // GET check EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // PATCH set EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(editPatchedFeature)));
    // GET check PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(editPatchedFeature)));
    // PATCH set PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(pendingPatchedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(1);
    existingFeature.setGebcoApprovalState("EDIT");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/pending",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request2.getRequestUrl().encodedPath());
    assertEquals("PATCH", request2.getMethod());

    RecordedRequest request3 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request3.getRequestUrl().encodedPath());
    assertEquals("GET", request3.getMethod());

    RecordedRequest request4 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request4.getRequestUrl().encodedPath());
    assertEquals("PATCH", request4.getMethod());

    GebcoFeature expectedPatch = new GebcoFeature();
    expectedPatch.setVersion(editPatchedFeature.getVersion());
    expectedPatch.setApprovalState("PENDING");
    assertEquals(expectedPatch, objectMapper.readValue(request4.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isPresent());
    });
  }

  @Test
  public void testReady() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setFeatureId("12");
    gebcoFeature.setFeatureStateId("23");
    gebcoFeature.setName("Teddy Bear");
    gebcoFeature.setGenericTerm("Ridge");
    gebcoFeature.setVersion(1);
    gebcoFeature.setApprovalState("EDIT");
    gebcoFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    gebcoFeature.setProposerId("44");
    gebcoFeature.setDiscovererId("55");
    gebcoFeature.setDiscoveryYear(1987);
    gebcoFeature.setProposalYear(2023);
    gebcoFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    gebcoFeature.setMinDepth(10);
    gebcoFeature.setMaxDepth(100);
    gebcoFeature.setTotalRelief(45);
    gebcoFeature.setDimension("33 x 100");
    gebcoFeature.setHistory("Named after a teddy bear.");
    gebcoFeature.setRemarks("It's fluffy");
    gebcoFeature.setComments("Teddy");
    gebcoFeature.setTimeLastUpdated(Instant.now());
    gebcoFeature.setEditor("ows");
    gebcoFeature.setOwner("ows");


    GebcoFeature patchedFeature = gebcoFeature.copy();
    patchedFeature.setVersion(gebcoFeature.getVersion() + 1);
    patchedFeature.setApprovalState("READY");

    // GET check EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // GET check READY state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // PATCH set READY state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(patchedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(2);
    existingFeature.setGebcoApprovalState("PENDING");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/ready",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request2.getRequestUrl().encodedPath());
    assertEquals("GET", request2.getMethod());

    RecordedRequest request3 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request3.getRequestUrl().encodedPath());
    assertEquals("PATCH", request3.getMethod());

    GebcoFeature expectedPatch = new GebcoFeature();
    expectedPatch.setVersion(gebcoFeature.getVersion());
    expectedPatch.setApprovalState("READY");
    assertEquals(expectedPatch, objectMapper.readValue(request3.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isPresent());
    });
  }

  @Test
  public void testReadyNotInEditState() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setFeatureId("12");
    gebcoFeature.setFeatureStateId("23");
    gebcoFeature.setName("Teddy Bear");
    gebcoFeature.setGenericTerm("Ridge");
    gebcoFeature.setVersion(2);
    gebcoFeature.setApprovalState("PENDING");
    gebcoFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    gebcoFeature.setProposerId("44");
    gebcoFeature.setDiscovererId("55");
    gebcoFeature.setDiscoveryYear(1987);
    gebcoFeature.setProposalYear(2023);
    gebcoFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    gebcoFeature.setMinDepth(10);
    gebcoFeature.setMaxDepth(100);
    gebcoFeature.setTotalRelief(45);
    gebcoFeature.setDimension("33 x 100");
    gebcoFeature.setHistory("Named after a teddy bear.");
    gebcoFeature.setRemarks("It's fluffy");
    gebcoFeature.setComments("Teddy");
    gebcoFeature.setTimeLastUpdated(Instant.now());
    gebcoFeature.setEditor("ows");
    gebcoFeature.setOwner("ows");

    GebcoFeature editPatchedFeature = gebcoFeature.copy();
    editPatchedFeature.setVersion(gebcoFeature.getVersion() + 1);
    editPatchedFeature.setApprovalState("EDIT");

    GebcoFeature readyPatchedFeature = gebcoFeature.copy();
    readyPatchedFeature.setVersion(editPatchedFeature.getVersion() + 1);
    readyPatchedFeature.setApprovalState("READY");

    // GET check EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    // PATCH set EDIT state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(editPatchedFeature)));
    // GET check PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(editPatchedFeature)));
    // PATCH set PENDING state
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(readyPatchedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(1);
    existingFeature.setGebcoApprovalState("EDIT");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/ready",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request2.getRequestUrl().encodedPath());
    assertEquals("PATCH", request2.getMethod());

    RecordedRequest request3 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request3.getRequestUrl().encodedPath());
    assertEquals("GET", request3.getMethod());

    RecordedRequest request4 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request4.getRequestUrl().encodedPath());
    assertEquals("PATCH", request4.getMethod());

    GebcoFeature expectedPatch = new GebcoFeature();
    expectedPatch.setVersion(editPatchedFeature.getVersion());
    expectedPatch.setApprovalState("READY");
    assertEquals(expectedPatch, objectMapper.readValue(request4.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isPresent());
    });
  }

  @Test
  public void testApprove() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature gebcoFeature = new GebcoFeature();
    gebcoFeature.setFeatureId("12");
    gebcoFeature.setFeatureStateId("23");
    gebcoFeature.setName("Teddy Bear");
    gebcoFeature.setGenericTerm("Ridge");
    gebcoFeature.setVersion(3);
    gebcoFeature.setApprovalState("READY");
    gebcoFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    gebcoFeature.setProposerId("44");
    gebcoFeature.setDiscovererId("55");
    gebcoFeature.setDiscoveryYear(1987);
    gebcoFeature.setProposalYear(2023);
    gebcoFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    gebcoFeature.setMinDepth(10);
    gebcoFeature.setMaxDepth(100);
    gebcoFeature.setTotalRelief(45);
    gebcoFeature.setDimension("33 x 100");
    gebcoFeature.setHistory("Named after a teddy bear.");
    gebcoFeature.setRemarks("It's fluffy");
    gebcoFeature.setComments("Teddy");
    gebcoFeature.setTimeLastUpdated(Instant.now());
    gebcoFeature.setEditor("ows");
    gebcoFeature.setOwner("ows");


    GebcoFeature patchedFeature = gebcoFeature.copy();
    patchedFeature.setVersion(gebcoFeature.getVersion() + 1);
    patchedFeature.setApprovalState("APPROVED");

    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(gebcoFeature)));
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(patchedFeature)));

    FeatureView existingFeature = new FeatureView();
    existingFeature.setId(id);
    existingFeature.setName("Teddy Bear");
    existingFeature.setGenericTerm("Ridge");
    existingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    existingFeature.setProposerId("44");
    existingFeature.setDiscovererId("55");
    existingFeature.setDiscoveryYear(1987);
    existingFeature.setProposalYear(2023);
    existingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    existingFeature.setMinDepth(10);
    existingFeature.setMaxDepth(100);
    existingFeature.setTotalRelief(45);
    existingFeature.setDimension("33 x 100");
    existingFeature.setHistory("Named after a teddy bear.");
    existingFeature.setRemarks("It's fluffy");
    existingFeature.setComments("Teddy");
    existingFeature.setOwsNote("Survey information");
    existingFeature.setGebcoFeatureStateId("23");
    existingFeature.setGebcoFeatureVersion(3);
    existingFeature.setGebcoApprovalState("PENDING");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/approve",
        HttpMethod.POST,
        new HttpEntity<>(existingFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request2.getRequestUrl().encodedPath());
    assertEquals("PATCH", request2.getMethod());

    GebcoFeature expectedPatch = new GebcoFeature();
    expectedPatch.setVersion(3);
    expectedPatch.setApprovalState("APPROVED");
    assertEquals(expectedPatch, objectMapper.readValue(request2.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertTrue(featureRepository.findById(id).isPresent());
    });
  }


  @Test
  public void testUpdate() throws Exception {
    String id = UUID.randomUUID().toString();
    tx.executeWithoutResult(s -> {
      FeatureEntity entity = new FeatureEntity();
      entity.setId(id);
      entity.setGebcoFeatureStateId("23");
      entity.setNote("Survey information");
      featureRepository.saveAndFlush(entity);
    });

    GebcoFeature startingFeature = new GebcoFeature();
    startingFeature.setFeatureId("12");
    startingFeature.setFeatureStateId("23");
    startingFeature.setName("Teddy Bear");
    startingFeature.setGenericTerm("Ridge");
    startingFeature.setVersion(5);
    startingFeature.setApprovalState("READY");
    startingFeature.setGeometries(Collections.singletonList("POINT (0 0)"));
    startingFeature.setProposerId("44");
    startingFeature.setDiscovererId("55");
    startingFeature.setDiscoveryYear(1987);
    startingFeature.setProposalYear(2023);
    startingFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    startingFeature.setMinDepth(10);
    startingFeature.setMaxDepth(100);
    startingFeature.setTotalRelief(45);
    startingFeature.setDimension("33 x 100");
    startingFeature.setHistory("Named after a teddy bear.");
    startingFeature.setRemarks("It's fluffy");
    startingFeature.setComments("Teddy");
    startingFeature.setTimeLastUpdated(Instant.now());
    startingFeature.setEditor("ows");
    startingFeature.setOwner("ows");


    GebcoFeature editFeature = startingFeature.copy();
    editFeature.setVersion(startingFeature.getVersion() + 1);
    editFeature.setApprovalState("EDIT");

    GebcoFeature modifiedFeature = editFeature.copy();
    modifiedFeature.setVersion(editFeature.getVersion() + 1);
    modifiedFeature.setGeometries(Collections.singletonList("POINT (1 1)"));

    GebcoFeature readyFeature = modifiedFeature.copy();
    readyFeature.setVersion(modifiedFeature.getVersion() + 1);
    readyFeature.setApprovalState("READY");

    // get check edit
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(startingFeature)));
    // set to edit
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(editFeature)));
    // put, modify
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(modifiedFeature)));
    // get check edit
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(modifiedFeature)));
    // get check ready
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(modifiedFeature)));
    // set to ready
    mockGebcoGazetteer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(readyFeature)));

    FeatureView updatedOwsFeature = new FeatureView();
    updatedOwsFeature.setId(id);
    updatedOwsFeature.setName("Teddy Bear");
    updatedOwsFeature.setGenericTerm("Ridge");
    updatedOwsFeature.setGeometries(Collections.singletonList("POINT (1 1)"));
    updatedOwsFeature.setProposerId("44");
    updatedOwsFeature.setDiscovererId("55");
    updatedOwsFeature.setDiscoveryYear(1987);
    updatedOwsFeature.setProposalYear(2023);
    updatedOwsFeature.setMeetings(Collections.singletonList("SCUFN-36"));
    updatedOwsFeature.setMinDepth(10);
    updatedOwsFeature.setMaxDepth(100);
    updatedOwsFeature.setTotalRelief(45);
    updatedOwsFeature.setDimension("33 x 100");
    updatedOwsFeature.setHistory("Named after a teddy bear.");
    updatedOwsFeature.setRemarks("It's fluffy");
    updatedOwsFeature.setComments("Teddy");
    updatedOwsFeature.setOwsNote("More survey information");
    updatedOwsFeature.setGebcoFeatureStateId("23");
    updatedOwsFeature.setGebcoFeatureVersion(5);
    updatedOwsFeature.setGebcoApprovalState("READY");

    ResponseEntity<FeatureView> response = testRestClient.exchange(
        "/api/update",
        HttpMethod.POST,
        new HttpEntity<>(updatedOwsFeature),
        FeatureView.class
    );
    assertEquals(200, response.getStatusCode().value());

    RecordedRequest request1 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request1.getRequestUrl().encodedPath());
    assertEquals("GET", request1.getMethod());

    RecordedRequest request2 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request2.getRequestUrl().encodedPath());
    assertEquals("PATCH", request2.getMethod());

    RecordedRequest request3 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request3.getRequestUrl().encodedPath());
    assertEquals("PUT", request3.getMethod());

    RecordedRequest request4 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request4.getRequestUrl().encodedPath());
    assertEquals("GET", request4.getMethod());

    RecordedRequest request5 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature/23", request5.getRequestUrl().encodedPath());
    assertEquals("GET", request5.getMethod());

    RecordedRequest request6 = mockGebcoGazetteer.takeRequest();
    assertEquals("/api/v1/feature-state/23", request6.getRequestUrl().encodedPath());
    assertEquals("PATCH", request6.getMethod());

    GebcoFeature expectedBody = new GebcoFeature();
    expectedBody.setVersion(5);
    expectedBody.setApprovalState("EDIT");
    assertEquals(expectedBody, objectMapper.readValue(request2.getUtf8Body(), GebcoFeature.class));

    expectedBody = new GebcoFeature();
    expectedBody.setName("Teddy Bear");
    expectedBody.setGenericTerm("Ridge");
    expectedBody.setVersion(6);
    expectedBody.setGeometries(Collections.singletonList("POINT (1 1)"));
    expectedBody.setProposerId("44");
    expectedBody.setDiscovererId("55");
    expectedBody.setDiscoveryYear(1987);
    expectedBody.setProposalYear(2023);
    expectedBody.setMeetings(Collections.singletonList("SCUFN-36"));
    expectedBody.setMinDepth(10);
    expectedBody.setMaxDepth(100);
    expectedBody.setTotalRelief(45);
    expectedBody.setDimension("33 x 100");
    expectedBody.setHistory("Named after a teddy bear.");
    expectedBody.setRemarks("It's fluffy");
    expectedBody.setComments("Teddy");
    assertEquals(expectedBody, objectMapper.readValue(request3.getUtf8Body(), GebcoFeature.class));

    expectedBody = new GebcoFeature();
    expectedBody.setVersion(7);
    expectedBody.setApprovalState("READY");
    assertEquals(expectedBody, objectMapper.readValue(request6.getUtf8Body(), GebcoFeature.class));


    tx.executeWithoutResult(s -> {
      assertEquals("More survey information", featureRepository.findById(id).get().getNote());
    });
  }

}