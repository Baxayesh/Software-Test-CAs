package stepdefinitions;

import exceptions.CommodityIsNotInBuyList;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Commodity;
import model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

public class User_removeItemFromBuyList_Steps {

    private User anonymousUser;
    private Exception thrownException;


    private static final String CommodityIdWithMultipleItemsInBuyList = "multiple";
    private static final String CommodityIdWithASingleItemInBuyList = "single";
    private static final String CommodityIdNotInBuyList = "unknown";



    Commodity CreateCommodityWithId(String id){
        var commodity = new Commodity();
        commodity.setId(id);
        return  commodity;
    }
    @Given("the anonymous user with some items in buy list")
    public void theAnonymousUserWithSomeItemsInBuyList() {
        anonymousUser = new User();
        var singleCommodity = CreateCommodityWithId(CommodityIdWithASingleItemInBuyList);
        var multipleCommodity = CreateCommodityWithId(CommodityIdWithMultipleItemsInBuyList);

        anonymousUser.addBuyItem(singleCommodity);
        anonymousUser.addBuyItem(multipleCommodity);
        anonymousUser.addBuyItem(multipleCommodity);
    }

    @When("removing an item with current quantity more than one")
    public void removingAnItemWithCurrentQuantityMoreThan() throws CommodityIsNotInBuyList {
        var removing = CreateCommodityWithId(CommodityIdWithMultipleItemsInBuyList);
        anonymousUser.removeItemFromBuyList(removing);
    }

    @Then("the item quantity should be decreased")
    public void theItemQuantityShouldBeDecreased() {
        var quantity = anonymousUser.getBuyList().get(CommodityIdWithMultipleItemsInBuyList);
        Assertions.assertEquals(1,quantity);
    }

    @When("removing an item with just one item")
    public void removingAnItemWithJustOneItem() throws CommodityIsNotInBuyList {
        var removing = CreateCommodityWithId(CommodityIdWithASingleItemInBuyList);
        anonymousUser.removeItemFromBuyList(removing);
    }

    @Then("the item should be removed from buy list")
    public void theItemShouldBeRemovedFromBuyList() {
        Assertions.assertFalse(
                anonymousUser
                        .getBuyList()
                        .containsKey(
                                CommodityIdWithASingleItemInBuyList
                        )
        );
    }

    @Given("Some anonymous user")
    public void someAnonymousUser() {
        anonymousUser = new User();
    }

    @When("removing a commodity that is not in buy list")
    public void removingACommodityThatIsNotInBuyList() {
        try{
            var removing = CreateCommodityWithId(CommodityIdNotInBuyList);
            anonymousUser.removeItemFromBuyList(removing);
        }catch (Exception ex){
            thrownException = ex;
        }
    }

    @Then("a CommodityIsNotInBuyList should occur")
    public void aCommodityIsNotInBuyListShouldOccur() {
        Assert.assertTrue(thrownException instanceof CommodityIsNotInBuyList);
    }


}
