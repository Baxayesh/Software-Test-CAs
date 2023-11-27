package ApiTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.BalootApplication;
import controllers.CommoditiesController;
import controllers.UserController;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import service.Baloot;

import java.util.ArrayList;
import java.util.Map;


@SpringBootTest(classes = BalootApplication.class)
@DisplayName("Commodities Controller Api Tests")
public class CommoditiesControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private CommoditiesController controller;

    @MockBean
    private Baloot baloot;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        controller.setBaloot(baloot);
    }



    @Nested
    @DisplayName("Tests For /commodities/{id}/comment")
    public class CommentTests{



    }

    @Nested
    @DisplayName("Tests For /commodities/{id}/rate")
    public  class RatingTests {


    }

    @Nested
    @DisplayName("Tests For /commodities/search")
    public  class SearchTests {


    }

    @Nested
    @DisplayName("Tests For /commodities/{id}/suggested")
    public  class SuggestionTests {


    }

    @Nested
    @DisplayName("Tests For /commodities/{id} and /commodities")
    public  class FetchTests {


    }

}
