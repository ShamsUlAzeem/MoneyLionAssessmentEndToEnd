package shams.moneylionassessment.assessment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shams.moneylionassessment.assessment.data.models.Feature;
import shams.moneylionassessment.assessment.data.repository.FeatureRepository;
import shams.moneylionassessment.assessment.data.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/feature")
public class FeaturesController {

    final FeatureRepository featureRepository;
    final UserRepository userRepository;

    public FeaturesController(FeatureRepository featureRepository, UserRepository userRepository) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Feature> showIndex() {
        return ResponseEntity.ok(new Feature().name("ss"));
    }

    @GetMapping("/{name}")
    public String findFeatureByName(@PathVariable(value = "name") String name) {
        Optional<Feature> feature = featureRepository.findById(name);
        return feature.isPresent() ? feature.get().name() : "Not found";
    }

    @GetMapping("/save")
    public String saveFeature(@RequestParam(value = "name") String name) {
        Feature created = featureRepository.save(new Feature().name(name));
        return String.format("A feature has been created with name %s", created.name());
    }
}