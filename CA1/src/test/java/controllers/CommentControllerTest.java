package controllers;

import exceptions.NotExistentComment;
import model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentControllerTest {

    @Mock
    Baloot BalootMock;

    @InjectMocks
    CommentController Controller;

    @Spy
    Comment AnonymousSpyComment;

    @BeforeEach
    public void InitMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GIVEN_ValidCommentIdAndUsername_WHEN_LikingComment_THEN_UserVoteShouldBeCalled
            () throws NotExistentComment {

        var commentId = "0";
        var username = "username";
        var body = Map.of("username", username);
        when(BalootMock.getCommentById(0)).thenReturn(AnonymousSpyComment);

        var response = Controller.likeComment(commentId,body);

        verify(AnonymousSpyComment).addUserVote(username,"like");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void GIVEN_CommentIdNotExists_WHEN_LikingComment_THEN_ShouldReturn404Status
            () throws NotExistentComment {

        var commentId = "0";
        var username = "username";
        var body = Map.of("username", username);
        when(BalootMock.getCommentById(0)).thenThrow(NotExistentComment.class);

        var response = Controller.likeComment(commentId,body);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void GIVEN_CommentIdIsNotNumber_WHEN_LikingComment_THEN_ShouldReturn400Status
            ()  {

        var commentId = "id";
        var username = "username";
        var body = Map.of("username", username);

        var response = Controller.likeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void GIVEN_CommentIdIsNull_WHEN_LikingComment_THEN_ShouldReturn400Status
            (String commentId)  {

        var username = "username";
        var body = Map.of("username", username);

        var response = Controller.likeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void GIVEN_RequestBodyHasNotUsername_WHEN_LikingComment_THEN_ShouldReturn400Status
            () {

        var commentId = "0";
        var body = new HashMap<String,String>();

        var response = Controller.likeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void GIVEN_ValidCommentIdAndUsername_WHEN_DislikingComment_THEN_UserVoteShouldBeCalled
            () throws NotExistentComment {

        var commentId = "0";
        var username = "username";
        var body = Map.of("username", username);
        when(BalootMock.getCommentById(0)).thenReturn(AnonymousSpyComment);

        var response = Controller.dislikeComment(commentId,body);

        verify(AnonymousSpyComment).addUserVote(username,"dislike");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void GIVEN_CommentIdNotExists_WHEN_DislikingComment_THEN_ShouldReturn404Status
            () throws NotExistentComment {

        var commentId = "0";
        var username = "username";
        var body = Map.of("username", username);
        when(BalootMock.getCommentById(0)).thenThrow(NotExistentComment.class);

        var response = Controller.dislikeComment(commentId,body);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void GIVEN_CommentIdIsNotNumber_WHEN_DislikingComment_THEN_ShouldReturn400Status
            ()  {

        var commentId = "id";
        var username = "username";
        var body = Map.of("username", username);

        var response = Controller.dislikeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void GIVEN_CommentIdIsNull_WHEN_DislikingComment_THEN_ShouldReturn400Status
            (String commentId) {

        var username = "username";
        var body = Map.of("username", username);

        var response = Controller.dislikeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void GIVEN_RequestBodyHasNotUsername_WHEN_DislikingComment_THEN_ShouldReturn400Status
            () {

        var commentId = "0";
        var body = new HashMap<String,String>();

        var response = Controller.dislikeComment(commentId,body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}