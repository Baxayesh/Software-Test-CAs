package domain;

import org.junit.Assert;
import org.junit.Test;

public class OrderTest {


    @Test
    public void GIVEN_AnonymousOrder_WHEN_ComparingWithNonOrderObject_THEN_NotEqual(){

        var order = new Order();

        var otherNonOrderObject = new Object();

        var result = order.equals(otherNonOrderObject);

        Assert.assertFalse(result);

    }


    @Test
    public void GIVEN_AnonymousOrder_WHEN_ComparingWithItself_THEN_Equal(){

        var order = new Order();

        var result = order.equals(order);

        Assert.assertTrue(result);

    }

    @Test
    public void TestIdGetter(){

        var order = new Order();
        order.setId(5);

        Assert.assertEquals(5, order.getId());
    }

    @Test
    public void TestPriceGetter(){

        var order = new Order();
        order.setPrice(100);

        Assert.assertEquals(100, order.getPrice());
    }

    @Test
    public void TestCustomerGetter(){

        var order = new Order();
        order.setCustomer(10);

        Assert.assertEquals(10, order.getCustomer());
    }

    @Test
    public void TestQuantityGetter(){
        var order = new Order();
        order.setQuantity(15);

        Assert.assertEquals(15, order.getQuantity());

    }
}