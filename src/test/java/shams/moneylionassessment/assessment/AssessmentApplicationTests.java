package shams.moneylionassessment.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import shams.moneylionassessment.assessment.data.dto.TokenDto;
import shams.moneylionassessment.assessment.data.requests.AuthRequest;
import shams.moneylionassessment.assessment.data.requests.CreateFeatureRequest;
import shams.moneylionassessment.assessment.data.requests.CreateUserRequest;
import shams.moneylionassessment.assessment.data.requests.FeatureAssignRequest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssessmentApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String adminUsername = "shams@gmail.com"; // Todo: take this from properties file
    private final String adminPassword = "password"; // Todo: take this from properties file
    @Autowired
    private MockMvc mockMvc;

    private String serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void obtainTokenSuccess() throws Exception {
        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword(adminPassword));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    void obtainTokenFail() throws Exception {
        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword("invalidPassword"));

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid")));
    }

    @Test
    void createFeature() throws Exception {
        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword(adminPassword));
        MvcResult authResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest)).andReturn();
        TokenDto tokenDto = objectMapper.readValue(authResult.getResponse().getContentAsString(), TokenDto.class);

        String createFeatureRequest = serialize(new CreateFeatureRequest().setName("add_user"));
        mockMvc.perform(put("/api/feature") // Can be a post endpoint, but it's being used by the assign feature
                .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")));
    }

    @Test
    void createUser() throws Exception {
        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword(adminPassword));
        MvcResult authResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest)).andReturn();
        TokenDto tokenDto = objectMapper.readValue(authResult.getResponse().getContentAsString(), TokenDto.class);

        String createUserRequest = serialize(new CreateUserRequest().setUsername("shams2").setPassword("password"));
        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createUserRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("successfully")));
    }

    // An integration test for feature assignment
    @Test
    void assignFeature() throws Exception {
        String email = "shams3@gmail.com";
        String featureName = "delete_user";

        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword(adminPassword));
        MvcResult authResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest)).andReturn();
        TokenDto tokenDto = objectMapper.readValue(authResult.getResponse().getContentAsString(), TokenDto.class);

        String createFeatureRequest = serialize(new CreateFeatureRequest().setName(featureName));
        mockMvc.perform(put("/api/feature") // Can be a post endpoint, but it's being used by the assign feature endpoint
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")));

        String createUserRequest = serialize(new CreateUserRequest().setUsername(email).setPassword("password"));
        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createUserRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("successfully")));

        String assignFeatureRequest = serialize(new FeatureAssignRequest().setFeatureName(featureName).setEmail(email).setEnable(true));
        mockMvc.perform(post("/api/feature")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(assignFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        String assignFeatureRequest2 = serialize(new FeatureAssignRequest().setFeatureName(featureName).setEmail(email).setEnable(true));
        mockMvc.perform(post("/api/feature")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(assignFeatureRequest2))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string(containsString("")));

        mockMvc.perform(get("/api/feature").param("email", email).param("featureName", featureName)
                        .header("Authorization", "Bearer " + tokenDto.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    // An integration test for feature revocation
    @Test
    void revokeFeature() throws Exception {
        String email = "shams4@gmail.com";
        String featureName = "update_user";

        String authRequest = serialize(new AuthRequest().setUsername(adminUsername).setPassword(adminPassword));
        MvcResult authResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(authRequest)).andReturn();
        TokenDto tokenDto = objectMapper.readValue(authResult.getResponse().getContentAsString(), TokenDto.class);

        String createFeatureRequest = serialize(new CreateFeatureRequest().setName(featureName));
        mockMvc.perform(put("/api/feature") // Can be a post endpoint, but it's being used by the assign feature endpoint
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")));

        String createUserRequest = serialize(new CreateUserRequest().setUsername(email).setPassword("password"));
        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(createUserRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("successfully")));

        String assignFeatureRequest = serialize(new FeatureAssignRequest().setFeatureName(featureName).setEmail(email).setEnable(true));
        mockMvc.perform(post("/api/feature")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(assignFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/feature").param("email", email).param("featureName", featureName)
                        .header("Authorization", "Bearer " + tokenDto.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        String revokeFeatureRequest = serialize(new FeatureAssignRequest().setFeatureName(featureName).setEmail(email).setEnable(false));
        mockMvc.perform(post("/api/feature")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON).content(revokeFeatureRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));

        mockMvc.perform(get("/api/feature").param("email", email).param("featureName", featureName)
                        .header("Authorization", "Bearer " + tokenDto.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }
}
