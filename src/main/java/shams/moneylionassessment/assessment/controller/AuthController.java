package shams.moneylionassessment.assessment.controller;

import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shams.moneylionassessment.assessment.data.dto.TokenDto;
import shams.moneylionassessment.assessment.data.models.User;
import shams.moneylionassessment.assessment.data.requests.AuthRequest;
import shams.moneylionassessment.assessment.security.JwtTokenUtil;
import shams.moneylionassessment.assessment.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest request) {
        try {
            User user = userService.authenticate(request.getUsername(), request.getPassword());

            if(user != null) {
                return ResponseEntity.ok().body(new TokenDto().setToken(jwtTokenUtil.generateToken(user)));
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

}
