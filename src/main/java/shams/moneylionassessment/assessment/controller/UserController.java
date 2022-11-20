package shams.moneylionassessment.assessment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shams.moneylionassessment.assessment.data.models.User;
import shams.moneylionassessment.assessment.data.requests.CreateUserRequest;
import shams.moneylionassessment.assessment.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if(username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            boolean userExists = userService.userExists(username);
            if(userExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("User (%s) already exists", username));
            } else {
                User user = userService.createUser(username, password);
                return ResponseEntity.ok(String.format("User (%s) has been created successfully", user.username()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'username' and 'password' fields are required");
        }
    }

}