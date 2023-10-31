package controllers;

import exceptions.NotExistentComment;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestExecutionListeners;
import service.Baloot;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommoditiesControllerTest {


    @Mock
    Baloot BalootMock;

    @InjectMocks
    CommoditiesController Controller;


    @Captor
    ArgumentCaptor<Comment> CommentCaptor;

    @BeforeEach
    public void InitMocks(){
        MockitoAnnotations.openMocks(this);
    }


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
    public void GIVEN_AnonymousCommodityExists_WHEN_AddingPositiveRateToCommodity_THEN_ShouldReturn200() throws  NotExistentCommodity {

        Commodity commodity = spy(Commodity.class);
        when(BalootMock.getCommodityById("1")).thenReturn(commodity);
        var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "2"));
        verify(commodity).addRate("ali",2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("rate added successfully!" ,  response.getBody());

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
}