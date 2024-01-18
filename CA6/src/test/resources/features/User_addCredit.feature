Feature: Increasing User Credit

  Scenario: Adding credit to the user account with a positive amount
    Given the anonymous user
    When user credit added by positive value
    Then the user credit should be increased

  Scenario: Attempting to add negative credit to the user account
    Given the anonymous user
    When the user attempts to add some negative credit
    Then an InvalidCreditRange should occur