Feature: Register a new trainee

  Scenario: Successful registration
    Given a trainee named "aziz" "murodov" born on "2005-01-17" living at "toshkent"
    When the trainee registers at "/trainees/register"
    Then the response status should be 200
    And the response body should contain a username and password


  Scenario: Successful registration without birthdate and address
    Given a trainee named "aziz" "murodov" born on "" living at ""
    When the trainee registers at "/trainees/register"
    Then the response status should be 200
    And the response body should contain a username and password

  Scenario: Unsuccessful registration without lastname
    Given a trainee named "aziz" "" born on "2005-01-17" living at "tashkent"
    When the trainee registers at "/trainees/register"
    Then the response status should be 400
    And the body contains contains a list of errors

  Scenario: Successful registration without firstname
    Given a trainee named "" "murodov" born on "2005-01-17" living at "toshkent"
    When the trainee registers at "/trainees/register"
    Then the response status should be 400
    And the body contains contains a list of errors

