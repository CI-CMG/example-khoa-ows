package example.khoa.ows.api.controller;

import example.khoa.ows.service.FeatureService;
import example.khoa.ows.api.view.FeatureView;
import example.khoa.ows.api.view.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class ApiController {

  private final FeatureService featureService;

  @Autowired
  public ApiController(FeatureService featureService) {
    this.featureService = featureService;
  }

  @GetMapping(path = "list")
  public ListView<FeatureView> list() {
    return featureService.list();
  }

  @PostMapping(path = "load")
  public FeatureView load(@RequestBody FeatureView feature) {
    return featureService.load(feature);
  }

  @PostMapping(path = "disapprove")
  public FeatureView disapprove(@RequestBody FeatureView feature) {
    return featureService.disapprove(feature);
  }

  @PostMapping(path = "pending")
  public FeatureView pending(@RequestBody FeatureView feature) {
    return featureService.pending(feature);
  }

  @PostMapping(path = "ready")
  public FeatureView ready(@RequestBody FeatureView feature) {
    return featureService.ready(feature);
  }

  @PostMapping(path = "update")
  public FeatureView update(@RequestBody FeatureView feature) {
    return featureService.update(feature);
  }

  @PostMapping(path = "approve")
  public FeatureView approve(@RequestBody FeatureView feature) {
    return featureService.approve(feature);
  }

}
