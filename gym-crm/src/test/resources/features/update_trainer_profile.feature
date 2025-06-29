Feature: Update trainer profile

  Scenario: Trainer updates their profile successfully
    Given a trainer to update with username "olim.karimov" exists
    When trainer updates their profile with new details
    Then the response status is 200
    And the response for trainer profile update request contains updated info
