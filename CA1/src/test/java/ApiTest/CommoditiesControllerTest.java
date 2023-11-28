package ApiTest;

import static ApiTest.ResponseObjectListMatcher.responseListBody;
import static ApiTest.ResponseObjectMatcher.responseBody;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.BalootApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultMatcher;
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

    ArrayList<Commodity> CreateListOfAnonymousCommodities(){
        var list = new ArrayList<Commodity>();
        list.add(new Commodity());
        list.add(new Commodity());
        list.add(new Commodity());
        return list;
    }

    @Nested
    @DisplayName("Tests For /commodities/{id}/comment")
    public class CommentTests{

        String URI = "/commodities/{id}/comment";

        @Test
        public void CALLING_addCommodityComment_WHEN_validComment_THEN_success() throws Exception {

            var commodityId = "1";
            var userId = "user";
            var user = CreateAnonymousUserById(userId);
            when(baloot.getUserById(userId)).thenReturn(user);

            var body = Map.of("username",userId, "comment","comment text");

            mockMvc
                    .perform(
                            post(URI, commodityId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("comment added successfully!"));


        }

        @Test
        public void CALLING_getCommodityComment_WHEN_validComment_THEN_success() throws Exception {

            var commodityId =1;
            var commentList = new ArrayList<Comment>();
            commentList.add(new Comment());
            commentList.add(new Comment());
            commentList.add(new Comment());
            when(baloot.getCommentsForCommodity(commodityId)).thenReturn(commentList);

            mockMvc
                    .perform(
                            get(URI, commodityId)
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(commentList));
        }
    }

    @Nested
    @DisplayName("Tests For /commodities/{id}/rate")
    public  class RatingTests {

    }

    @Nested
    @DisplayName("Tests For /commodities/search")
    public  class SearchTests {

        String URI = "/commodities/search";

        @Test
        public void CALLING_searchCommodities_WHEN_searchOptionIsName_THEN_shouldReturnSearchAnswer() throws Exception{

            var searchValue = "some search Value";
            var searchResult = CreateListOfAnonymousCommodities();
            var searchOption = "name";
            when(baloot.filterCommoditiesByName(searchValue)).thenReturn(searchResult);
            var body = Map.of("searchOption",searchOption, "searchValue", searchValue);

            mockMvc
                    .perform(
                            post(URI)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(searchResult));
        }

        @Test
        public void CALLING_searchCommodities_WHEN_searchOptionIsCategory_THEN_shouldReturnSearchAnswer() throws Exception{

            var searchValue = "some search Value";
            var searchResult = CreateListOfAnonymousCommodities();
            var searchOption = "category";
            when(baloot.filterCommoditiesByCategory(searchValue)).thenReturn(searchResult);
            var body = Map.of("searchOption",searchOption, "searchValue", searchValue);

            mockMvc
                    .perform(
                            post(URI)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(searchResult));
        }

        @Test
        public void CALLING_searchCommodities_WHEN_searchOptionIsProvider_THEN_shouldReturnSearchAnswer() throws Exception{

            var searchValue = "some search Value";
            var searchResult = CreateListOfAnonymousCommodities();
            var searchOption = "provider";
            when(baloot.filterCommoditiesByProviderName(searchValue)).thenReturn(searchResult);
            var body = Map.of("searchOption",searchOption, "searchValue", searchValue);

            mockMvc
                    .perform(
                            post(URI)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(searchResult));
        }

        @Test
        public void CALLING_searchCommodities_WHEN_searchOptionIsInvalid_THEN_shouldReturnEmptyList() throws Exception{

            var searchValue = "some search Value";
            var searchOption = "invalid option";
            var body = Map.of("searchOption",searchOption, "searchValue", searchValue);

            mockMvc
                    .perform(
                            post(URI)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(new ArrayList<>()));
        }

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
