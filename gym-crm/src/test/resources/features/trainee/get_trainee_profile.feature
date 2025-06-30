Feature: Get Trainee Profile

  Scenario: Successfully get trainee profile by username
    Given a trainee with username "aziz.murodov" exists
    When I send a GET request to "/trainees/aziz.murodov"
    Then the response status should be 200
    Then the trainee profile is not empty
