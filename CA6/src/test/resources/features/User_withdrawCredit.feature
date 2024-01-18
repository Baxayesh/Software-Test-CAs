Feature: User Credit Withdrawal

  Scenario: Withdrawing credit within the available balance
    Given the anonymous user with some initial credit
    When the user withdraws some positive amount of credit
    Then the user credit should be decreased

  Scenario: Attempting to withdraw more credit than available
    Given the anonymous user with some initial credit
    When the user attempts to withdraw more than his credit
    Then an InsufficientCredit should occur