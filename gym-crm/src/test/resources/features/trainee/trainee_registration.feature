Feature: Register a new trainee

  Scenario: Successful registration
    Given a trainee named "aziz" "murodov" born on "2005-01-17" living at "toshkent"
    When the trainee registers at "/trainees/register"
    Then the response status should be 200
    And the response body contains a username and password
