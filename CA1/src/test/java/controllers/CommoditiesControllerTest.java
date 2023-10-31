package controllers;

import exceptions.NotExistentComment;
import exceptions.NotExistentCommodity;
import model.Comment;
import model.Commodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestExecutionListeners;
import service.Baloot;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommoditiesControllerTest {


    @Mock
    Baloot BalootMock;

    @InjectMocks
    CommoditiesController Controller;

    @Spy
    Comment AnonymousSpyComment;

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
    public void GIVEN_AnonymousCommodityNotExists_WHEN_AddingPositiveRateToCommodity_THEN_ShouldReturn404() throws  NotExistentCommodity {

        when(BalootMock.getCommodityById("1")).thenThrow(NotExistentCommodity.class);

        var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "2"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void GIVEN_AnonymousCommodityExists_WHEN_AddingWrongRateToCommodity_THEN_ShouldReturn400() throws  NotExistentCommodity {
        var response = Controller.rateCommodity("1" , Map.of("username" , "ali" , "rate" , "aa"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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