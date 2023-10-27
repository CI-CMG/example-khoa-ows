package example.khoa.ows.api.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeatureView {

  private String id;
  private String name;
  private String genericTerm;
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
  private String gebcoFeatureStateId;
  private Integer gebcoFeatureVersion;
  private String gebcoApprovalState;
  private String owsNote;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public List<String> getGeometries() {
    return geometries;
  }

  public void setGeometries(List<String> geometries) {
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

  public String getGebcoFeatureStateId() {
    return gebcoFeatureStateId;
  }

  public void setGebcoFeatureStateId(String gebcoFeatureStateId) {
    this.gebcoFeatureStateId = gebcoFeatureStateId;
  }

  public Integer getGebcoFeatureVersion() {
    return gebcoFeatureVersion;
  }

  public void setGebcoFeatureVersion(Integer gebcoFeatureVersion) {
    this.gebcoFeatureVersion = gebcoFeatureVersion;
  }

  public String getGebcoApprovalState() {
    return gebcoApprovalState;
  }

  public void setGebcoApprovalState(String gebcoApprovalState) {
    this.gebcoApprovalState = gebcoApprovalState;
  }

  public String getOwsNote() {
    return owsNote;
  }

  public void setOwsNote(String owsNote) {
    this.owsNote = owsNote;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureView that = (FeatureView) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(genericTerm, that.genericTerm)
        && Objects.equals(geometries, that.geometries) && Objects.equals(proposerId, that.proposerId) && Objects.equals(
        discovererId, that.discovererId) && Objects.equals(discoveryYear, that.discoveryYear) && Objects.equals(proposalYear,
        that.proposalYear) && Objects.equals(meetings, that.meetings) && Objects.equals(minDepth, that.minDepth)
        && Objects.equals(maxDepth, that.maxDepth) && Objects.equals(totalRelief, that.totalRelief) && Objects.equals(
        dimension, that.dimension) && Objects.equals(history, that.history) && Objects.equals(remarks, that.remarks)
        && Objects.equals(comments, that.comments) && Objects.equals(gebcoFeatureStateId, that.gebcoFeatureStateId)
        && Objects.equals(gebcoFeatureVersion, that.gebcoFeatureVersion) && Objects.equals(gebcoApprovalState,
        that.gebcoApprovalState) && Objects.equals(owsNote, that.owsNote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, genericTerm, geometries, proposerId, discovererId, discoveryYear, proposalYear, meetings, minDepth, maxDepth,
        totalRelief, dimension, history, remarks, comments, gebcoFeatureStateId, gebcoFeatureVersion, gebcoApprovalState, owsNote);
  }

  @Override
  public String toString() {
    return "FeatureView{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", genericTerm='" + genericTerm + '\'' +
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
        ", gebcoFeatureStateId='" + gebcoFeatureStateId + '\'' +
        ", gebcoFeatureVersion=" + gebcoFeatureVersion +
        ", gebcoApprovalState='" + gebcoApprovalState + '\'' +
        ", owsNote='" + owsNote + '\'' +
        '}';
  }
}
