package example.khoa.ows.service;

import example.khoa.ows.api.controller.ApiException;
import example.khoa.ows.api.view.ApiErrorView;
import example.khoa.ows.api.view.FeatureView;
import example.khoa.ows.api.view.ListView;
import example.khoa.ows.db.FeatureRepository;
import example.khoa.ows.jpa.FeatureEntity;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeatureService {

  private final GebcoService gebcoService;
  private final FeatureRepository featureRepository;

  @Autowired
  public FeatureService(GebcoService gebcoService, FeatureRepository featureRepository) {
    this.gebcoService = gebcoService;
    this.featureRepository = featureRepository;
  }

  public ListView<FeatureView> list() {
    ListView<FeatureView> listView = new ListView<>();
    listView.setItems(featureRepository.findAll().stream().map(featureDb -> {
      GebcoFeature gebcoFeature = gebcoService.getFeature(featureDb.getGebcoFeatureStateId());
      return dbToView(featureDb, gebcoFeature);
    }).collect(Collectors.toList()));
    return listView;
  }

  public FeatureView load(FeatureView feature) {
    FeatureEntity featureDb = viewToDb(feature, null);
    featureDb = featureRepository.saveAndFlush(featureDb);
    GebcoFeature gebcoFeature = gebcoService.createFeature(feature);
    featureDb.setGebcoFeatureStateId(gebcoFeature.getFeatureStateId());
    featureDb = featureRepository.saveAndFlush(featureDb);
    return dbToView(featureDb, gebcoFeature);
  }

  public FeatureView disapprove(FeatureView feature) {
    FeatureEntity featureEntity = featureRepository.findById(feature.getId()).orElseThrow(() -> {
      ApiErrorView errorView = new ApiErrorView();
      errorView.addFlashError("Unable to find feature " + feature.getId());
      return new ApiException("Unable to find feature " + feature.getId(), HttpStatus.NOT_FOUND, errorView);
    });
    featureRepository.delete(featureEntity);
    gebcoService.deleteFeature(featureEntity.getGebcoFeatureStateId());
    return feature;
  }

  public FeatureView pending(FeatureView feature) {
    return changeState(feature.getId(), "PENDING");
  }

  public FeatureView ready(FeatureView feature) {
    return changeState(feature.getId(), "READY");
  }

  public FeatureView edit(FeatureView feature) {
    return changeState(feature.getId(), "EDIT");
  }

  public FeatureView update(FeatureView feature) {
    FeatureView editFeature = edit(feature);
    feature.setGebcoFeatureVersion(editFeature.getGebcoFeatureVersion());
    feature.setGebcoFeatureStateId(editFeature.getGebcoFeatureStateId());
    GebcoFeature gebcoFeature = gebcoService.updateFeature(feature, feature.getGebcoFeatureStateId());
    feature.setGebcoFeatureVersion(gebcoFeature.getVersion());
    feature.setGebcoFeatureStateId(gebcoFeature.getFeatureStateId());
    FeatureEntity featureDb = featureRepository.findById(feature.getId()).orElseThrow(() -> {
      ApiErrorView errorView = new ApiErrorView();
      errorView.addFlashError("Unable to find feature " + feature.getId());
      return new ApiException("Unable to find feature " + feature.getId(), HttpStatus.NOT_FOUND, errorView);
    });
    featureDb = viewToDb(feature, featureDb);
    featureRepository.saveAndFlush(featureDb);
    return ready(feature);
  }

  public FeatureView approve(FeatureView feature) {
    return changeState(feature.getId(), "APPROVED");
  }

  private FeatureView changeState(String id, String state) {
    FeatureEntity featureDb = featureRepository.findById(id).orElseThrow(() -> {
      ApiErrorView errorView = new ApiErrorView();
      errorView.addFlashError("Unable to find feature " + id);
      return new ApiException("Unable to find feature " + id, HttpStatus.NOT_FOUND, errorView);
    });
    GebcoFeature gebcoFeature = gebcoService.updateState(featureDb.getGebcoFeatureStateId(), state);
    featureDb.setGebcoFeatureStateId(gebcoFeature.getFeatureStateId());
    featureDb = featureRepository.saveAndFlush(featureDb);
    return dbToView(featureDb, gebcoFeature);
  }

  private FeatureView dbToView(FeatureEntity featureEntity, GebcoFeature gebcoFeature) {
    FeatureView featureView = new FeatureView();
    featureView.setId(featureEntity.getId());
    featureView.setName(gebcoFeature.getName());
    featureView.setGenericTerm(gebcoFeature.getGenericTerm());
    featureView.setGeometries(gebcoFeature.getGeometries());
    featureView.setProposerId(gebcoFeature.getProposerId());
    featureView.setDiscovererId(gebcoFeature.getDiscovererId());
    featureView.setDiscoveryYear(gebcoFeature.getDiscoveryYear());
    featureView.setProposalYear(gebcoFeature.getProposalYear());
    featureView.setMeetings(gebcoFeature.getMeetings());
    featureView.setMinDepth(gebcoFeature.getMinDepth());
    featureView.setMaxDepth(gebcoFeature.getMaxDepth());
    featureView.setTotalRelief(gebcoFeature.getTotalRelief());
    featureView.setDimension(gebcoFeature.getDimension());
    featureView.setHistory(gebcoFeature.getHistory());
    featureView.setRemarks(gebcoFeature.getRemarks());
    featureView.setComments(gebcoFeature.getComments());
    featureView.setGebcoApprovalState(gebcoFeature.getApprovalState());
    featureView.setGebcoFeatureStateId(featureEntity.getGebcoFeatureStateId());
    featureView.setGebcoFeatureVersion(gebcoFeature.getVersion());
    featureView.setOwsNote(featureEntity.getNote());
    return featureView;
  }

  private FeatureEntity viewToDb(FeatureView featureView, FeatureEntity featureEntity) {
    if (featureEntity == null) {
      featureEntity = new FeatureEntity();
      featureEntity.setId(UUID.randomUUID().toString());
    }
    featureEntity.setGebcoFeatureStateId(featureView.getGebcoFeatureStateId());
    featureEntity.setNote(featureView.getOwsNote());
    return featureEntity;
  }

}
