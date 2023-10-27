package example.khoa.ows.jpa;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FEATURE")
public class FeatureEntity {

  @Id
  @Column(name = "ID", length = 36)
  private String id;

  @Column(name = "NOTE", length = 500)
  private String note;

  @Column(name = "GEBCO_FEATURE_STATE_ID", length = 36)
  private String gebcoFeatureStateId;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getGebcoFeatureStateId() {
    return gebcoFeatureStateId;
  }

  public void setGebcoFeatureStateId(String gebcoFeatureStateId) {
    this.gebcoFeatureStateId = gebcoFeatureStateId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureEntity that = (FeatureEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(note, that.note) && Objects.equals(gebcoFeatureStateId,
        that.gebcoFeatureStateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, note, gebcoFeatureStateId);
  }
}
