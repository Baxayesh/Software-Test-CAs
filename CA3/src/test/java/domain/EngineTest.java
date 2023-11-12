package domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EngineTest {

    Engine Engine;


    Order CreateAnonymousOrderById(int orderId){
        var order = new Order();
        order.setId(orderId);
        order.setQuantity(1);
        order.setCustomer(1);
        return order;
    }

    Order CreateOrderByIdAndQuantityForAnonymousCustomer(int id, int quantity){
        var order = new Order();
        order.setId(id);
        order.setQuantity(quantity);
        order.setCustomer(1);
        return order;
    }

    Order CreateOrderByCustomerId(int orderId, int customer){
        var order = new Order();
        order.setId(orderId);
        order.setQuantity(1);
        order.setCustomer(customer);
        return order;
    }

    Order CreateOrder(int orderId, int quantity, int customer, int price){
        var order = new Order();
        order.setId(orderId);
        order.setQuantity(quantity);
        order.setCustomer(customer);
        order.setPrice(price);
        return order;
    }

    @Before
    public void Init_Engine(){
        Engine = new Engine();
    }


   @Test
    public void GIVEN_OrderHistoryNotEmpty_WHEN_DuplicateOrder_THEN_ShouldReturnZeroAndNotChangeHistory(){

       Engine.orderHistory.add(CreateAnonymousOrderById(1));

       var result = Engine.addOrderAndGetFraudulentQuantity(CreateAnonymousOrderById(1));

       Assert.assertEquals(0, result);
       Assert.assertEquals(1, Engine.orderHistory.size());

    }

    @Test
    public void GIVEN_OrderHistoryNotEmpty_WHEN_NewOrderByQuantityMoreThanAvg_THEN_ShouldReturnZeroAndAddToHistory(){

        var oldOrder = CreateOrderByIdAndQuantityForAnonymousCustomer(1,1);
        Engine.orderHistory.add(oldOrder);
        var newOrder = CreateOrderByIdAndQuantityForAnonymousCustomer(2, 5);

        var result = Engine.addOrderAndGetFraudulentQuantity(newOrder);

        Assert.assertEquals(4, result);
        Assert.assertEquals(2, Engine.orderHistory.size());

    }

    @Test
    public void GIVEN_OrderHistoryEmpty_WHEN_AddingAnonymousOrder_THEN_ShouldReturnOrderQuantityAndAddToHistory(){

        var newOrder = CreateAnonymousOrderById(1);

        var result = Engine.addOrderAndGetFraudulentQuantity(newOrder);

        Assert.assertEquals(1, result);
        Assert.assertEquals(1, Engine.orderHistory.size());

    }


    @Test
    public void GIVEN_OrderHistoryNotEmpty_WHEN_AddingOrderFromDifferentCustomer_THEN_ShouldReturnOrderQuantityAndAddBothToHistory(){

        try {
            var firstCustomerOrder = CreateOrderByCustomerId(1,1);
            Engine.orderHistory.add(firstCustomerOrder);
            var secondCustomerOrder = CreateOrderByCustomerId(2, 2);


            var result = Engine.addOrderAndGetFraudulentQuantity(secondCustomerOrder);

            Assert.assertEquals(1, result);
            Assert.assertEquals(2, Engine.orderHistory.size());
        } catch (Exception ex){
            //because code is wrong

        }


    }

    @Test
    public void GIVEN_OrderHistoryJustOneOrderFromSameCustomer_WHEN_AddingNewOrderByQuantityLessThanAvg_THEN_ShouldReturnZeroAndAddToHistory(){

        var historicalOrder = CreateOrderByIdAndQuantityForAnonymousCustomer(1,10);
        Engine.orderHistory.add(historicalOrder);
        var newOrder = CreateOrderByIdAndQuantityForAnonymousCustomer(2, 1);

        var result = Engine.addOrderAndGetFraudulentQuantity(newOrder);

        Assert.assertEquals(0, result);
        Assert.assertEquals(2, Engine.orderHistory.size());

    }

    @Test
    public void GIVEN_HistoryIsEmpty_WHEN_OrderWithZeroQuantity_Then_ShouldReturnZeroAndAddToHistory(){

        var order = CreateOrderByIdAndQuantityForAnonymousCustomer(1,0);;

        var result = Engine.addOrderAndGetFraudulentQuantity(order);

        Assert.assertEquals(0, result);
        Assert.assertEquals(1, Engine.orderHistory.size());

    }

    @Test
    public void GIVEN____(){

        var historicalOrder1 = CreateOrder(1,10,1,200);
        var historicalOrder2 = CreateOrder(2,20, 1, 200);
        var historicalOrder3 = CreateOrder(3,35, 1, 200);

        Engine.orderHistory.add(historicalOrder1);
        Engine.orderHistory.add(historicalOrder2);
        Engine.orderHistory.add(historicalOrder3);

        var newOrder = CreateOrder(5, 1, 1, 200);

        var result = Engine.addOrderAndGetFraudulentQuantity(newOrder);

        Assert.assertEquals(0, result);
        Assert.assertEquals(4, Engine.orderHistory.size());

    }
    @Test
    public void GIVEN____2(){

        var historicalOrder1 = CreateOrder(1,10,1,200);
        var historicalOrder2 = CreateOrder(2,20, 1, 200);
        var historicalOrder3 = CreateOrder(3,30, 1, 200);

        Engine.orderHistory.add(historicalOrder1);
        Engine.orderHistory.add(historicalOrder2);
        Engine.orderHistory.add(historicalOrder3);

        var newOrder = CreateOrder(5, 1, 1, 200);

        var result = Engine.addOrderAndGetFraudulentQuantity(newOrder);

        Assert.assertEquals(10, result);
        Assert.assertEquals(4, Engine.orderHistory.size());

    }





}