package model;

import exceptions.NotInStock;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.util.Date;


class CommentTest {

    static Date currentDate ;
    static Comment comment ;
    @BeforeAll
    static void setup(){
        currentDate = new Date();
        comment = new Comment(2,"test@test.com","test",32,"good");
    }

    @DisplayName("Tests Date Comment")
    @Nested
    class DateCommentTest{
        @Test
        public void GIVEN_Comment_THEN_CommentDateEqualNowDate() {
            Assertions.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate), comment.getDate());

        }
    }

    @DisplayName("Tests Vote Comment")
    @Nested
    class VoteCommentTests{
        @Test
        public void GIVEN_Comment_WHEN_LikeDuplicateUser_THEN_LikeCountNotChange() {
            comment.addUserVote("amin" , "like");
            comment.addUserVote("amin" , "like");
            Assertions.assertEquals(1, comment.getLike());
            Assertions.assertEquals(0, comment.getDislike());
        }
        @Test
        public void GIVEN_Comment_WHEN_LikeChangeToDislike_THEN_LikeCountChange() {
            comment.addUserVote("amin" , "like");
            comment.addUserVote("amin" , "dislike");
            Assertions.assertEquals(0, comment.getLike());
            Assertions.assertEquals(1, comment.getDislike());
        }
        @Test
        public void GIVEN_Comment_WHEN_LikeAndDislikeMultiUser_THEN_CheckLikeAndDislikeCount() {
            comment.addUserVote("amin" , "like");
            comment.addUserVote("ali" , "dislike");
            comment.addUserVote("amir" , "like");
            Assertions.assertEquals(2, comment.getLike());
            Assertions.assertEquals(1, comment.getDislike());
        }
        @Test
        public void GIVEN_Comment_WHEN_NotLikeOrDislike_THEN_LikeAndDislikeCountEqualZero() {
            Assertions.assertEquals(0, comment.getLike());
            Assertions.assertEquals(0, comment.getDislike());
        }
        @Test
        public void GIVEN_Comment_WHEN_WrongLikeOrDislike_THEN_LikeAndDislikeCountEqualZero() {
            comment.addUserVote("amin" , "likee");
            comment.addUserVote("ali" , "dislikee");
            comment.addUserVote("amir" , "likee");
            Assertions.assertEquals(0, comment.getLike());
            Assertions.assertEquals(0, comment.getDislike());
        }
    }
}