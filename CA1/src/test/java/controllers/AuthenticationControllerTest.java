package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import exceptions.UsernameAlreadyTaken;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.Map;

import static org.mockito.Mockito.*;

@DisplayName("Tests On Authentication Controller")
class AuthenticationControllerTest {

    @Mock
    Baloot BalootMock;

    @InjectMocks
    private AuthenticationController Controller;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GIVEN_ValidCreditential_WHEN_CallingLogin_Then_ShouldReturn200Status
            () throws NotExistentUser, IncorrectPassword {

        var username = "user";
        var password = "1234";
        var body = Map.of("username",username,"password",password);

        var response = Controller.login(body);

        verify(BalootMock).login(username,password);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void GIVEN_InvalidUsername_WHEN_CallingLogin_Then_ShouldReturn404Status
            () throws NotExistentUser, IncorrectPassword {

        String username = "not_user";
        String password = "1234";
        var body = Map.of("username",username,"password",password);

        doThrow(NotExistentUser.class)
                .when(BalootMock)
                .login(username,password);

        var response = Controller.login(body);

        verify(BalootMock).login(username,password);
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
    @Test
    public void GIVEN_InvalidPassword_WHEN_CallingLogin_Then_ShouldReturn403Status
            () throws NotExistentUser, IncorrectPassword {

        String username = "user";
        String password = "12345";
        var body = Map.of("username",username,"password",password);

        doThrow(IncorrectPassword.class)
                .when(BalootMock)
                .login(username,password);

        var response = Controller.login(body);

        verify(BalootMock).login(username,password);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
    }



    @Test
    public void GIVEN_ValidCreditential_WHEN_CallingSignUp_Then_ShouldReturn200Status
            () throws  UsernameAlreadyTaken {

        String address = "address";
        String birthDate = "birthDate";
        String email = "email";
        String username = "username";
        String password = "password";

        var body = Map.of("address",address,"birthDate" , birthDate , "email" , email , "username", username , "password",password);

        var response = Controller.signup(body);
        verify(BalootMock).addUser(userArgumentCaptor.capture());
        var addedUser = userArgumentCaptor.getValue();

        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertEquals(addedUser.getUsername() , username );
    }

    @Test
    public void GIVEN_DuplicateUserName_WHEN_CallingSignUp_Then_ShouldReturn400Status
            () throws  UsernameAlreadyTaken {

        String address = "address";
        String birthDate = "birthDate";
        String email = "email";
        String username = "duplicate_username";
        String password = "password";

        var body = Map.of("address",address,"birthDate" , birthDate , "email" , email , "username", username , "password",password);
        doThrow(UsernameAlreadyTaken.class)
                .when(BalootMock)
                .addUser(userArgumentCaptor.capture());
        var response = Controller.signup(body);

        var wrongUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertEquals(wrongUser.getUsername() , username );
    }
}