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

}