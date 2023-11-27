package ApiTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.BalootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.UserController;
import model.User;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import service.Baloot;



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


    @Nested
    @DisplayName("Tests For /users/{id}")
    public class getUserTests{

        static String URI = "/users/{id}";

        @Test
        void CALLING_getUser_WHEN_validUserId_THEN_returnUserWith200() throws Exception {

            var id = "userId";
            when(baloot.getUserById(id)).thenReturn(new User());

            mockMvc
                    .perform(get(URI, id))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Test
        void CALLING_getUser_WHEN_notExistentUser_THEN_returnErrorWith404(){
            Assertions.fail("Not Implemented Yet!");
        }


    }

    @Nested
    @DisplayName("Tests For /users/{id}/credit")
    public class addCreditTests{

        static String URI = "/users/{id}/credit";
        @Test
        void CALLING_addCredit_WHEN_validUserValidCredit_THEN_returnErrorWith404(){
            Assertions.fail("Not Implemented Yet!");
        }
        @Test
        void CALLING_addCredit_WHEN_notExistentUser_THEN_returnErrorWith404(){
            Assertions.fail("Not Implemented Yet!");
        }
        @Test
        void CALLING_addCredit_WHEN_notIncludingCredit_THEN_returnErrorWith400(){
            Assertions.fail("Not Implemented Yet!");
        }
        @Test
        void CALLING_addCredit_WHEN_negativeCredit_THEN_returnErrorWith400(){
            Assertions.fail("Not Implemented Yet!");
        }
        @Test
        void CALLING_addCredit_WHEN_textAsCredit_THEN_returnErrorWith400(){
            Assertions.fail("Not Implemented Yet!");
        }

    }


}
