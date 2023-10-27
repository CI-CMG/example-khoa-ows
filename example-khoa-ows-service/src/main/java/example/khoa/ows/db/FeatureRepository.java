package example.khoa.ows.db;

import example.khoa.ows.jpa.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<FeatureEntity, String> {

}
