Feature: Delete a trainee

  Scenario: Trainee deletes their own profile
    When Checked for trainee with username "javohir.sattorov", trainee should exist
    And I send a DELETE request to "/trainees/javohir.sattorov"
    Then the response status is 200
    But the trainee with username "javohir.sattorov" should not exist
