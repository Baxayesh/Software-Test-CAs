package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UserTest {

    @Nested
    @DisplayName("Tests That Effect User Credit")
    class UserCreditTests {
        User createAnonymousUserWithCredit(float initialCredit){
            var user = new User();
            user.setCredit(initialCredit);
            return user;
        }


        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_AddingNegativeCredit_THEN_WillThrowInvalidCreditRange() {

            var user = createAnonymousUserWithCredit(0);
            var negativeIncrement = -1f;

            Assertions.assertThrows(
                    InvalidCreditRange.class,
                    () -> user.addCredit(negativeIncrement)
            );
        }

        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_AddingZeroCredit_THEN_UserCreditWillNotChange()
                throws InvalidCreditRange {

            var user = createAnonymousUserWithCredit(0);
            var zeroIncrement = 0f;

            user.addCredit(zeroIncrement);

            Assertions.assertEquals(0f, user.getCredit());
        }

        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_AddingPositiveCredit_THEN_UserCreditWillBeIncreased()
                throws InvalidCreditRange {

            var user = createAnonymousUserWithCredit(0);
            var positiveIncrement = 1f;

            user.addCredit(positiveIncrement);

            Assertions.assertEquals(1f, user.getCredit());
        }


        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_WithdrawingMoreThanUserCredit_THEN_WillThrowInsufficientCredit
                (){

            var user = createAnonymousUserWithCredit(0);
            var withdrawAmount = 1f;

            Assertions.assertThrows(
                    InsufficientCredit.class,
                    () -> user.withdrawCredit(withdrawAmount)
            );
        }

        //TODO: ask TA: why code doesn't handle this case? is it a bug in code?
        // am i allowed to write such tests?
        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_WithdrawingByNegativeValue_THEN_WillThrowSomeException
        (){

            var user = createAnonymousUserWithCredit(0);
            var withdrawAmount = -1f;

            Assertions.assertThrows(
                    InvalidCreditRange.class,
                    () -> user.withdrawCredit(withdrawAmount)
            );
        }

        @Test
        public void GIVEN_UserWithSomeCredit_WHEN_WithdrawingLessThanUserCredit_THEN_WillDecreaseUserCredit
                () throws InsufficientCredit {

            var user = createAnonymousUserWithCredit(10f);
            var withdrawAmount = 1f;

            user.withdrawCredit(withdrawAmount);

            Assertions.assertEquals(9f, user.getCredit());
        }
    }

    @Nested
    @DisplayName("Tests For User Buy List")
    public class BuyListTests{

        Commodity createCommodityWithId(String id){
            var commodity = new Commodity();
            commodity.setId(id);
            return commodity;
        }

        User createUserWithEmptyBuyList(){
            return new User();
        }

        User createUserWithGivenBuyList(Map<String,Integer> commodities){
            var user = new User();
            user.setBuyList(new HashMap<>(commodities));
            return user;
        }

        void UserBuyListShouldBe(User user, Map<String, Integer> expectedBuyList){
            var userBuyList = user.getBuyList();
            Assertions.assertEquals(userBuyList, expectedBuyList);
        }

        @Test
        public void GIVEN_UserWithEmptyBuyList_WHEN_AddingNewItemToBuyList_THEN_ItemShouldBeAddedToBuyList(){

            var user = createUserWithEmptyBuyList();
            var newItem = createCommodityWithId("item");

            user.addBuyItem(newItem);

            UserBuyListShouldBe(user, Map.of("item",1));
        }

        @Test
        public void GIVEN_UserWithNonEmptyBuyList_WHEN_AddingNewItemToBuyList_THEN_BuyListShouldContainBothOldAndNewItems(){

            var user = createUserWithGivenBuyList(Map.of("old",1));
            var newItem = createCommodityWithId("new");

            user.addBuyItem(newItem);

            UserBuyListShouldBe(
                user,
                Map.of(
                    "old",1,
                    "new", 1
                )
            );
        }


        @Test
        public void GIVEN_UserWithNonEmptyBuyList_WHEN_AddingDuplicateItemToBuyList_THEN_QuantityOfItemShouldBeIncreased(){

            var user = createUserWithGivenBuyList(Map.of("item",1));
            var newItem = createCommodityWithId("item");

            user.addBuyItem(newItem);

            UserBuyListShouldBe(
                    user,
                    Map.of(
                            "item", 2
                    )
            );
        }

        @Test
        public void  GIVEN_UserWithEmptyBuyList_WHEN_RemovingItemThatIsNotInBuyList_THEN_WillThrowCommodityIsNotInBuyList(){

            var user = createUserWithEmptyBuyList();
            var item = createCommodityWithId("item");

            Assertions.assertThrows(
                    CommodityIsNotInBuyList.class,
                    () -> user.removeItemFromBuyList(item)
            );
        }

        @Test
        public void  GIVEN_UserWithNonEmptyBuyList_WHEN_RemovingItemThatIsNotInBuyList_THEN_WillThrowCommodityIsNotInBuyList(){

            var user = createUserWithGivenBuyList(Map.of("item",1));
            var item = createCommodityWithId("another item");

            Assertions.assertThrows(
                    CommodityIsNotInBuyList.class,
                    () -> user.removeItemFromBuyList(item)
            );
        }

        //TODO: any better name?
        @Test
        public void GIVEN_UserWithNonEmptyBuyList_WHEN_RemovingItemWithCurrentQuantityOfOne_THEN_ItemShouldBeDeletedFromList
                () throws CommodityIsNotInBuyList {

            var user = createUserWithGivenBuyList(Map.of("item", 1));
            var item = createCommodityWithId("item");

            user.removeItemFromBuyList(item);

            UserBuyListShouldBe(user,Map.of());
        }


        @Test
        public void GIVEN_UserWithNonEmptyBuyList_WHEN_RemovingItemWithMoreThanOneQuantity_THEN_ItemQuantityShouldBeDecreased
                () throws CommodityIsNotInBuyList {

            var user = createUserWithGivenBuyList(Map.of("item", 2));
            var item = createCommodityWithId("item");

            user.removeItemFromBuyList(item);

            UserBuyListShouldBe(user,Map.of("item", 1));
        }
    }

    @Nested
    @DisplayName("Tests For User Purchased List")
    public class PurchasedListTests {


        User createUserWitEmptyPurchasedList(){
            return createUserWithGivenPurchasedList(Map.of());
        }

        User createUserWithGivenPurchasedList(Map<String,Integer> purchases){
            var user = new User();
            user.setPurchasedList(new HashMap<>(purchases));
            return user;
        }

        void UserPurchasedListShouldBe(User user, Map<String, Integer> expectedPurchasedList){
            var userPurchasedList = user.getPurchasedList();
            Assertions.assertEquals(userPurchasedList, expectedPurchasedList);
        }

        //TODO: should add tests for negative quantity?

        @Test
        public void GIVEN_UserWithEmptyPurchasedList_WHEN_AddingANewItem_THEN_ItemShouldBeAdded(){

            var user = createUserWitEmptyPurchasedList();

            user.addPurchasedItem("Purchased Item", 1);

            UserPurchasedListShouldBe(user, Map.of("Purchased Item", 1));
        }

        @Test
        public void GIVEN_UserWithNonEmptyPurchasedList_WHEN_AddingADuplicateItem_THEN_ItemQuantityShouldBeIncreased(){

            var user = createUserWithGivenPurchasedList(Map.of("Purchased Item", 1));

            user.addPurchasedItem("Purchased Item", 1);

            UserPurchasedListShouldBe(user, Map.of("Purchased Item", 2));
        }

    }

}