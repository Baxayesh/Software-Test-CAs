package model;

import exceptions.NotInStock;
import org.junit.jupiter.api.*;


class CommodityTest {


    @Nested
    @DisplayName("Tests updateInStock")
    class UpdateInStockTests {
        static Commodity commodity;
        @BeforeAll
        static void createAnonymousCommodity(){
            commodity = new Commodity();
            commodity.setInStock(5);
        }
        @Test
        public void GIVEN_AnonymousCommodity_WHEN_AddingNegativeInStock_THEN_WillThrowNotInStock() throws NotInStock {
            var negativeInStock = -10;
            commodity.updateInStock(negativeInStock);
            Assertions.assertThrows(
                    NotInStock.class,
                    () -> commodity.updateInStock(negativeInStock)
            );
        }

        @Test
        public void GIVEN_AnonymousCommodity_WHEN_AddingZeroInStock_THEN_CommodityInStockWillNotChange()
                throws NotInStock{

            var zeroIncrement = 0;
            commodity.updateInStock(zeroIncrement);
            Assertions.assertEquals(5, commodity.getInStock());
        }

        @Test
        public void GIVEN_AnonymousCommodity_WHEN_AddingPositiveInStock_THEN_CommodityInStockWillBeIncreased()
                throws NotInStock {
            var positiveIncrement = 10;
            commodity.updateInStock(positiveIncrement);
            Assertions.assertEquals(15, commodity.getInStock());
        }
    }

    @Nested
    @DisplayName("Tests For Rating Commodity")
    public class RatingTests{

        Commodity createAnonymousCommodityInitRate (float initRate){
            var commodity = new Commodity();
            commodity.setInitRate(initRate);
            return commodity;
        }
//        Commodity createAnonymousCommodityWithRated(){
//            var commodity = new Commodity();
//            return commodity;
//        }

        @Test
        public void GIVEN_AnonymousCommodity_WHEN_AddingDuplicateRateToCommodity_THEN_RateShouldChanged(){

            var commodity = createAnonymousCommodityInitRate(5);
            commodity.addRate("amin" ,10);
            commodity.addRate("amin" ,3);
            Assertions.assertEquals(4, commodity.getRating());
        }
        @Test
        public void GIVEN_AnonymousCommodity_WHEN_AddingNotDuplicateRateToCommodity_THEN_RateShouldChanged(){

            var commodity = createAnonymousCommodityInitRate(2);
            commodity.addRate("amin" ,3);
            commodity.addRate("ali" ,1);
            Assertions.assertEquals(2, commodity.getRating());
        }

//        @Test
//        public void GIVEN_AnonymousCommodity_WHEN_AddingNegetiveRateToCommodity_THEN_ThrowError(){
//
//            var commodity = createAnonymousCommodityInitRate(2);
//            commodity.addRate("amin" ,-3);
//            Assertions.assertThrows(, commodity.getRating());
//        }
    }

}