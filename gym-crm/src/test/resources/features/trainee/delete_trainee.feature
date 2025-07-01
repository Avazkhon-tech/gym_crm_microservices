Feature: Delete a trainee
  Scenario: Trainee deletes their own profile
    Given a trainee with username "javohir.sattorov" exists
    And the trainee sends a DELETE request to "/trainees/javohir.sattorov"
    Then the response status should be 200
    But the trainee with username "javohir.sattorov" should not exist
