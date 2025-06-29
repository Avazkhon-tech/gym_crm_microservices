Feature: Register a new trainer

  Scenario: Successful registration
    Given a trainer named "olim" "karimov" born on "2005-01-17" living at "toshkent"
    When the trainer registers at "/trainers/register"
    And the response body for trainer registration contains a username and password
    And the response status is 200