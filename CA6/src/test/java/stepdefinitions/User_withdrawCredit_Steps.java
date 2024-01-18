package stepdefinitions;

import exceptions.InsufficientCredit;
import io.cucumber.java.en.Given;
        import io.cucumber.java.en.When;
        import io.cucumber.java.en.Then;
import model.User;
import org.junit.Assert;

public class User_withdrawCredit_Steps {


    private User anonymousUser;
    private Exception thrownException;

    @Given("the anonymous user with some initial credit")
    public void theAnonymousUserWithSomeInitialCredit() {
        anonymousUser = new User();
        anonymousUser.setCredit(10.0f);
    }

    @When("the user withdraws some positive amount of credit")
    public void theUserWithdrawsSomePositiveAmountOfCredit() {
        try {
            var withdrawCredit = 1.0f;
            anonymousUser.withdrawCredit(withdrawCredit);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the user credit should be decreased")
    public void theUserCreditShouldBeDecreased() {
        Assert.assertEquals(9.0f, anonymousUser.getCredit(), 1e-10); // Adjust delta as needed
    }



    @When("the user attempts to withdraw more than his credit")
    public void theUserAttemptsToWithdrawSomeNegativeAmountOfCredit() {
        try {
            var withdrawCredit = 20.0f;
            anonymousUser.withdrawCredit(withdrawCredit);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an InsufficientCredit should occur")
    public void anInsufficientCreditShouldOccur() {
        Assert.assertTrue(thrownException instanceof InsufficientCredit);
    }

}
