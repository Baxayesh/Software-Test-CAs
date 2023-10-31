package controllers;

import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tests On Commodity Controller")
class CommoditiesControllerTest {


    @Mock
    Baloot BalootMock;

    @InjectMocks
    CommoditiesController Controller;


    @BeforeEach
    public void InitMocks(){
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("Tests On Commodity Comments")
    public class CommentTests{

        @Captor
        ArgumentCaptor<Comment> CommentCaptor;

        @BeforeEach
        public void InitMocks(){
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void GIVEN_AnonymousCommentExists_WHEN_GettingCommentOfCommodity_THEN_ShouldReturnCommodityCommentsWith200Response()  {
            ArrayList<Comment> comments = new ArrayList<>();
            comments.add(new Comment());
            when(BalootMock.getCommentsForCommodity(1)).thenReturn(comments);

            var response = Controller.getCommodityComment("1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(comments, response.getBody());
        }


        @Test
        public void GIVEN_AnonymousUserExists_WHEN_UserAddsCommentsToCommodity_THEN_ShouldAddCommentAndReturn200()
                throws NotExistentUser {

            var commentId = "1";
            var username = "ali";
            var userEmail = "ali@gmail.com";
            var commentText = "bla bla bla";
            var body = Map.of("username", username, "comment", commentText);
            var user = mock(User.class);
            when(user.getUsername()).thenReturn(username);
            when(user.getEmail()).thenReturn(userEmail);
            when(BalootMock.getUserById(username)).thenReturn(user);

            var response = Controller.addCommodityComment(commentId, body);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(BalootMock).addComment(CommentCaptor.capture());
            var addedComment = CommentCaptor.getValue();
            assertEquals(commentText, addedComment.getText());
            assertEquals(1, addedComment.getCommodityId());
            assertEquals(userEmail, addedComment.getUserEmail());
            assertEquals(username, addedComment.getUsername());
        }

        @Test
        public void GIVEN_UsernameNotExistsInBaloot_WHEN_AddingCommentToCommodity_THEN_ShouldAddCommentWithNullUsernameAndIdAndShouldReturn200() throws NotExistentUser {
            var commentId = "1";
            var username = "ali";
            var commentText = "bla bla bla";
            var body = Map.of("username", username, "comment", commentText);

            when(BalootMock.getUserById(username)).thenThrow(NotExistentUser.class);

            var response = Controller.addCommodityComment(commentId, body);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(BalootMock).addComment(CommentCaptor.capture());
            var addedComment = CommentCaptor.getValue();
            assertEquals(commentText, addedComment.getText());
            assertEquals(1, addedComment.getCommodityId());
            assertNull(addedComment.getUserEmail());
            assertNull(addedComment.getUsername());
        }


    }

    @Nested
    @DisplayName("Test On Commodity Ratings")
    public  class RatingTests{

        @Test
        public void GIVEN_AnonymousCommodityExists_WHEN_AddingPositiveRateToCommodity_THEN_ShouldReturn200() throws  NotExistentCommodity {

            Commodity commodity = spy(Commodity.class);
            when(BalootMock.getCommodityById("1")).thenReturn(commodity);
            var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "2"));
            verify(commodity).addRate("ali",2);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("rate added successfully!" ,  response.getBody());

        }


        @Test
        public void GIVEN_AnonymousCommodityNotExists_WHEN_AddingPositiveRateToCommodity_THEN_ShouldReturn404() throws  NotExistentCommodity {

            when(BalootMock.getCommodityById("1")).thenThrow(NotExistentCommodity.class);

            var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "2"));

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        public void GIVEN_AnonymousCommodityExists_WHEN_AddingWrongRateToCommodity_THEN_ShouldReturn400()  {
            var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "aa"));
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }


    }

    @Nested
    @DisplayName("Tests On Commodity Querying/Search/Suggestion")
    public class QueryTests{

        @Test
        public void GIVEN_ThereAreSomeCommoditiesOnBaloot_WHEN_GettingCommodities_THEN_ShouldReturnRegisteredCommoditiesWith200Response(){

            ArrayList<Commodity> commodities = new ArrayList<>();
            commodities.add(new Commodity());
            when(BalootMock.getCommodities()).thenReturn(commodities);

            var response = Controller.getCommodities();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodities, response.getBody());

        }

        @Test
        public void GIVEN_ThereIsCommodityById1OnBaloot_WHEN_GettingCommodityById_THEN_ShouldReturnCommodityById1With200Response() throws  NotExistentCommodity {

            Commodity commodity = new Commodity();

            when(BalootMock.getCommodityById("1")).thenReturn(commodity);

            var response = Controller.getCommodity("1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodity, response.getBody());

        }

        @Test
        public void GIVEN_ThereIsNoCommodityById1OnBaloot_WHEN_GettingCommodityById_THEN_ShouldReturn404Response() throws  NotExistentCommodity {
            when(BalootMock.getCommodityById("1")).thenThrow(NotExistentCommodity.class);
            var response = Controller.getCommodity("1");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull( response.getBody());
        }


        @Test
        public void GIVEN_AnonymousCommodityExists_WHEN_GettingSuggestionOnCommodity_THEN_ShouldReturnSuggestionWithCode200() throws NotExistentCommodity {

            var commodityId = "1";
            var suggestions = new ArrayList<Commodity>();
            suggestions.add(new Commodity());
            suggestions.add(new Commodity());
            var commodity = mock(Commodity.class);
            when(BalootMock.getCommodityById(commodityId)).thenReturn(commodity);
            when(BalootMock.suggestSimilarCommodities(commodity)).thenReturn(suggestions);

            var response = Controller.getSuggestedCommodities(commodityId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(suggestions, response.getBody());
        }

        @Test
        public void GIVEN_CommodityNotExists_WHEN_GettingSuggestionOnCommodity_THEN_ShouldReturn404() throws NotExistentCommodity {

            var commodityId = "1";

            when(BalootMock.getCommodityById(commodityId)).thenThrow(NotExistentCommodity.class);

            var response = Controller.getSuggestedCommodities(commodityId);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }


        @Test
        public void GIVEN_AnonymousCommoditiesExists_WHEN_SearchCommoditiesWithOptionName_THEN_ShouldReturnCommodityWith200Response()  {
            String searchOption = "name";
            String searchValue = "something";

            ArrayList<Commodity> commodities = new ArrayList<>();
            commodities.add(new Commodity());

            when(BalootMock.filterCommoditiesByName(searchValue)).thenReturn(commodities);

            var response = Controller.searchCommodities(Map.of("searchOption" , searchOption ,"searchValue", searchValue ));
            verify(BalootMock).filterCommoditiesByName(searchValue);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodities, response.getBody());
        }

        @Test
        public void GIVEN_AnonymousCommoditiesExists_WHEN_SearchCommoditiesWithOptionCategory_THEN_ShouldReturnCommodityWith200Response()  {
            String searchOption = "category";
            String searchValue = "something";

            ArrayList<Commodity> commodities = new ArrayList<>();
            commodities.add(new Commodity());

            when(BalootMock.filterCommoditiesByCategory(searchValue)).thenReturn(commodities);

            var response = Controller.searchCommodities(Map.of("searchOption" , searchOption ,"searchValue", searchValue ));
            verify(BalootMock).filterCommoditiesByCategory(searchValue);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodities, response.getBody());
        }

        @Test
        public void GIVEN_AnonymousCommoditiesExists_WHEN_SearchCommoditiesWithOptionProvider_THEN_ShouldReturnCommodityWith200Response()  {
            String searchOption = "provider";
            String searchValue = "something";

            ArrayList<Commodity> commodities = new ArrayList<>();
            commodities.add(new Commodity());

            when(BalootMock.filterCommoditiesByProviderName(searchValue)).thenReturn(commodities);

            var response = Controller.searchCommodities(Map.of("searchOption" , searchOption ,"searchValue", searchValue ));
            verify(BalootMock).filterCommoditiesByProviderName(searchValue);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodities, response.getBody());
        }
        @Test
        public void GIVEN_AnonymousCommoditiesExists_WHEN_SearchCommoditiesWithNoOption_THEN_ShouldReturnEmptyArrayWith200Response()  {
            String searchOption = "wrong";
            String searchValue = "something";

            ArrayList<Commodity> commodities = new ArrayList<>();
            var response = Controller.searchCommodities(Map.of("searchOption" , searchOption ,"searchValue", searchValue ));
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(commodities, response.getBody());
        }
    }


}