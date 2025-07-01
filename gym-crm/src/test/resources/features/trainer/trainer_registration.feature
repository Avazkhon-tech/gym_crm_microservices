Feature: Register a new trainer

  Scenario: Successful registration
    Given a trainer with name "olim" and surname "karimov" exists
    When the trainer registers at "/trainers/register"
    Then the response status should be 200
    And the response body for trainer registration contains a username and password

  Scenario: Successful registration
    Given a trainer with name "" and surname "karimov" exists
    When the trainer registers at "/trainers/register"
    Then the response status should be 400
    And the body contains contains a list of errors

  Scenario: Successful registration
    Given a trainer with name "olim" and surname "" exists
    When the trainer registers at "/trainers/register"
    Then the response status should be 400
    And the body contains contains a list of errors