package ApiTest;

import static ApiTest.ResponseObjectListMatcher.responseListBody;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.CommoditiesController;
import exceptions.NotExistentCommodity;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
        String URI = "/commodities/{id}/rate";

        @Test
        public void CALLING_addCommodityRate_WHEN_validCommodityAndRate_THEN_success() throws Exception {

            int rate  = 1;
            String username = "username";
            String commodityId = "1";
            Commodity tempCommodity = new Commodity();
            when(baloot.getCommodityById(commodityId)).thenReturn(tempCommodity);

            var body = Map.of("rate",rate, "username",username);

            mockMvc
                    .perform(
                            post(URI, commodityId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("rate added successfully!"));


        }

        @Test
        public void CALLING_addCommodityRate_WHEN_validCommodityAndInvalidRate_THEN_error() throws Exception {

            var rate  = "a";
            String username = "username";
            String commodityId = "1";
            Commodity tempCommodity = new Commodity();
            when(baloot.getCommodityById(commodityId)).thenReturn(tempCommodity);

            var body = Map.of("rate",rate, "username",username);

            mockMvc
                    .perform(
                            post(URI, commodityId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("For input string: \""+rate+"\""));
                    // invalid message

        }
        @Test
        public void CALLING_addCommodityRate_WHEN_invalidCommodityAndvalidRate_THEN_error() throws Exception {

            var rate  = 1;
            String username = "username";
            String commodityId = "1";
            Commodity tempCommodity = new Commodity();
            when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

            var body = Map.of("rate",rate, "username",username);

            mockMvc
                    .perform(
                            post(URI, commodityId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Commodity does not exist."));
            // invalid message

        }
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

        String URI = "/commodities/{id}/suggested";

        @Test
        public void CALLING_getSuggestedCommodities_WHEN_validId_THEN_success() throws Exception{

            var commodityId = 1;
            var suggestions = CreateListOfAnonymousCommodities();
            when(baloot.suggestSimilarCommodities(any())).thenReturn(suggestions);

            mockMvc
                    .perform(
                            get(URI, commodityId)
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(suggestions));

        }


        @Test
        public void CALLING_getSuggestedCommodities_WHEN_NonExistentCommodityId_THEN_shouldReturnEmptyListWith404() throws Exception{

            var commodityId = "commodityId";
            when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

            mockMvc
                    .perform(
                            get(URI, commodityId)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("[]"));

        }
    }

    @Nested
    @DisplayName("Tests For /commodities/{id} and /commodities")
    public  class FetchTests {

        String URI = "/commodities";
        String URI_WITH_ID = "/commodities/{id}";
        @Test
        public void CALLING_getCommodities_WHEN_nothing_THEN_success() throws Exception {
            var commodityList = CreateListOfAnonymousCommodities();
            when(baloot.getCommodities()).thenReturn(commodityList);

            mockMvc
                    .perform(
                            get(URI)
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseListBody().containsObjectAsJson(commodityList));
        }

        @Test
        public void CALLING_getCommodity_WHEN_validId_THEN_success() throws Exception {
            var commodityId = "1";
            var tempCommodity = new Commodity();
            when(baloot.getCommodityById(commodityId)).thenReturn(tempCommodity);

            mockMvc
                    .perform(
                            get(URI_WITH_ID,commodityId )
                    )
                    .andExpect(status().isOk())
                    .andExpect(responseBody().containsObjectAsJson(tempCommodity , Commodity.class));
        }

        @Test
        public void CALLING_getCommodity_WHEN_invalidId_THEN_error() throws Exception {
            var commodityId = "1";

            when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

            mockMvc
                    .perform(
                            get(URI_WITH_ID,commodityId )
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(""));
        }
    }

}
