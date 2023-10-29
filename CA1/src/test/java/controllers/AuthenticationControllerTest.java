package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


class AuthenticationControllerTest {

    @Mock
    Baloot BalootMock;

    @InjectMocks
    private AuthenticationController Controller;

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



}