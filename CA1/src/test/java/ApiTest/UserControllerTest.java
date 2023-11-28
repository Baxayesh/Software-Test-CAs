package ApiTest;

import static ApiTest.ResponseObjectMatcher.responseBody;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.BalootApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.UserController;
import defines.Errors;
import exceptions.NotExistentUser;
import model.User;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;

import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import service.Baloot;

import java.util.Map;


@SpringBootTest(classes = BalootApplication.class)
@DisplayName("User Controller Api Tests")
public class UserControllerTest {



    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Baloot baloot;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        controller.setBaloot(baloot);
    }

    User CreateAnonymousUserById(String id){
        var user = new User();
        user.setUsername(id);
        return user;
    }

    @Nested
    @DisplayName("Tests For /users/{id}")
    public class getUserTests{

        static String URI = "/users/{id}";

        @Test
        void CALLING_getUser_WHEN_validUserId_THEN_returnUserWith200() throws Exception {

            var id = "userId";
            var expectedUser = CreateAnonymousUserById(id);
            when(baloot.getUserById(id)).thenReturn(expectedUser);

            mockMvc
                    .perform(get(URI, id))
                    .andExpect(status().isOk())
                    .andExpect(responseBody().containsObjectAsJson(expectedUser, User.class));
        }

        @Test
        void CALLING_getUser_WHEN_notExistentUser_THEN_returnErrorWith404() throws Exception {

            var id = "userId";
            when(baloot.getUserById(id)).thenThrow(NotExistentUser.class);

            mockMvc
                    .perform(get(URI, id))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(""));
        }


    }

    @Nested
    @DisplayName("Tests For /users/{id}/credit")
    public class addCreditTests{

        static String URI = "/users/{id}/credit";

        @Test
        void CALLING_addCredit_WHEN_validUserValidCredit_THEN_success() throws Exception{

            var id = "userId";
            when(baloot.getUserById(id)).thenReturn(new User());
            var body = Map.of("credit","1");

            mockMvc
                    .perform(
                            post(URI, id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("credit added successfully!"));
        }


        @Test
        void CALLING_addCredit_WHEN_notExistentUser_THEN_returnErrorWith404() throws Exception {

            var id = "userId";
            when(baloot.getUserById(id)).thenThrow(new NotExistentUser());
            var body = Map.of("credit","0");

            mockMvc
                    .perform(
                            post(URI, id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("User does not exist."));
        }


        @Test
        void CALLING_addCredit_WHEN_negativeCredit_THEN_returnErrorWith400() throws Exception {

            var id = "userId";
            when(baloot.getUserById(id)).thenReturn(new User());
            var body = Map.of("credit","-1");

            mockMvc
                    .perform(
                            post(URI, id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Credit value must be a positive float"));
        }

        @Test
        void CALLING_addCredit_WHEN_textAsCredit_THEN_returnErrorWith400() throws Exception{


            var id = "userId";
            when(baloot.getUserById(id)).thenReturn(new User());
            var body = Map.of("credit","some credit");

            mockMvc
                    .perform(
                            post(URI, id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Please enter a valid number for the credit amount."));

        }

    }


}
