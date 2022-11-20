package shams.moneylionassessment.assessment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shams.moneylionassessment.assessment.data.dto.FeatureAccessDto;
import shams.moneylionassessment.assessment.data.dto.FeatureDto;
import shams.moneylionassessment.assessment.data.models.Feature;
import shams.moneylionassessment.assessment.data.requests.CreateFeatureRequest;
import shams.moneylionassessment.assessment.data.requests.FeatureAssignRequest;
import shams.moneylionassessment.assessment.service.FeatureService;
import shams.moneylionassessment.assessment.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    final FeatureService featureService;
    final UserService userService;

    public FeatureController(FeatureService featureService, UserService userService) {
        this.featureService = featureService;
        this.userService = userService;
    }

    @GetMapping(value = "/features", produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<FeatureDto>> listFeatures() {
        return ResponseEntity.ok(featureService.list().stream().map(FeatureDto::of).collect(Collectors.toList()));
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> canUserAccessFeature(@RequestParam("email") String username, @RequestParam("featureName") String featureName) {

        boolean userExists = userService.userExists(username);
        boolean featureExists = featureService.featureExists(featureName);

        if(userExists && featureExists) {
            return ResponseEntity.ok(new FeatureAccessDto().setCanAccess(featureService.hasFeatureAccess(username, featureName)));
        } else {
            return ResponseEntity.badRequest().body(notExistError(userExists, featureExists, username, featureName));
        }
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<FeatureDto> findFeatureByName(@PathVariable(value = "name") String name) {
        Feature feature = featureService.getFeature(name);

        if(feature != null) {
            return ResponseEntity.ok(FeatureDto.of(feature));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> assignFeature(@RequestBody FeatureAssignRequest request) {
        String username = request.getEmail();
        String featureName = request.getFeatureName();
        boolean enable = request.getEnable();
        boolean hasFeatureAccess = featureService.hasFeatureAccess(username, featureName);

        if(enable == hasFeatureAccess) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } else {
            boolean userExists = userService.userExists(username);
            boolean featureExists = featureService.featureExists(featureName);

            if(userExists && featureExists) {
                if (enable) {
                    featureService.assignFeature(username, featureName); // Ignoring return type
                } else {
                    featureService.revokeFeature(username, featureName);
                }

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body(notExistError(userExists, featureExists, username, featureName));
            }
        }
    }

    // Should be a post endpoint based on the REST API guidelines, but it's being used by the assign/revoke feature endpoint as mentioned in the assignment
    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> saveFeature(@RequestBody CreateFeatureRequest request) {
        String name = request.getName();

        if(featureService.featureExists(name)) {
            return ResponseEntity.badRequest().body(String.format("A feature with name %s already exists", name));
        } else {
            Feature created = featureService.saveFeature(name);
            return ResponseEntity.ok(String.format("A feature has been created with name %s", created.name()));
        }
    }

    private String notExistError(boolean userExists, boolean featureExists, String username, String featureName) {
        List<String> errorMessages = new ArrayList<>();

        if(!userExists) errorMessages.add(String.format("User email (%s) does not exist", username));
        if(!featureExists) errorMessages.add(String.format("Feature (%s) does not exist", featureName));

        return String.join(" ", errorMessages);
    }
}