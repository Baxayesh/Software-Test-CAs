package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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
        public void GIVEN_AnonymousUser_WHEN_AddingNegativeCredit_THEN_WillThrowInvalidCreditRange() {

            var user = createAnonymousUserWithCredit(0);
            var negativeIncrement = -1f;

            Assertions.assertThrows(
                    InvalidCreditRange.class,
                    () -> user.addCredit(negativeIncrement)
            );
        }

        @Test
        public void GIVEN_AnonymousUser_WHEN_AddingZeroCredit_THEN_UserCreditWillNotChange()
                throws InvalidCreditRange {

            var user = createAnonymousUserWithCredit(0);
            var zeroIncrement = 0f;

            user.addCredit(zeroIncrement);

            Assertions.assertEquals(0f, user.getCredit());
        }

        @Test
        public void GIVEN_AnonymousUser_WHEN_AddingPositiveCredit_THEN_UserCreditWillBeIncreased()
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


        @Test
        public void GIVEN_UserWithZeroCredit_WHEN_WithdrawingByNegativeValue_THEN_WillThrowInvalidCreditRange
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
                () throws InsufficientCredit, InvalidCreditRange {

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
        public void GIVEN_AnonymousUser_WHEN_AddingNullItemToBuyList_THEN_ShouldThrowNullPointerException(){

            var user = createUserWithEmptyBuyList();
            Commodity nullItem = null;

            Assertions.assertThrows(
                    NullPointerException.class,
                    () -> user.addBuyItem(nullItem)
            );
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

        
        @Test
        public void GIVEN_AnonymousUser_WHEN_RemovingNullItemToBuyList_THEN_ShouldThrowNullPointerException(){

            var user = createUserWithEmptyBuyList();

            Commodity nullItem = null;

            Assertions.assertThrows(
                    NullPointerException.class,
                    () -> user.removeItemFromBuyList(nullItem)
            );
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


        @Test
        public void GIVEN_AnonymousUser_WHEN_AddingANewPurchasedItem_THEN_ItemShouldBeAdded(){

            var user = createUserWitEmptyPurchasedList();

            user.addPurchasedItem("Purchased Item", 1);

            UserPurchasedListShouldBe(user, Map.of("Purchased Item", 1));
        }

        @Test
        public void GIVEN_UserWithNonEmptyPurchasedList_WHEN_AddingADuplicatePurchasedItem_THEN_ItemQuantityShouldBeIncreased(){

            var user = createUserWithGivenPurchasedList(Map.of("Purchased Item", 1));

            user.addPurchasedItem("Purchased Item", 1);

            UserPurchasedListShouldBe(user, Map.of("Purchased Item", 2));
        }

        @Test
        public void GIVEN_AnonymousUser_WHEN_AddingPurchasedItemWithNegativeQuantity_THEN_ShouldThrowException(){

            var user = createUserWitEmptyPurchasedList();

            Assertions.assertThrows(
                    Exception.class,
                    () -> user.addPurchasedItem("item", -1)
            );

        }

        @Test
        public void GIVEN_AnonymousUser_WHEN_AddingPurchasedItemWithZeroQuantity_THEN_ShouldNotAddItemToUser(){
            var user = createUserWitEmptyPurchasedList();

            user.addPurchasedItem("item", 0);

            UserPurchasedListShouldBe(user, Map.of());
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void GIVEN_AnonymousUser_WHEN_AddingPurchasedItemWithNullOrEmptyItemName_THEN_ShouldThrowNullPointerException
                (String itemName){
            var user = createUserWitEmptyPurchasedList();

            Assertions.assertThrows(
                    NullPointerException.class,
                    () -> user.addPurchasedItem(itemName, 1)
            );
        }
    }

}