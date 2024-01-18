Feature: Removing Commodity from User's Buy List

  Scenario: Removing a commodity with quantity greater than one
    Given the anonymous user with some items in buy list
    When removing an item with current quantity more than one
    Then the item quantity should be decreased

  Scenario: Removing the last item of a commodity
    Given the anonymous user with some items in buy list
    When removing an item with just one item
    Then the item should be removed from buy list

  Scenario: Attempting to remove a commodity not in the buy list
    Given Some anonymous user
    When removing a commodity that is not in buy list
    Then a CommodityIsNotInBuyList should occur