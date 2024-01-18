package stepdefinitions;
import exceptions.InvalidCreditRange;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import model.User;
import org.junit.Assert;

public class User_addCredit_Steps {

    private User anonymousUser;
    private Exception thrownException;


    @Given("the anonymous user")
    public void anAnonymousUser() {
        anonymousUser = new User();
        anonymousUser.setCredit(0);
    }

    @When("user credit added by positive value")
    public void userCreditAddedByPositiveValue() {
        try {
            var creditIncrement = 1.0f;
            anonymousUser.addCredit(creditIncrement);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the user credit should be increased")
    public void theUserCreditShouldBeIncreased() {
        Assert.assertEquals(1.0f, anonymousUser.getCredit(), 1e-10);
    }

    @When("the user attempts to add some negative credit")
    public void theUserAttemptsToAddSomeNegativeCredit() {
        try {
            var creditIncrement = -1.0f;
            anonymousUser.addCredit(creditIncrement);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an InvalidCreditRange should occur")
    public void anInvalidCreditRangeShouldOccur() {
        Assert.assertTrue(thrownException instanceof InvalidCreditRange);
    }
}