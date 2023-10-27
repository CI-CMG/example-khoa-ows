package example.khoa.ows.service;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GebcoFeature {

  private String featureId;
  private String featureStateId;
  private String name;
  private String genericTerm;
  private Integer version;
  private String approvalState;
  private List<String> geometries = new ArrayList<>(0);
  private String proposerId;
  private String discovererId;
  private Integer discoveryYear;
  private Integer proposalYear;
  private List<String> meetings = new ArrayList<>(0);
  private Integer minDepth;
  private Integer maxDepth;
  private Integer totalRelief;
  private String dimension;
  private String history;
  private String remarks;
  private String comments;

  private Instant timeLastUpdated;
  private String editor;
  private String owner;

  private Map<String, Object> otherFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  @JsonAnySetter
  private void setOtherField(String name, Object value) {
    this.otherFields.put(name, value);
  }

  public String getFeatureId() {
    return featureId;
  }

  public void setFeatureId(String featureId) {
    this.featureId = featureId;
  }

  public String getFeatureStateId() {
    return featureStateId;
  }

  public void setFeatureStateId(String featureStateId) {
    this.featureStateId = featureStateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGenericTerm() {
    return genericTerm;
  }

  public void setGenericTerm(String genericTerm) {
    this.genericTerm = genericTerm;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getApprovalState() {
    return approvalState;
  }

  public void setApprovalState(String approvalState) {
    this.approvalState = approvalState;
  }

  public List<String> getGeometries() {
    return geometries;
  }

  public void setGeometries(List<String> geometries) {
    if (geometries == null) {
      geometries = new ArrayList<>(0);
    }
    this.geometries = geometries;
  }

  public String getProposerId() {
    return proposerId;
  }

  public void setProposerId(String proposerId) {
    this.proposerId = proposerId;
  }

  public String getDiscovererId() {
    return discovererId;
  }

  public void setDiscovererId(String discovererId) {
    this.discovererId = discovererId;
  }

  public Integer getDiscoveryYear() {
    return discoveryYear;
  }

  public void setDiscoveryYear(Integer discoveryYear) {
    this.discoveryYear = discoveryYear;
  }

  public Integer getProposalYear() {
    return proposalYear;
  }

  public void setProposalYear(Integer proposalYear) {
    this.proposalYear = proposalYear;
  }

  public List<String> getMeetings() {
    return meetings;
  }

  public void setMeetings(List<String> meetings) {
    if (meetings == null) {
      meetings = new ArrayList<>(0);
    }
    this.meetings = meetings;
  }

  public Integer getMinDepth() {
    return minDepth;
  }

  public void setMinDepth(Integer minDepth) {
    this.minDepth = minDepth;
  }

  public Integer getMaxDepth() {
    return maxDepth;
  }

  public void setMaxDepth(Integer maxDepth) {
    this.maxDepth = maxDepth;
  }

  public Integer getTotalRelief() {
    return totalRelief;
  }

  public void setTotalRelief(Integer totalRelief) {
    this.totalRelief = totalRelief;
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }

  public String getHistory() {
    return history;
  }

  public void setHistory(String history) {
    this.history = history;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Instant getTimeLastUpdated() {
    return timeLastUpdated;
  }

  public void setTimeLastUpdated(Instant timeLastUpdated) {
    this.timeLastUpdated = timeLastUpdated;
  }

  public String getEditor() {
    return editor;
  }

  public void setEditor(String editor) {
    this.editor = editor;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GebcoFeature that = (GebcoFeature) o;
    return Objects.equals(featureId, that.featureId) && Objects.equals(featureStateId, that.featureStateId)
        && Objects.equals(name, that.name) && Objects.equals(genericTerm, that.genericTerm) && Objects.equals(version,
        that.version) && Objects.equals(approvalState, that.approvalState) && Objects.equals(geometries, that.geometries)
        && Objects.equals(proposerId, that.proposerId) && Objects.equals(discovererId, that.discovererId)
        && Objects.equals(discoveryYear, that.discoveryYear) && Objects.equals(proposalYear, that.proposalYear)
        && Objects.equals(meetings, that.meetings) && Objects.equals(minDepth, that.minDepth) && Objects.equals(maxDepth,
        that.maxDepth) && Objects.equals(totalRelief, that.totalRelief) && Objects.equals(dimension, that.dimension)
        && Objects.equals(history, that.history) && Objects.equals(remarks, that.remarks) && Objects.equals(comments,
        that.comments) && Objects.equals(timeLastUpdated, that.timeLastUpdated) && Objects.equals(editor, that.editor)
        && Objects.equals(owner, that.owner) && Objects.equals(otherFields, that.otherFields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(featureId, featureStateId, name, genericTerm, version, approvalState, geometries, proposerId, discovererId, discoveryYear,
        proposalYear, meetings, minDepth, maxDepth, totalRelief, dimension, history, remarks, comments, timeLastUpdated, editor, owner, otherFields);
  }

  @Override
  public String toString() {
    return "GebcoFeature{" +
        "featureId='" + featureId + '\'' +
        ", featureStateId='" + featureStateId + '\'' +
        ", name='" + name + '\'' +
        ", genericTerm='" + genericTerm + '\'' +
        ", version=" + version +
        ", approvalState='" + approvalState + '\'' +
        ", geometries=" + geometries +
        ", proposerId='" + proposerId + '\'' +
        ", discovererId='" + discovererId + '\'' +
        ", discoveryYear=" + discoveryYear +
        ", proposalYear=" + proposalYear +
        ", meetings=" + meetings +
        ", minDepth=" + minDepth +
        ", maxDepth=" + maxDepth +
        ", totalRelief=" + totalRelief +
        ", dimension='" + dimension + '\'' +
        ", history='" + history + '\'' +
        ", remarks='" + remarks + '\'' +
        ", comments='" + comments + '\'' +
        ", timeLastUpdated=" + timeLastUpdated +
        ", editor='" + editor + '\'' +
        ", owner='" + owner + '\'' +
        ", otherFields=" + otherFields +
        '}';
  }
}
